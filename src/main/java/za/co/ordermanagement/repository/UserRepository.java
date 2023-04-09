package za.co.ordermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.ordermanagement.domain.database.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    boolean existsByName(String name);

    boolean existsByEmailAddress(String emailAddress);

    Optional<User> findByPhoneNumber(String phoneNumber);
}
