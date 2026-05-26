package prueba.com.example.demo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DTOForgotPassword {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    private String email;
}
