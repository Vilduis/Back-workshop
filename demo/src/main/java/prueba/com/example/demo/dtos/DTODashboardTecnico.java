package prueba.com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTODashboardTecnico {

    // --- Resumen de mis órdenes ---
    private long totalMyOrders;
    private long myPendingOrders;    // PENDIENTE
    private long myInProcessOrders;  // EN_PROCESO
    private long myCompletedOrders;  // TERMINADO

    // --- Mis últimas 5 órdenes ---
    private List<DTORecentOrder> myRecentOrders;
}

