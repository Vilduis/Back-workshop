package prueba.com.example.demo.services;

import prueba.com.example.demo.dtos.DTOServiceOrders;
import prueba.com.example.demo.entities.OrderStatus;
import prueba.com.example.demo.entities.ServiceOrders;

import java.util.List;

public interface ServiceOrderService {
    ServiceOrders findById(Long id);
    ServiceOrders insertServiceOrder(DTOServiceOrders dto);
    ServiceOrders updateServiceOrder(DTOServiceOrders dto);
    ServiceOrders updateStatus(Long id, OrderStatus status);  // para que el técnico actualice el estado
    List<ServiceOrders> listAllServiceOrders();
    List<ServiceOrders> findByTechnicalId(Long technicalId);
    List<ServiceOrders> findByStatus(OrderStatus status);
    void deleteServiceOrder(Long id);
}

