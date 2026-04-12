package prueba.com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prueba.com.example.demo.entities.Technical;

@Repository
public interface TechnicalRepository extends JpaRepository<Technical, Long> {
    Technical findByUserId(Long userId);
}

