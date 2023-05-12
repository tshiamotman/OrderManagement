package za.co.ordermanagement.domain.dto;

import java.io.Serializable;

import java.math.BigDecimal;

public class OrderItemRequest implements Serializable {

    private String menuItem;

    private Long quantity;

    private String instructions;

    public OrderItemRequest(String menuItem, Long quantity, String instructions) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.instructions = instructions;
    }

    public OrderItemRequest() {
    }

    public String getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(String menuItem) {
        this.menuItem = menuItem;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
