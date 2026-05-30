package prueba.com.example.demo.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prueba.com.example.demo.dtos.DTOCreateTechnical;
import prueba.com.example.demo.dtos.DTOTechnical;
import prueba.com.example.demo.entities.Authority;
import prueba.com.example.demo.entities.Technical;
import prueba.com.example.demo.entities.User;
import prueba.com.example.demo.entities.Workshop;
import prueba.com.example.demo.exceptions.InvalidDataException;
import prueba.com.example.demo.exceptions.ResourceNotFoundException;
import prueba.com.example.demo.repositories.TechnicalRepository;
import prueba.com.example.demo.repositories.UserRepository;
import prueba.com.example.demo.repositories.WorkshopRepository;
import prueba.com.example.demo.security.TenantContext;
import prueba.com.example.demo.services.AuthorityService;
import prueba.com.example.demo.services.EmailService;
import prueba.com.example.demo.services.PasswordGeneratorService;
import prueba.com.example.demo.services.TechnicalService;

import java.util.List;

@Service
public class TechnicalServiceImpl implements TechnicalService {

    @Autowired
    private TechnicalRepository technicalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkshopRepository workshopRepository;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordGeneratorService passwordGenerator;

    @Autowired
    private EmailService emailService;

    @Override
    public Technical findById(Long id) {
        Long workshopId = TenantContext.requireWorkshopId();
        return technicalRepository.findByIdAndWorkshopId(id, workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Technical with id: " + id + " not found"));
    }

    @Override
    @Transactional
    public Technical createTechnical(DTOCreateTechnical dto) {
        Long workshopId = TenantContext.requireWorkshopId();
        Workshop workshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop no encontrado"));

        if (userRepository.findByEmail(dto.getEmail()) != null) {
            throw new InvalidDataException("Ya existe un usuario con ese email");
        }

        String tempPassword = passwordGenerator.generateTemporaryPassword();
        Authority technicalAuthority = authorityService.findByName("TECHNICAL");

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setActive(true);
        user.setMustChangePassword(true);
        user.setWorkshop(workshop);
        user.setAuthorities(List.of(technicalAuthority));
        user = userRepository.save(user);

        Technical technical = new Technical();
        technical.setName(dto.getName());
        technical.setLastName(dto.getLastName());
        technical.setSpecialty(dto.getSpecialty());
        technical.setUser(user);
        technical.setWorkshop(workshop);
        Technical saved = technicalRepository.save(technical);

        emailService.sendTemporaryPassword(
                dto.getEmail(),
                dto.getName() + " " + dto.getLastName(),
                tempPassword,
                workshop.getWorkshopName()
        );

        return saved;
    }

    @Override
    @Transactional
    public Technical updateTechnical(DTOTechnical dto) {
        Technical found = findById(dto.getId());
        found.setName(dto.getName());
        found.setLastName(dto.getLastName());
        found.setSpecialty(dto.getSpecialty());

        if (dto.getEmail() != null && !dto.getEmail().isBlank()
                && !dto.getEmail().equals(found.getUser().getEmail())) {
            User existing = userRepository.findByEmail(dto.getEmail());
            if (existing != null && !existing.getId().equals(found.getUser().getId())) {
                throw new InvalidDataException("Ya existe un usuario con ese email");
            }
            found.getUser().setEmail(dto.getEmail());
            userRepository.save(found.getUser());
        }

        return technicalRepository.save(found);
    }

    @Override
    public List<Technical> listAllTechnicals() {
        Long workshopId = TenantContext.requireWorkshopId();
        return technicalRepository.findAllByWorkshopId(workshopId);
    }

    @Override
    public void deleteTechnical(Long id) {
        Technical found = findById(id);
        technicalRepository.delete(found);
    }
}
