package prueba.com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import prueba.com.example.demo.entities.OrderStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOServiceOrders {
    private Long id;
    private LocalDateTime date;
    private Long vehicleId;
    private Long customerId;
    private Long technicalId;
    private String diagnosis;
    private OrderStatus status;  // PENDIENTE, EN_PROCESO, TERMINADO
}
