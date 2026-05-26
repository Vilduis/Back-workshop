package prueba.com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import prueba.com.example.demo.dtos.DTODashboardAdmin;
import prueba.com.example.demo.dtos.DTODashboardTecnico;
import prueba.com.example.demo.dtos.DTORecentOrder;
import prueba.com.example.demo.entities.OrderStatus;
import prueba.com.example.demo.entities.ServiceOrders;
import prueba.com.example.demo.entities.Technical;
import prueba.com.example.demo.repositories.CustomerRepository;
import prueba.com.example.demo.repositories.ServiceOrderRepository;
import prueba.com.example.demo.repositories.TechnicalRepository;
import prueba.com.example.demo.repositories.VehiclesRepository;
import prueba.com.example.demo.security.SecurityUser;
import prueba.com.example.demo.security.TenantContext;

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

    @Autowired
    private TechnicalRepository technicalRepository;

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DTODashboardAdmin> getAdminDashboard() {
        Long workshopId = TenantContext.requireWorkshopId();

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd   = todayStart.plusDays(1);

        LocalDate today     = LocalDate.now();
        LocalDate weekStart = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDateTime weekStartDT = weekStart.atStartOfDay();
        LocalDateTime weekEndDT   = weekStart.plusDays(7).atStartOfDay();

        long totalVehicles         = vehiclesRepository.countByWorkshopId(workshopId);
        long newVehiclesThisWeek   = vehiclesRepository.countByWorkshopIdAndCreatedAtBetween(workshopId, weekStartDT, weekEndDT);
        long totalCustomers        = customerRepository.countByWorkshopId(workshopId);
        long newCustomersThisWeek  = customerRepository.countByWorkshopIdAndCreatedAtBetween(workshopId, weekStartDT, weekEndDT);

        long ordersToday    = serviceOrderRepository.countByWorkshopIdAndDateBetween(workshopId, todayStart, todayEnd);
        long completedToday = serviceOrderRepository.countByWorkshopIdAndStatusAndDateBetween(workshopId, OrderStatus.TERMINADO, todayStart, todayEnd);

        long pendingOrders   = serviceOrderRepository.countByWorkshopIdAndStatus(workshopId, OrderStatus.PENDIENTE);
        long inProcessOrders = serviceOrderRepository.countByWorkshopIdAndStatus(workshopId, OrderStatus.EN_PROCESO);
        long completedOrders = serviceOrderRepository.countByWorkshopIdAndStatus(workshopId, OrderStatus.TERMINADO);

        List<DTORecentOrder> recentOrders = serviceOrderRepository
                .findTop5ByWorkshopIdOrderByDateDesc(workshopId)
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

    @GetMapping("/tecnico/{technicalId}")
    @PreAuthorize("hasAuthority('TECHNICAL') or hasAuthority('ADMIN')")
    public ResponseEntity<DTODashboardTecnico> getTecnicoDashboard(@PathVariable Long technicalId) {
        Long workshopId = TenantContext.requireWorkshopId();

        // Validar que el tecnico pertenece al workshop, y si quien consulta es TECHNICAL,
        // que sea su propio dashboard.
        Technical technical = technicalRepository.findByIdAndWorkshopId(technicalId, workshopId)
                .orElse(null);
        if (technical == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof SecurityUser su) {
            boolean isAdmin = su.getAuthorities().stream()
                    .anyMatch(a -> "ADMIN".equals(a.getAuthority()));
            if (!isAdmin && !su.getUserId().equals(technical.getUser().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        long totalMyOrders     = serviceOrderRepository.countByTechnicalIdAndWorkshopId(technicalId, workshopId);
        long myPendingOrders   = serviceOrderRepository.countByTechnicalIdAndStatusAndWorkshopId(technicalId, OrderStatus.PENDIENTE, workshopId);
        long myInProcessOrders = serviceOrderRepository.countByTechnicalIdAndStatusAndWorkshopId(technicalId, OrderStatus.EN_PROCESO, workshopId);
        long myCompletedOrders = serviceOrderRepository.countByTechnicalIdAndStatusAndWorkshopId(technicalId, OrderStatus.TERMINADO, workshopId);

        List<DTORecentOrder> myRecentOrders = serviceOrderRepository
                .findTop5ByTechnicalIdAndWorkshopIdOrderByDateDesc(technicalId, workshopId)
                .stream()
                .map(this::toRecentOrder)
                .collect(Collectors.toList());

        DTODashboardTecnico dto = new DTODashboardTecnico(
                totalMyOrders, myPendingOrders, myInProcessOrders, myCompletedOrders,
                myRecentOrders
        );

        return ResponseEntity.ok(dto);
    }

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
