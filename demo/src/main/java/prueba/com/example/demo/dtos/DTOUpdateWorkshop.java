package prueba.com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DTOUpdateWorkshop {

    @NotBlank(message = "El nombre del taller es obligatorio")
    private String workshopName;

    @NotBlank(message = "El nombre del dueño es obligatorio")
    private String ownerName;

    private String phone;
    private String address;
}
