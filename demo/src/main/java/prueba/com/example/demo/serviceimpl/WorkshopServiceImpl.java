package prueba.com.example.demo.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prueba.com.example.demo.dtos.DTORegisterWorkshop;
import prueba.com.example.demo.dtos.DTOUpdateWorkshop;
import prueba.com.example.demo.entities.Authority;
import prueba.com.example.demo.entities.Plan;
import prueba.com.example.demo.entities.User;
import prueba.com.example.demo.entities.Workshop;
import prueba.com.example.demo.exceptions.InvalidDataException;
import prueba.com.example.demo.exceptions.ResourceNotFoundException;
import prueba.com.example.demo.repositories.UserRepository;
import prueba.com.example.demo.repositories.WorkshopRepository;
import prueba.com.example.demo.security.TenantContext;
import prueba.com.example.demo.services.AuthorityService;
import prueba.com.example.demo.services.WorkshopService;

import java.util.List;

@Service
public class WorkshopServiceImpl implements WorkshopService {

    @Autowired
    private WorkshopRepository workshopRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerWorkshop(DTORegisterWorkshop dto) {
        if (workshopRepository.existsByEmail(dto.getEmail())) {
            throw new InvalidDataException("Ya existe un taller registrado con ese email");
        }
        if (userRepository.findByEmail(dto.getEmail()) != null) {
            throw new InvalidDataException("Ya existe un usuario con ese email");
        }

        Workshop workshop = new Workshop();
        workshop.setWorkshopName(dto.getWorkshopName());
        workshop.setOwnerName(dto.getOwnerName());
        workshop.setEmail(dto.getEmail());
        workshop.setPhone(dto.getPhone());
        workshop.setAddress(dto.getAddress());
        workshop.setPlan(Plan.FREE);
        workshop.setActive(true);
        workshop = workshopRepository.save(workshop);

        Authority adminAuthority = authorityService.findByName("ADMIN");

        User admin = new User();
        admin.setEmail(dto.getEmail());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setActive(true);
        admin.setMustChangePassword(false);
        admin.setWorkshop(workshop);
        admin.setAuthorities(List.of(adminAuthority));

        return userRepository.save(admin);
    }

    @Override
    public Workshop findById(Long id) {
        Long tenantId = TenantContext.requireWorkshopId();
        if (!tenantId.equals(id)) {
            throw new InvalidDataException("No tienes acceso a este taller");
        }
        return workshopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Taller no encontrado con id: " + id));
    }

    @Override
    @Transactional
    public Workshop updateWorkshop(Long id, DTOUpdateWorkshop dto) {
        Workshop workshop = findById(id);
        workshop.setWorkshopName(dto.getWorkshopName());
        workshop.setOwnerName(dto.getOwnerName());
        workshop.setPhone(dto.getPhone());
        workshop.setAddress(dto.getAddress());
        return workshopRepository.save(workshop);
    }
}
