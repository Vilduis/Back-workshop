package prueba.com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import prueba.com.example.demo.dtos.DTOServiceOrders;
import prueba.com.example.demo.entities.OrderStatus;
import prueba.com.example.demo.entities.ServiceOrders;
import prueba.com.example.demo.services.ServiceOrderService;

import java.util.List;

@RestController
@RequestMapping("")
@CrossOrigin("*")
public class ServiceOrderController {

    @Autowired
    ServiceOrderService serviceOrderService;

    @GetMapping("/service-orders")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICAL')")
    public ResponseEntity<List<ServiceOrders>> getAllServiceOrders() {
        return ResponseEntity.ok(serviceOrderService.listAllServiceOrders());
    }

    @GetMapping("/service-orders/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICAL')")
    public ResponseEntity<ServiceOrders> getServiceOrder(@PathVariable Long id) {
        return ResponseEntity.ok(serviceOrderService.findById(id));
    }

    @GetMapping("/service-orders/technical/{technicalId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICAL')")
    public ResponseEntity<List<ServiceOrders>> getServiceOrdersByTechnical(@PathVariable Long technicalId) {
        return ResponseEntity.ok(serviceOrderService.findByTechnicalId(technicalId));
    }

    @GetMapping("/service-orders/status/{status}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ServiceOrders>> getServiceOrdersByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(serviceOrderService.findByStatus(status));
    }

    @PostMapping("/service-orders")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICAL')")
    public ResponseEntity<ServiceOrders> createServiceOrder(@RequestBody DTOServiceOrders dto) {
        return new ResponseEntity<>(serviceOrderService.insertServiceOrder(dto), HttpStatus.CREATED);
    }

    @PutMapping("/service-orders/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ServiceOrders> updateServiceOrder(@PathVariable Long id, @RequestBody DTOServiceOrders dto) {
        dto.setId(id);
        return ResponseEntity.ok(serviceOrderService.updateServiceOrder(dto));
    }

    @PatchMapping("/service-orders/{id}/status")
    @PreAuthorize("hasAuthority('TECHNICAL') or hasAuthority('ADMIN')")
    public ResponseEntity<ServiceOrders> updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(serviceOrderService.updateStatus(id, status));
    }

    @DeleteMapping("/service-orders/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteServiceOrder(@PathVariable Long id) {
        serviceOrderService.deleteServiceOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
