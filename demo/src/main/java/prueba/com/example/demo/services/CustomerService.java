package prueba.com.example.demo.services;

import prueba.com.example.demo.dtos.DTOCustomer;
import prueba.com.example.demo.entities.Customer;

import java.util.List;

public interface CustomerService {
    Customer findById(Long id);
    Customer insertCustomer(DTOCustomer dto);
    Customer updateCustomer(DTOCustomer dto);
    List<Customer> listAllCustomers();
    void deleteCustomer(Long id);
}

