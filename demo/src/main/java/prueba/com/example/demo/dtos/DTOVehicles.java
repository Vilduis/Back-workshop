package prueba.com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class DTOVehicles {
    private Long id;
    private String plate;
    private String brand;
    private String model;
    private Integer year;
    private Long customerId;  // FK al cliente
}
