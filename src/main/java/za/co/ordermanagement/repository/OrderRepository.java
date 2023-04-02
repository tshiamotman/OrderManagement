package za.co.ordermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.ordermanagement.domain.database.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
