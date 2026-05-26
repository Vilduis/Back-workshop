package prueba.com.example.demo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DTOCreateTechnical {

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    private String specialty;

    @NotBlank
    @Email
    private String email;
}
