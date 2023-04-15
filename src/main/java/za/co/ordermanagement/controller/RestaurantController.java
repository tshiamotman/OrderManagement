package za.co.ordermanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import za.co.ordermanagement.domain.database.MenuItem;
import za.co.ordermanagement.service.MenuService;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private final MenuService menuService;

    public RestaurantController(MenuService menuService) {
        this.menuService = menuService;
    }

    @RequestMapping( path = "/createMenuItem",
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

    @RequestMapping( path = "/createMenuItems",
            method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createNewMenuItems(@RequestBody List<MenuItem> items) {
        try {
            return ResponseEntity.ok(menuService.createMultipleMenuItems(items));
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }

    @RequestMapping( path = "/updateMenuItem/{id}",
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

    @RequestMapping( path = "/deleteMenuItem/{id}",
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

    @RequestMapping( path = "/getRestaurantMenu/{name}",
            method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getRestaurantMenu(@PathVariable("name") String name) {
        try {
            return ResponseEntity.ok(menuService.getRestaurantMenuItems(name));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @RequestMapping( path = "/getMenuItemByName/{name}",
            method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getMenuItemByName(@PathVariable("name") String name) {
        try {
            return ResponseEntity.ok(menuService.getMenuItemByName(name));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @RequestMapping( path = "/getMenuItemById/{id}",
            method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getMenuItemById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(menuService.getMenuItem(id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }
}
