package prueba.com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prueba.com.example.demo.dtos.DTOVehicles;
import prueba.com.example.demo.entities.Vehicles;
import prueba.com.example.demo.services.VehiclesService;

import java.util.List;

@RestController
@RequestMapping("")
@CrossOrigin("*")
public class VehiclesController {

    @Autowired
    VehiclesService vehiclesService;

    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicles>> getAllVehicles() {
        return ResponseEntity.ok(vehiclesService.listAllVehicles());
    }

    @GetMapping("/vehicles/{id}")
    public ResponseEntity<Vehicles> getVehicle(@PathVariable Long id) {
        return ResponseEntity.ok(vehiclesService.findById(id));
    }

    @GetMapping("/vehicles/customer/{customerId}")
    public ResponseEntity<List<Vehicles>> getVehiclesByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(vehiclesService.findByCustomerId(customerId));
    }

    @PostMapping("/vehicles")
    public ResponseEntity<Vehicles> createVehicle(@RequestBody DTOVehicles dto) {
        return new ResponseEntity<>(vehiclesService.insertVehicle(dto), HttpStatus.CREATED);
    }

    @PutMapping("/vehicles/{id}")
    public ResponseEntity<Vehicles> updateVehicle(@PathVariable Long id, @RequestBody DTOVehicles dto) {
        dto.setId(id);
        return ResponseEntity.ok(vehiclesService.updateVehicle(dto));
    }

    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehiclesService.deleteVehicle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

