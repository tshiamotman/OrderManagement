package za.co.ordermanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import za.co.ordermanagement.domain.database.MenuItem;
import za.co.ordermanagement.service.MenuService;

import java.sql.SQLException;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private final MenuService menuService;

    public RestaurantController(MenuService menuService) {
        this.menuService = menuService;
    }

    @RequestMapping( name = "/createMenuItem",
            method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createNewMenuItem(@RequestBody MenuItem menuItem) {
        try {
            return ResponseEntity.ok(menuService.createMenuItem(menuItem));
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }

    @RequestMapping( name = "/updateMenuItem/{id}",
            method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateMenuItem(@PathVariable("id") Long id, @RequestBody MenuItem menuItem) {
        try {
            return ResponseEntity.ok(menuService.updateMenuItem(id, menuItem));
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }

    @RequestMapping( name = "/deleteMenuItem/{id}",
            method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteMenuItem(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(menuService.deleteMenuItem(id));
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }

    @RequestMapping( name = "/getRestaurantMenu/{name}",
            method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getRestaurantMenu(@PathVariable("name") String name) {
        try {
            return ResponseEntity.ok(menuService.getRestaurantMenuItems(name));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @RequestMapping( name = "/getMenuItemByName/{name}",
            method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getMenuItemByName(@PathVariable("name") String name) {
        try {
            return ResponseEntity.ok(menuService.getMenuItemByName(name));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @RequestMapping( name = "/getMenuItemById/{id}",
            method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getMenuItemById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(menuService.getMenuItem(id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }
}
