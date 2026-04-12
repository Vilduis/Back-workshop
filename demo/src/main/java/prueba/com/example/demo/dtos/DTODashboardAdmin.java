package prueba.com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTODashboardAdmin {

    // --- Tarjeta: Vehículos ---
    private long totalVehicles;
    private long newVehiclesThisWeek;

    // --- Tarjeta: Clientes ---
    private long totalCustomers;
    private long newCustomersThisWeek;

    // --- Tarjeta: Órdenes Hoy ---
    private long ordersToday;
    private long completedToday;

    // --- Estado del Taller ---
    private long pendingOrders;      // PENDIENTE
    private long inProcessOrders;    // EN_PROCESO (= bahías ocupadas)
    private long completedOrders;    // TERMINADO

    // --- Actividad Reciente (últimas 5 órdenes) ---
    private List<DTORecentOrder> recentOrders;
}

