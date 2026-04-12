package prueba.com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prueba.com.example.demo.entities.Vehicles;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VehiclesRepository extends JpaRepository<Vehicles, Long> {
    List<Vehicles> findByCustomerId(Long customerId);
    Vehicles findByPlate(String plate);

    // Dashboard: vehículos registrados en un rango de fechas
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}

