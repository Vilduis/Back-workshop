package prueba.com.example.demo.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prueba.com.example.demo.dtos.DTOVehicles;
import prueba.com.example.demo.entities.Vehicles;
import prueba.com.example.demo.exceptions.ResourceNotFoundException;
import prueba.com.example.demo.repositories.VehiclesRepository;
import prueba.com.example.demo.services.CustomerService;
import prueba.com.example.demo.services.VehiclesService;

import java.util.List;

@Service
public class VehiclesServiceImpl implements VehiclesService {

    @Autowired
    VehiclesRepository vehiclesRepository;

    @Autowired
    CustomerService customerService;

    @Override
    public Vehicles findById(Long id) {
        return vehiclesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle with id: " + id + " not found"));
    }

    @Override
    public Vehicles insertVehicle(DTOVehicles dto) {
        Vehicles vehicle = new Vehicles();
        vehicle.setPlate(dto.getPlate());
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setYear(dto.getYear());
        vehicle.setCustomer(customerService.findById(dto.getCustomerId()));
        return vehiclesRepository.save(vehicle);
    }

    @Override
    public Vehicles updateVehicle(DTOVehicles dto) {
        Vehicles found = findById(dto.getId());
        found.setPlate(dto.getPlate());
        found.setBrand(dto.getBrand());
        found.setModel(dto.getModel());
        found.setYear(dto.getYear());
        if (dto.getCustomerId() != null) {
            found.setCustomer(customerService.findById(dto.getCustomerId()));
        }
        return vehiclesRepository.save(found);
    }

    @Override
    public List<Vehicles> listAllVehicles() {
        return vehiclesRepository.findAll();
    }

    @Override
    public List<Vehicles> findByCustomerId(Long customerId) {
        return vehiclesRepository.findByCustomerId(customerId);
    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicles found = findById(id);
        vehiclesRepository.delete(found);
    }
}

