package za.co.ordermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.ordermanagement.domain.database.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
