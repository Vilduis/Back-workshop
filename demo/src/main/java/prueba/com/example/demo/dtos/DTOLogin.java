package prueba.com.example.demo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DTOLogin {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
