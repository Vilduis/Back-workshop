package prueba.com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prueba.com.example.demo.dtos.DTOCustomer;
import prueba.com.example.demo.entities.Customer;
import prueba.com.example.demo.services.CustomerService;

import java.util.List;

@RestController
@RequestMapping("")
@CrossOrigin("*")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.listAllCustomers());
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@RequestBody DTOCustomer dto) {
        return new ResponseEntity<>(customerService.insertCustomer(dto), HttpStatus.CREATED);
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody DTOCustomer dto) {
        dto.setId(id);
        return ResponseEntity.ok(customerService.updateCustomer(dto));
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

