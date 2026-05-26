package prueba.com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prueba.com.example.demo.entities.Vehicles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehiclesRepository extends JpaRepository<Vehicles, Long> {
    List<Vehicles> findAllByWorkshopId(Long workshopId);
    Optional<Vehicles> findByIdAndWorkshopId(Long id, Long workshopId);
    List<Vehicles> findByCustomerIdAndWorkshopId(Long customerId, Long workshopId);

    long countByWorkshopId(Long workshopId);
    long countByWorkshopIdAndCreatedAtBetween(Long workshopId, LocalDateTime start, LocalDateTime end);
}
