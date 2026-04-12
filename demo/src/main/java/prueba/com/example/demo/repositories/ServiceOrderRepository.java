package prueba.com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prueba.com.example.demo.entities.OrderStatus;
import prueba.com.example.demo.entities.ServiceOrders;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrders, Long> {
    List<ServiceOrders> findByCustomerId(Long customerId);
    List<ServiceOrders> findByTechnicalId(Long technicalId);
    List<ServiceOrders> findByVehicleId(Long vehicleId);
    List<ServiceOrders> findByStatus(OrderStatus status);

    // --- Dashboard Admin ---
    long countByStatus(OrderStatus status);
    long countByDateBetween(LocalDateTime start, LocalDateTime end);
    long countByStatusAndDateBetween(OrderStatus status, LocalDateTime start, LocalDateTime end);
    List<ServiceOrders> findTop5ByOrderByDateDesc();

    // --- Dashboard Técnico ---
    long countByTechnicalId(Long technicalId);
    long countByTechnicalIdAndStatus(Long technicalId, OrderStatus status);
    List<ServiceOrders> findTop5ByTechnicalIdOrderByDateDesc(Long technicalId);
}

