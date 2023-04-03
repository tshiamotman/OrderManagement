package za.co.ordermanagement.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import za.co.ordermanagement.domain.database.MenuItem;
import za.co.ordermanagement.domain.database.Order;
import za.co.ordermanagement.domain.database.OrderItem;
import za.co.ordermanagement.domain.database.User;
import za.co.ordermanagement.domain.dto.*;
import za.co.ordermanagement.repository.OrderItemRepository;
import za.co.ordermanagement.repository.OrderRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final UserService userService;

    private final MenuService menuService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserService userService, MenuService menuService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userService = userService;
        this.menuService = menuService;
    }

    public OrderResponse createOrder(Customer customer, Restaurant restaurant, List<OrderItemRequest> items) {
        Order order = new Order();
        order.setCreatedDate(new Date());
        order.setRestaurant(restaurant);
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        AtomicReference<Double> totalCost = new AtomicReference<>(0.0);
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

            double price = menuItem.getPrice() * item.getQuantity();

            orderItem.setPrice(price);
            orderItem.setInstructions(item.getInstructions());
            orderItems.add(orderItem);

            totalCost.updateAndGet(v -> v + price);
        });
        orderItemRepository.saveAll(orderItems);

        return new OrderResponse(savedOrder, orderItems, totalCost.get());
    }

    public Order updateStatus(Long id, OrderStatus status) throws SQLException {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();

        User restaurant = userService.getUser(user);

        Optional<Order> orderOptional = orderRepository.findById(id);

        if(orderOptional.isEmpty()) {
            throw new SQLException(String.format("Order with ID: %d, does not exist.", id));
        }

        Order order = orderOptional.get();

        if(!restaurant.getRole().equals(Role.RESTAURANT) || !restaurant.getId().equals(order.getRestaurant().getId())) {
            throw new BadCredentialsException("Use correct restaurant credentials to update order.");
        }

        order.setStatus(status);

        return orderRepository.save(order);
    }
}
