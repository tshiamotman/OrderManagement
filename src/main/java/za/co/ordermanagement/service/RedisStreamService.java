package za.co.ordermanagement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.stereotype.Service;
import za.co.ordermanagement.domain.database.Order;
import za.co.ordermanagement.domain.dto.StreamResults;
import za.co.ordermanagement.utils.Constants;
import za.co.ordermanagement.utils.PropertyProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RedisStreamService {
    private final RedisCommands<String, String> syncCommands;

    private final ObjectMapper objectMapper;

    private final PropertyProvider propertyProvider;

    public RedisStreamService(ObjectMapper objectMapper, PropertyProvider propertyProvider) {
        this.objectMapper = objectMapper;
        this.propertyProvider = propertyProvider;
        RedisClient redisClient = RedisClient.create(this.propertyProvider.getRedisUrl());
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        this.syncCommands = connection.sync();
    }

    public String addOrderToStream(Order order) {
        String restaurant = order.getRestaurant().getName();
        String streamKey = Constants.RESTAURANT_STREAM.concat(restaurant);

        Map<String,String> redisBody = Map.of(
                "id", String.valueOf(order.getId()),
                "restaurantId", String.valueOf(order.getRestaurant().getId()),
                "customerId", String.valueOf(order.getCustomer().getId()),
                "price", String.valueOf(order.getPrice())
        );

        String messageId = syncCommands.xadd(streamKey, redisBody);

        syncCommands.xgroupCreate(
                XReadArgs.StreamOffset.from(streamKey, "0-0"),
                Constants.CUSTOMER_GROUP.concat(restaurant)
        );

        return messageId;
    }

    public List<StreamResults> getRestaurantPendingOrders(String restaurant) {
        String streamKey = Constants.RESTAURANT_STREAM.concat(restaurant);
        String consumerGroup = Constants.CUSTOMER_GROUP.concat(restaurant);

        List<StreamResults> orders = new ArrayList<>();

        List<StreamMessage<String, String>> messages = syncCommands.xreadgroup(
                Consumer.from(consumerGroup, restaurant),
                XReadArgs.StreamOffset.latest(streamKey)
        );

        if(!messages.isEmpty()) {
            for (StreamMessage<String, String> message: messages) {
                StreamResults order = objectMapper.convertValue(message.getBody(), StreamResults.class);
                orders.add(order);

                syncCommands.xack(streamKey, consumerGroup, message.getId());
            }
        }
        return orders;
    }
}
