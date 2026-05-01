package prueba.com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prueba.com.example.demo.dtos.DTODashboardAdmin;
import prueba.com.example.demo.dtos.DTODashboardTecnico;
import prueba.com.example.demo.dtos.DTORecentOrder;
import prueba.com.example.demo.entities.OrderStatus;
import prueba.com.example.demo.entities.ServiceOrders;
import prueba.com.example.demo.repositories.CustomerRepository;
import prueba.com.example.demo.repositories.ServiceOrderRepository;
import prueba.com.example.demo.repositories.VehiclesRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin("*")
public class DashboardController {

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private VehiclesRepository vehiclesRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // ─────────────────────────────────────────────
    //  GET /api/dashboard/admin
    //  Devuelve estadísticas globales del taller
    // ─────────────────────────────────────────────
    @GetMapping("/admin")
    public ResponseEntity<DTODashboardAdmin> getAdminDashboard() {

        // Rango: hoy
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd   = todayStart.plusDays(1);

        // Rango: esta semana (lunes a domingo)
        LocalDate today      = LocalDate.now();
        LocalDate weekStart  = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDateTime weekStartDT = weekStart.atStartOfDay();
        LocalDateTime weekEndDT   = weekStart.plusDays(7).atStartOfDay();

        // Totales
        long totalVehicles         = vehiclesRepository.count();
        long newVehiclesThisWeek   = vehiclesRepository.countByCreatedAtBetween(weekStartDT, weekEndDT);
        long totalCustomers        = customerRepository.count();
        long newCustomersThisWeek  = customerRepository.countByCreatedAtBetween(weekStartDT, weekEndDT);

        // Órdenes de hoy
        long ordersToday    = serviceOrderRepository.countByDateBetween(todayStart, todayEnd);
        long completedToday = serviceOrderRepository.countByStatusAndDateBetween(OrderStatus.TERMINADO, todayStart, todayEnd);

        // Resumen por estado (total histórico)
        long pendingOrders   = serviceOrderRepository.countByStatus(OrderStatus.PENDIENTE);
        long inProcessOrders = serviceOrderRepository.countByStatus(OrderStatus.EN_PROCESO);
        long completedOrders = serviceOrderRepository.countByStatus(OrderStatus.TERMINADO);

        // Actividad reciente: últimas 5 órdenes
        List<DTORecentOrder> recentOrders = serviceOrderRepository
                .findTop5ByOrderByDateDesc()
                .stream()
                .map(this::toRecentOrder)
                .collect(Collectors.toList());

        DTODashboardAdmin dto = new DTODashboardAdmin(
                totalVehicles, newVehiclesThisWeek,
                totalCustomers, newCustomersThisWeek,
                ordersToday, completedToday,
                pendingOrders, inProcessOrders, completedOrders,
                recentOrders
        );

        return ResponseEntity.ok(dto);
    }

    // ─────────────────────────────────────────────
    //  GET /api/dashboard/tecnico/{technicalId}
    //  Devuelve estadísticas personales del técnico
    // ─────────────────────────────────────────────
    @GetMapping("/tecnico/{technicalId}")
    public ResponseEntity<DTODashboardTecnico> getTecnicoDashboard(@PathVariable Long technicalId) {

        long totalMyOrders      = serviceOrderRepository.countByTechnicalId(technicalId);
        long myPendingOrders    = serviceOrderRepository.countByTechnicalIdAndStatus(technicalId, OrderStatus.PENDIENTE);
        long myInProcessOrders  = serviceOrderRepository.countByTechnicalIdAndStatus(technicalId, OrderStatus.EN_PROCESO);
        long myCompletedOrders  = serviceOrderRepository.countByTechnicalIdAndStatus(technicalId, OrderStatus.TERMINADO);

        // Mis últimas 5 órdenes
        List<DTORecentOrder> myRecentOrders = serviceOrderRepository
                .findTop5ByTechnicalIdOrderByDateDesc(technicalId)
                .stream()
                .map(this::toRecentOrder)
                .collect(Collectors.toList());

        DTODashboardTecnico dto = new DTODashboardTecnico(
                totalMyOrders,
                myPendingOrders,
                myInProcessOrders,
                myCompletedOrders,
                myRecentOrders
        );

        return ResponseEntity.ok(dto);
    }

    // ─────────────────────────────────────────────
    //  Mapper: ServiceOrders → DTORecentOrder
    // ─────────────────────────────────────────────
    private DTORecentOrder toRecentOrder(ServiceOrders order) {
        DTORecentOrder dto = new DTORecentOrder();
        dto.setOrderId(order.getId());
        dto.setDate(order.getDate());
        dto.setDiagnosis(order.getDiagnosis());
        dto.setStatus(order.getStatus() != null ? order.getStatus().name() : "");

        if (order.getVehicle() != null) {
            dto.setVehiclePlate(order.getVehicle().getPlate());
            dto.setVehicleBrand(order.getVehicle().getBrand());
            dto.setVehicleModel(order.getVehicle().getModel());
        }

        if (order.getCustomer() != null) {
            dto.setCustomerName(order.getCustomer().getName() + " " + order.getCustomer().getLastName());
        }

        if (order.getTechnical() != null) {
            dto.setTechnicalName(order.getTechnical().getName() + " " + order.getTechnical().getLastName());
        }

        return dto;
    }
}

