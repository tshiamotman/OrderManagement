package za.co.ordermanagement.domain.dto;

import za.co.ordermanagement.domain.database.Order;
import za.co.ordermanagement.domain.database.OrderItem;

import java.util.List;

public class OrderResponse {

    private Order order;

    private List<OrderItem> orderItems;

    private Double cost;

    public OrderResponse(Order order, List<OrderItem> orderItems, Double cost) {
        this.order = order;
        this.orderItems = orderItems;
        this.cost = cost;
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

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
