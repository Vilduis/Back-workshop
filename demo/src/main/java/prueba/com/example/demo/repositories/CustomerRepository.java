package prueba.com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prueba.com.example.demo.entities.Customer;

import java.time.LocalDateTime;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);

    // Dashboard: clientes registrados en un rango de fechas
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}

