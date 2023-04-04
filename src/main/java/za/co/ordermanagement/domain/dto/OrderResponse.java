package za.co.ordermanagement.domain.dto;

import za.co.ordermanagement.domain.database.Order;
import za.co.ordermanagement.domain.database.OrderItem;

import java.util.List;

public class OrderResponse {

    private Order order;

    private List<OrderItem> orderItems;


    public OrderResponse(Order order, List<OrderItem> orderItems) {
        this.order = order;
        this.orderItems = orderItems;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
