package za.co.ordermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.ordermanagement.domain.database.MenuItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    boolean existsByName(String name);

    List<MenuItem> findByRestaurantId(Long id);

    Optional<MenuItem> findByName(String name);
}
