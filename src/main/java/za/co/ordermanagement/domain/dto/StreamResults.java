package za.co.ordermanagement.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StreamResults {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("restaurantId")
    private Long restaurantId;

    @JsonProperty("customerId")
    private Long customerId;

    @JsonProperty("price")
    private Double price;

    public StreamResults() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
