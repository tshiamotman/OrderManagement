package za.co.ordermanagement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import org.springframework.stereotype.Service;
import za.co.ordermanagement.domain.database.Order;
import za.co.ordermanagement.domain.dto.StreamResults;
import za.co.ordermanagement.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitMqService {
    private final Connection connection;

    private final ObjectMapper objectMapper;

    public RabbitMqService(ObjectMapper objectMapper, ConnectionFactory factory) {
        this.objectMapper = objectMapper;
        try {
            this.connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addOrderToQueue(Order order) {
        String restaurant = order.getRestaurant().getName();
        String streamKey = Constants.RESTAURANT_STREAM.concat(restaurant);

        StreamResults streamResults = new StreamResults();
        streamResults.setId(order.getId());
        streamResults.setRestaurantId(order.getRestaurant().getId());
        streamResults.setCustomerId(order.getCustomer().getId());
        streamResults.setPrice(order.getPrice());

        try {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare("restaurant", "direct");

            channel.queueDeclare(streamKey, false, false, false, null);
            channel.queueBind(streamKey, "restaurant", streamKey);

            channel.basicPublish("restaurant", streamKey, null, objectMapper.writeValueAsBytes(streamResults));

            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StreamResults> getRestaurantPendingOrders(String restaurant) {
        String streamKey = Constants.RESTAURANT_STREAM.concat(restaurant);

        List<StreamResults> orders = new ArrayList<>();

        Channel channel = null;
        try {
            channel = connection.createChannel();
            channel.queueDeclare(streamKey, false, false, false, null);

            long orderCount = channel.messageCount(streamKey);

            for (long i = 0; i < orderCount; i++) {
                GetResponse response = channel.basicGet(streamKey, true);
                StreamResults order = objectMapper.readValue(response.getBody(), StreamResults.class);
                orders.add(order);
            }
            channel.queueDelete(streamKey);
            channel.close();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

        return orders;
    }
}
