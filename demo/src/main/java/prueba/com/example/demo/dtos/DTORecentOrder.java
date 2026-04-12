package prueba.com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTORecentOrder {
    private Long orderId;
    private String vehiclePlate;
    private String vehicleBrand;
    private String vehicleModel;
    private String customerName;
    private String technicalName;
    private String diagnosis;
    private String status;
    private LocalDateTime date;
}

