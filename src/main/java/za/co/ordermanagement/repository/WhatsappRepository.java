package za.co.ordermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.ordermanagement.domain.database.WhatsappModel;

import java.util.Optional;

public interface WhatsappRepository extends JpaRepository<WhatsappModel, Long> {
    Optional<WhatsappModel> findByPhoneNumberId(String phoneNumberId);
}
