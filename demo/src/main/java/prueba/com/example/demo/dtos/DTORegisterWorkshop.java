package prueba.com.example.demo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DTORegisterWorkshop {

    @NotBlank(message = "El nombre del taller es obligatorio")
    private String workshopName;

    @NotBlank(message = "El nombre del dueño es obligatorio")
    private String ownerName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    private String phone;
    private String address;
}
