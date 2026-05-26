package prueba.com.example.demo.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prueba.com.example.demo.dtos.DTOCustomer;
import prueba.com.example.demo.entities.Customer;
import prueba.com.example.demo.entities.Workshop;
import prueba.com.example.demo.exceptions.ResourceNotFoundException;
import prueba.com.example.demo.repositories.CustomerRepository;
import prueba.com.example.demo.repositories.WorkshopRepository;
import prueba.com.example.demo.security.TenantContext;
import prueba.com.example.demo.services.CustomerService;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WorkshopRepository workshopRepository;

    @Override
    public Customer findById(Long id) {
        Long workshopId = TenantContext.requireWorkshopId();
        return customerRepository.findByIdAndWorkshopId(id, workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id: " + id + " not found"));
    }

    @Override
    public Customer insertCustomer(DTOCustomer dto) {
        Long workshopId = TenantContext.requireWorkshopId();
        Workshop workshop = workshopRepository.getReferenceById(workshopId);

        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setLastName(dto.getLastName());
        customer.setPhone(dto.getPhone());
        customer.setEmail(dto.getEmail());
        customer.setWorkshop(workshop);
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(DTOCustomer dto) {
        Customer found = findById(dto.getId());
        found.setName(dto.getName());
        found.setLastName(dto.getLastName());
        found.setPhone(dto.getPhone());
        found.setEmail(dto.getEmail());
        return customerRepository.save(found);
    }

    @Override
    public List<Customer> listAllCustomers() {
        Long workshopId = TenantContext.requireWorkshopId();
        return customerRepository.findAllByWorkshopId(workshopId);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer found = findById(id);
        customerRepository.delete(found);
    }
}
