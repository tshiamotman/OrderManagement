package za.co.ordermanagement.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import za.co.ordermanagement.domain.database.MenuItem;
import za.co.ordermanagement.domain.database.Order;
import za.co.ordermanagement.domain.database.OrderItem;
import za.co.ordermanagement.domain.database.User;
import za.co.ordermanagement.domain.dto.*;
import za.co.ordermanagement.repository.OrderItemRepository;
import za.co.ordermanagement.repository.OrderRepository;
import za.co.ordermanagement.utils.PropertyProvider;

import java.sql.SQLException;
import java.util.*;

import java.math.BigDecimal;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final UserService userService;

    private final MenuService menuService;

    private final RabbitMqService rabbitMqService;

    private final RestTemplate restTemplate;

    private final PropertyProvider propertyProvider;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserService userService, MenuService menuService, RabbitMqService rabbitMqService, RestTemplate restTemplate, PropertyProvider propertyProvider) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userService = userService;
        this.menuService = menuService;
        this.rabbitMqService = rabbitMqService;
        this.restTemplate = restTemplate;
        this.propertyProvider = propertyProvider;
    }

    public OrderResponse createOrder(Customer customer, Restaurant restaurant, List<OrderItemRequest> items) {
        Order order = new Order();
        order.setCreatedDate(new Date());
        order.setRestaurant(restaurant);
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING.name());

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        var ref = new Object() {
            BigDecimal totalCost = new BigDecimal("0.0");
        };
        items.forEach(item -> {
            MenuItem menuItem;
            try {
                menuItem = menuService.getMenuItemByName(item.getMenuItem());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(item.getQuantity());

            BigDecimal price = menuItem.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            orderItem.setPrice(price);
            orderItem.setInstructions(item.getInstructions());
            orderItems.add(orderItem);

            ref.totalCost = ref.totalCost.add(price);
        });

        orderItemRepository.saveAll(orderItems);
        savedOrder.setPrice(ref.totalCost);
        rabbitMqService.addOrderToQueue(savedOrder);
        return new OrderResponse(orderRepository.save(savedOrder), orderItems);
    }

    public Order updateStatus(Long id, OrderStatus status) throws SQLException {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();

        User restaurant = userService.getUser(user);

        Optional<Order> orderOptional = orderRepository.findById(id);

        if(orderOptional.isEmpty()) {
            throw new SQLException(String.format("Order with ID: %d, does not exist.", id));
        }

        Order order = orderOptional.get();

        if(!restaurant.getRole().equals(Role.RESTAURANT.name()) || !restaurant.getId().equals(order.getRestaurant().getId())) {
            throw new BadCredentialsException("Use correct restaurant credentials to update order.");
        }

        order.setStatus(status.name());

        HttpEntity<Order> request = new HttpEntity<>(order);

        try {
            restTemplate.exchange(propertyProvider.getMessagingServiceUrl().concat("/orderUpdate"), HttpMethod.POST, request, List.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return orderRepository.save(order);
    }

    public List<OrderResponse> getRestaurantPendingOrders() {
        String restaurantName = SecurityContextHolder.getContext().getAuthentication().getName();

        User restaurant = userService.getUser(restaurantName);

        if(!restaurant.getRole().equals(Role.RESTAURANT.name())) {
            throw new BadCredentialsException("Use restaurant credentials to get pending orders.");
        }

        List<StreamResults> streamResults = rabbitMqService.getRestaurantPendingOrders(restaurantName);

        List<OrderResponse> orders = new ArrayList<>();

        for(StreamResults orderFromStream: streamResults) {
            Optional<Order> orderOptional = orderRepository.findById(orderFromStream.getId());
            orderOptional.ifPresent(order -> orders.add(new OrderResponse(
                    order,
                    orderItemRepository.findByOrderId(orderFromStream.getId())
            )));
        }
        return orders;
    }

    public OrderResponse getOrderById(Long id) throws SQLException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userService.getUser(username);

        Optional<Order> orderOptional = orderRepository.findById(id);

        if(orderOptional.isEmpty())
            throw new SQLException(String.format("Order with ID: %d, does not exist.", id));

        Order order = orderOptional.get();

        if(!order.getRestaurant().getId().equals(user.getId()) || !order.getCustomer().getId().equals(user.getId()))
            throw new BadCredentialsException("Credentials not authorized to get order details.");

        return new OrderResponse(order, orderItemRepository.findByOrderId(id));
    }
}
