package za.co.ordermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.ordermanagement.domain.database.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
