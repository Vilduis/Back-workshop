package prueba.com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prueba.com.example.demo.dtos.DTOServiceOrders;
import prueba.com.example.demo.entities.OrderStatus;
import prueba.com.example.demo.entities.ServiceOrders;
import prueba.com.example.demo.services.ServiceOrderService;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ServiceOrderController {

    @Autowired
    ServiceOrderService serviceOrderService;

    // ADMIN: listar todas las órdenes
    @GetMapping("/service-orders")
    public ResponseEntity<List<ServiceOrders>> getAllServiceOrders() {
        return ResponseEntity.ok(serviceOrderService.listAllServiceOrders());
    }

    // ADMIN / TÉCNICO: obtener una orden por id
    @GetMapping("/service-orders/{id}")
    public ResponseEntity<ServiceOrders> getServiceOrder(@PathVariable Long id) {
        return ResponseEntity.ok(serviceOrderService.findById(id));
    }

    // TÉCNICO: ver sus propias órdenes asignadas
    @GetMapping("/service-orders/technical/{technicalId}")
    public ResponseEntity<List<ServiceOrders>> getServiceOrdersByTechnical(@PathVariable Long technicalId) {
        return ResponseEntity.ok(serviceOrderService.findByTechnicalId(technicalId));
    }

    // ADMIN: filtrar órdenes por estado
    @GetMapping("/service-orders/status/{status}")
    public ResponseEntity<List<ServiceOrders>> getServiceOrdersByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(serviceOrderService.findByStatus(status));
    }

    // ADMIN: crear nueva orden (inicia en PENDIENTE)
    @PostMapping("/service-orders")
    public ResponseEntity<ServiceOrders> createServiceOrder(@RequestBody DTOServiceOrders dto) {
        return new ResponseEntity<>(serviceOrderService.insertServiceOrder(dto), HttpStatus.CREATED);
    }

    // ADMIN: editar orden completa
    @PutMapping("/service-orders/{id}")
    public ResponseEntity<ServiceOrders> updateServiceOrder(@PathVariable Long id, @RequestBody DTOServiceOrders dto) {
        dto.setId(id);
        return ResponseEntity.ok(serviceOrderService.updateServiceOrder(dto));
    }

    // TÉCNICO: actualizar únicamente el estado de su orden
    @PatchMapping("/service-orders/{id}/status")
    public ResponseEntity<ServiceOrders> updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(serviceOrderService.updateStatus(id, status));
    }

    // ADMIN: eliminar orden
    @DeleteMapping("/service-orders/{id}")
    public ResponseEntity<Void> deleteServiceOrder(@PathVariable Long id) {
        serviceOrderService.deleteServiceOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

