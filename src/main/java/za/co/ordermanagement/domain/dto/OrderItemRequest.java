package za.co.ordermanagement.domain.dto;

public class OrderItemRequest {

    private String menuItem;

    private Long quantity;

    private String Instructions;

    public OrderItemRequest(String menuItem, Long quantity, String instructions) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        Instructions = instructions;
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
        return Instructions;
    }

    public void setInstructions(String instructions) {
        Instructions = instructions;
    }
}
