package prueba.com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOTechnical {
    private Long id;
    private String name;
    private String lastName;
    private String specialty;
    private Long userId;  // FK al usuario del sistema (rol TÉCNICO)
}
