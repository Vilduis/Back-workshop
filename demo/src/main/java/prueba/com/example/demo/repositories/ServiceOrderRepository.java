package prueba.com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prueba.com.example.demo.entities.OrderStatus;
import prueba.com.example.demo.entities.ServiceOrders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrders, Long> {

    // --- Multi-tenant filtered ---
    List<ServiceOrders> findAllByWorkshopId(Long workshopId);
    Optional<ServiceOrders> findByIdAndWorkshopId(Long id, Long workshopId);
    List<ServiceOrders> findByTechnicalIdAndWorkshopId(Long technicalId, Long workshopId);
    List<ServiceOrders> findByStatusAndWorkshopId(OrderStatus status, Long workshopId);

    // --- Dashboard Admin (filtrado por workshop) ---
    long countByWorkshopIdAndStatus(Long workshopId, OrderStatus status);
    long countByWorkshopIdAndDateBetween(Long workshopId, LocalDateTime start, LocalDateTime end);
    long countByWorkshopIdAndStatusAndDateBetween(Long workshopId, OrderStatus status, LocalDateTime start, LocalDateTime end);
    List<ServiceOrders> findTop5ByWorkshopIdOrderByDateDesc(Long workshopId);

    // --- Dashboard Técnico (filtrado por workshop + tecnico) ---
    long countByTechnicalIdAndWorkshopId(Long technicalId, Long workshopId);
    long countByTechnicalIdAndStatusAndWorkshopId(Long technicalId, OrderStatus status, Long workshopId);
    List<ServiceOrders> findTop5ByTechnicalIdAndWorkshopIdOrderByDateDesc(Long technicalId, Long workshopId);
}
