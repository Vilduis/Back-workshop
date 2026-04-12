package prueba.com.example.demo.services;

import prueba.com.example.demo.dtos.DTOVehicles;
import prueba.com.example.demo.entities.Vehicles;

import java.util.List;

public interface VehiclesService {
    Vehicles findById(Long id);
    Vehicles insertVehicle(DTOVehicles dto);
    Vehicles updateVehicle(DTOVehicles dto);
    List<Vehicles> listAllVehicles();
    List<Vehicles> findByCustomerId(Long customerId);
    void deleteVehicle(Long id);
}

