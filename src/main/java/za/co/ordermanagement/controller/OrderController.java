package za.co.ordermanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import za.co.ordermanagement.domain.dto.OrderRequest;
import za.co.ordermanagement.domain.dto.OrderResponse;
import za.co.ordermanagement.domain.dto.OrderStatus;
import za.co.ordermanagement.service.OrderService;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping( name = "/getPendingOrders",
            method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getPendingOrders() {
        try {
            List<OrderResponse> orders = orderService.getRestaurantPendingOrders();
            if (!orders.isEmpty()) {
                return ResponseEntity.ok(orders);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @RequestMapping( name = "/createOrder",
            method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createNewOrder(@RequestBody OrderRequest orderRequest) {
        try {
            return ResponseEntity.ok(orderService.createOrder(
                    orderRequest.getCustomer(),
                    orderRequest.getRestaurant(),
                    orderRequest.getItems()
            ));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @RequestMapping( name = "/updateOrderStatus",
            method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateOrderStatus(@RequestParam("id") Long id, @RequestParam("status") String status) {
        try {
            return ResponseEntity.ok(orderService.updateStatus(id, OrderStatus.valueOf(status)));
        } catch (IllegalArgumentException | SQLException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }

    @RequestMapping( name = "/getOrder/{id}",
            method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> getOrderById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(orderService.getOrderById(id));
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }
}
