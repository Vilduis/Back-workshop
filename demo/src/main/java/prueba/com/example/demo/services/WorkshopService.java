package prueba.com.example.demo.services;

import prueba.com.example.demo.dtos.DTORegisterWorkshop;
import prueba.com.example.demo.dtos.DTOUpdateWorkshop;
import prueba.com.example.demo.entities.User;
import prueba.com.example.demo.entities.Workshop;

public interface WorkshopService {
    /**
     * Crea el Workshop y al primer User con rol ADMIN.
     * Devuelve el User admin recién creado.
     */
    User registerWorkshop(DTORegisterWorkshop dto);

    Workshop findById(Long id);

    Workshop updateWorkshop(Long id, DTOUpdateWorkshop dto);
}
