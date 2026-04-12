package prueba.com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOCustomer {
    private Long id;
    private String name;
    private String lastName;
    private String phone;
    private String email;
}

