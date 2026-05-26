package prueba.com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOWorkshop {
    private Long id;
    private String workshopName;
    private String ownerName;
    private String email;
    private String phone;
    private String address;
    private String plan;
    private Boolean active;
}
