package prueba.com.example.demo.services;

import prueba.com.example.demo.dtos.DTOCreateTechnical;
import prueba.com.example.demo.dtos.DTOTechnical;
import prueba.com.example.demo.entities.Technical;

import java.util.List;

public interface TechnicalService {
    Technical findById(Long id);
    Technical createTechnical(DTOCreateTechnical dto);
    Technical updateTechnical(DTOTechnical dto);
    List<Technical> listAllTechnicals();
    void deleteTechnical(Long id);
}
