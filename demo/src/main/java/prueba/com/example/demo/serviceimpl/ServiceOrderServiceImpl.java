package prueba.com.example.demo.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prueba.com.example.demo.dtos.DTOServiceOrders;
import prueba.com.example.demo.entities.OrderStatus;
import prueba.com.example.demo.entities.ServiceOrders;
import prueba.com.example.demo.exceptions.ResourceNotFoundException;
import prueba.com.example.demo.repositories.ServiceOrderRepository;
import prueba.com.example.demo.services.CustomerService;
import prueba.com.example.demo.services.ServiceOrderService;
import prueba.com.example.demo.services.TechnicalService;
import prueba.com.example.demo.services.VehiclesService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceOrderServiceImpl implements ServiceOrderService {

    @Autowired
    ServiceOrderRepository serviceOrderRepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    TechnicalService technicalService;

    @Autowired
    VehiclesService vehiclesService;

    @Override
    public ServiceOrders findById(Long id) {
        return serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service order with id: " + id + " not found"));
    }

    @Override
    public ServiceOrders insertServiceOrder(DTOServiceOrders dto) {
        ServiceOrders order = new ServiceOrders();
        order.setDate(dto.getDate() != null ? dto.getDate() : LocalDateTime.now());
        order.setVehicle(vehiclesService.findById(dto.getVehicleId()));
        order.setCustomer(customerService.findById(dto.getCustomerId()));
        order.setTechnical(technicalService.findById(dto.getTechnicalId()));
        order.setDiagnosis(dto.getDiagnosis());
        order.setStatus(OrderStatus.PENDIENTE);  // siempre inicia en PENDIENTE
        return serviceOrderRepository.save(order);
    }

    @Override
    public ServiceOrders updateServiceOrder(DTOServiceOrders dto) {
        ServiceOrders found = findById(dto.getId());
        found.setVehicle(vehiclesService.findById(dto.getVehicleId()));
        found.setCustomer(customerService.findById(dto.getCustomerId()));
        found.setTechnical(technicalService.findById(dto.getTechnicalId()));
        found.setDiagnosis(dto.getDiagnosis());
        if (dto.getStatus() != null) {
            found.setStatus(dto.getStatus());
        }
        return serviceOrderRepository.save(found);
    }

    @Override
    public ServiceOrders updateStatus(Long id, OrderStatus status) {
        ServiceOrders found = findById(id);
        found.setStatus(status);
        return serviceOrderRepository.save(found);
    }

    @Override
    public List<ServiceOrders> listAllServiceOrders() {
        return serviceOrderRepository.findAll();
    }

    @Override
    public List<ServiceOrders> findByTechnicalId(Long technicalId) {
        return serviceOrderRepository.findByTechnicalId(technicalId);
    }

    @Override
    public List<ServiceOrders> findByStatus(OrderStatus status) {
        return serviceOrderRepository.findByStatus(status);
    }

    @Override
    public void deleteServiceOrder(Long id) {
        ServiceOrders found = findById(id);
        serviceOrderRepository.delete(found);
    }
}

