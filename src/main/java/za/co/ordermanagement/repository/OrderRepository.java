package za.co.ordermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.ordermanagement.domain.database.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
