package za.co.ordermanagement.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import za.co.ordermanagement.domain.database.MenuItem;
import za.co.ordermanagement.domain.database.User;
import za.co.ordermanagement.domain.dto.Role;
import za.co.ordermanagement.repository.MenuItemRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {
    private final MenuItemRepository menuItemRepository;

    private final UserService userService;


    public MenuService(MenuItemRepository menuItemRepository, UserService userService) {
        this.menuItemRepository = menuItemRepository;
        this.userService = userService;
    }

    public MenuItem getMenuItem(Long id) throws SQLException {
        Optional<MenuItem> menuItemOptional = menuItemRepository.findById(id);

        if(menuItemOptional.isEmpty()) {
            throw new SQLException(String.format("Menu Item with ID: %d, does not exist.", id));
        }

        return menuItemOptional.get();
    }

    public MenuItem getMenuItemByName(String name) throws SQLException {
        Optional<MenuItem> menuItemOptional = menuItemRepository.findByName(name);

        if(menuItemOptional.isEmpty()) {
            throw new SQLException(String.format("Menu Item with name: %s, does not exist.", name));
        }

        return menuItemOptional.get();
    }

    public List<MenuItem> getRestaurantMenuItems(String restaurantName) {
        User restaurant = userService.getUser(restaurantName);

        if(!restaurant.getRole().equals(Role.RESTAURANT.name())) {
            throw new BadCredentialsException("Provide existing restaurant name");
        }

        return menuItemRepository.findByRestaurantId(restaurant.getId());
    }

    public MenuItem createMenuItem(MenuItem menuItem) throws Exception {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();

        User restaurant = userService.getUser(user);

        if(!restaurant.getRole().equals(Role.RESTAURANT.name())) {
            throw new BadCredentialsException("Use restaurant credentials to add new menu item.");
        }

        if(menuItemRepository.existsByName(menuItem.getName())) {
            throw new SQLException(String.format("Menu item with name: %s, already exist.", menuItem.getName()));
        }

        menuItem.setRestaurant(restaurant);

        return menuItemRepository.save(menuItem);
    }

    public MenuItem updateMenuItem(Long id, MenuItem menuItem) throws Exception {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();

        User restaurant = userService.getUser(user);

        Optional<MenuItem> menuItemOptional = menuItemRepository.findById(id);

        if(menuItemOptional.isEmpty()) {
            throw new SQLException(String.format("Menu Item with ID: %d, does not exist.", id));
        }
        MenuItem dbMenuItem = menuItemOptional.get();

        if(!restaurant.getRole().equals(Role.RESTAURANT) || !restaurant.getId().equals(dbMenuItem.getRestaurant().getId())) {
            throw new BadCredentialsException("Use correct restaurant credentials to update menu item.");
        }

        if(!menuItem.getName().equals(dbMenuItem.getName()) && menuItemRepository.existsByName(menuItem.getName())) {
            throw new SQLException(String.format("Menu item with name: %s, already exist.", menuItem.getName()));
        }

        menuItem.setId(id);
        menuItem.setRestaurant(restaurant);

        return menuItemRepository.save(menuItem);
    }

    public boolean deleteMenuItem(Long id) throws Exception {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();

        User restaurant = userService.getUser(user);

        Optional<MenuItem> menuItemOptional = menuItemRepository.findById(id);

        if(menuItemOptional.isEmpty()) {
            throw new SQLException(String.format("Menu Item with ID: %d, does not exist.", id));
        }
        MenuItem dbMenuItem = menuItemOptional.get();

        if(!restaurant.getId().equals(dbMenuItem.getRestaurant().getId())) {
            throw new BadCredentialsException("Use correct restaurant credentials to delete menu item.");
        }

        menuItemRepository.delete(dbMenuItem);
        return true;
    }

    public List<MenuItem> createMultipleMenuItems(List<MenuItem> items) {
        List<MenuItem> menuItems = new ArrayList<>();

        for(MenuItem item:items) {
            try {
                menuItems.add(createMenuItem(item));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return menuItems;
    }
}
