package prueba.com.example.demo.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prueba.com.example.demo.dtos.DTOServiceOrders;
import prueba.com.example.demo.entities.*;
import prueba.com.example.demo.exceptions.InvalidDataException;
import prueba.com.example.demo.exceptions.ResourceNotFoundException;
import prueba.com.example.demo.repositories.ServiceOrderRepository;
import prueba.com.example.demo.repositories.WorkshopRepository;
import prueba.com.example.demo.security.TenantContext;
import prueba.com.example.demo.services.CustomerService;
import prueba.com.example.demo.services.ServiceOrderService;
import prueba.com.example.demo.services.TechnicalService;
import prueba.com.example.demo.services.VehiclesService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ServiceOrderServiceImpl implements ServiceOrderService {

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private WorkshopRepository workshopRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TechnicalService technicalService;

    @Autowired
    private VehiclesService vehiclesService;

    @Override
    public ServiceOrders findById(Long id) {
        Long workshopId = TenantContext.requireWorkshopId();
        return serviceOrderRepository.findByIdAndWorkshopId(id, workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Service order with id: " + id + " not found"));
    }

    @Override
    public ServiceOrders insertServiceOrder(DTOServiceOrders dto) {
        Long workshopId = TenantContext.requireWorkshopId();
        Workshop workshop = workshopRepository.getReferenceById(workshopId);

        Vehicles vehicle    = vehiclesService.findById(dto.getVehicleId());
        Customer customer   = customerService.findById(dto.getCustomerId());
        Technical technical = technicalService.findById(dto.getTechnicalId());

        assertSameWorkshop(workshopId, vehicle.getWorkshop().getId(),    "vehículo");
        assertSameWorkshop(workshopId, customer.getWorkshop().getId(),   "cliente");
        assertSameWorkshop(workshopId, technical.getWorkshop().getId(),  "técnico");

        ServiceOrders order = new ServiceOrders();
        order.setDate(dto.getDate() != null ? dto.getDate() : LocalDateTime.now());
        order.setVehicle(vehicle);
        order.setCustomer(customer);
        order.setTechnical(technical);
        order.setDiagnosis(dto.getDiagnosis());
        order.setStatus(OrderStatus.PENDIENTE);
        order.setWorkshop(workshop);
        return serviceOrderRepository.save(order);
    }

    @Override
    public ServiceOrders updateServiceOrder(DTOServiceOrders dto) {
        ServiceOrders found = findById(dto.getId());
        Long workshopId = TenantContext.requireWorkshopId();

        Vehicles vehicle    = vehiclesService.findById(dto.getVehicleId());
        Customer customer   = customerService.findById(dto.getCustomerId());
        Technical technical = technicalService.findById(dto.getTechnicalId());

        assertSameWorkshop(workshopId, vehicle.getWorkshop().getId(),    "vehículo");
        assertSameWorkshop(workshopId, customer.getWorkshop().getId(),   "cliente");
        assertSameWorkshop(workshopId, technical.getWorkshop().getId(),  "técnico");

        found.setVehicle(vehicle);
        found.setCustomer(customer);
        found.setTechnical(technical);
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
        Long workshopId = TenantContext.requireWorkshopId();
        return serviceOrderRepository.findAllByWorkshopId(workshopId);
    }

    @Override
    public List<ServiceOrders> findByTechnicalId(Long technicalId) {
        Long workshopId = TenantContext.requireWorkshopId();
        return serviceOrderRepository.findByTechnicalIdAndWorkshopId(technicalId, workshopId);
    }

    @Override
    public List<ServiceOrders> findByStatus(OrderStatus status) {
        Long workshopId = TenantContext.requireWorkshopId();
        return serviceOrderRepository.findByStatusAndWorkshopId(status, workshopId);
    }

    @Override
    public void deleteServiceOrder(Long id) {
        ServiceOrders found = findById(id);
        serviceOrderRepository.delete(found);
    }

    private void assertSameWorkshop(Long expectedWorkshopId, Long actualWorkshopId, String entityLabel) {
        if (!Objects.equals(expectedWorkshopId, actualWorkshopId)) {
            throw new InvalidDataException("El " + entityLabel + " no pertenece a este taller");
        }
    }
}
