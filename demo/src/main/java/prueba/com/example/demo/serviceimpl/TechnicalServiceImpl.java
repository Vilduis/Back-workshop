package prueba.com.example.demo.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prueba.com.example.demo.dtos.DTOTechnical;
import prueba.com.example.demo.entities.Technical;
import prueba.com.example.demo.exceptions.ResourceNotFoundException;
import prueba.com.example.demo.repositories.TechnicalRepository;
import prueba.com.example.demo.services.TechnicalService;
import prueba.com.example.demo.services.UserService;

import java.util.List;

@Service
public class TechnicalServiceImpl implements TechnicalService {

    @Autowired
    TechnicalRepository technicalRepository;

    @Autowired
    UserService userService;

    @Override
    public Technical findById(Long id) {
        return technicalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technical with id: " + id + " not found"));
    }

    @Override
    public Technical insertTechnical(DTOTechnical dto) {
        Technical technical = new Technical();
        technical.setName(dto.getName());
        technical.setLastName(dto.getLastName());
        technical.setSpecialty(dto.getSpecialty());
        technical.setUser(userService.findById(dto.getUserId()));
        return technicalRepository.save(technical);
    }

    @Override
    public Technical updateTechnical(DTOTechnical dto) {
        Technical found = findById(dto.getId());
        found.setName(dto.getName());
        found.setLastName(dto.getLastName());
        found.setSpecialty(dto.getSpecialty());
        if (dto.getUserId() != null) {
            found.setUser(userService.findById(dto.getUserId()));
        }
        return technicalRepository.save(found);
    }

    @Override
    public List<Technical> listAllTechnicals() {
        return technicalRepository.findAll();
    }

    @Override
    public void deleteTechnical(Long id) {
        Technical found = findById(id);
        technicalRepository.delete(found);
    }
}

