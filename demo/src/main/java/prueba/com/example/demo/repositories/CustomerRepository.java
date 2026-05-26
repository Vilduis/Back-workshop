package prueba.com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prueba.com.example.demo.entities.Customer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByWorkshopId(Long workshopId);
    Optional<Customer> findByIdAndWorkshopId(Long id, Long workshopId);

    long countByWorkshopId(Long workshopId);
    long countByWorkshopIdAndCreatedAtBetween(Long workshopId, LocalDateTime start, LocalDateTime end);
}
