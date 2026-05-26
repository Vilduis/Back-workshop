package prueba.com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba.com.example.demo.entities.Workshop;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {
    boolean existsByEmail(String email);
}
