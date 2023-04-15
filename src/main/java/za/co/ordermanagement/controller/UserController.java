package za.co.ordermanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import za.co.ordermanagement.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping( path = "/getRestaurantByPhoneId/{id}",
            method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getRestaurantByPhoneId(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(userService.getRestaurantByPhoneNumberId(id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @RequestMapping( path = "/getCustomerByPhoneNumber/{id}",
            method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getCustomerByPhoneId(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(userService.getUserByPhoneNumber(id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @RequestMapping( path = "/ping",
            method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().build();
    }
}
