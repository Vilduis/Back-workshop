package prueba.com.example.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import prueba.com.example.demo.dtos.*;
import prueba.com.example.demo.entities.Authority;
import prueba.com.example.demo.entities.User;
import prueba.com.example.demo.exceptions.InvalidDataException;
import prueba.com.example.demo.exceptions.ResourceNotFoundException;
import prueba.com.example.demo.entities.Technical;
import prueba.com.example.demo.repositories.TechnicalRepository;
import prueba.com.example.demo.repositories.UserRepository;
import prueba.com.example.demo.security.JwtUtilService;
import prueba.com.example.demo.security.SecurityUser;
import prueba.com.example.demo.services.PasswordResetService;
import prueba.com.example.demo.services.UserService;
import prueba.com.example.demo.services.WorkshopService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private WorkshopService workshopService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private TechnicalRepository technicalRepository;

    @PostMapping("/register-workshop")
    public ResponseEntity<DTOToken> registerWorkshop(@Valid @RequestBody DTORegisterWorkshop dto) {
        User admin = workshopService.registerWorkshop(dto);
        return new ResponseEntity<>(buildToken(admin), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<DTOToken> login(@Valid @RequestBody DTOLogin dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        User user = userService.findByEmail(dto.getEmail());
        return ResponseEntity.ok(buildToken(user));
    }

    @GetMapping("/me")
    public ResponseEntity<DTOMe> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof SecurityUser securityUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.findById(securityUser.getUserId());
        if (user.getActive() == null || !user.getActive()) {
            throw new ResourceNotFoundException("La cuenta está deshabilitada");
        }
        String authorities = user.getAuthorities().stream()
                .map(Authority::getName).collect(Collectors.joining(";"));

        DTOMe dto = new DTOMe();
        dto.setUserId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setDisplayName(resolveDisplayName(user, authorities));
        dto.setAuthorities(authorities);
        dto.setActive(user.getActive());
        dto.setMustChangePassword(user.getMustChangePassword());
        dto.setWorkshopId(user.getWorkshop().getId());
        dto.setWorkshopName(user.getWorkshop().getWorkshopName());
        return ResponseEntity.ok(dto);
    }

    private String resolveDisplayName(User user, String authorities) {
        if (authorities.contains("ADMIN")) {
            String ownerName = user.getWorkshop().getOwnerName();
            if (ownerName != null && !ownerName.isBlank()) {
                return ownerName;
            }
        }
        if (authorities.contains("TECHNICAL")) {
            Technical technical = technicalRepository.findByUserId(user.getId());
            if (technical != null) {
                String name = technical.getName() != null ? technical.getName() : "";
                String lastName = technical.getLastName() != null ? technical.getLastName() : "";
                String full = (name + " " + lastName).trim();
                if (!full.isBlank()) {
                    return full;
                }
            }
        }
        String email = user.getEmail();
        int at = email.indexOf('@');
        return at > 0 ? email.substring(0, at) : email;
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody DTOChangePassword dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof SecurityUser securityUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.findById(securityUser.getUserId());
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidDataException("La contraseña actual es incorrecta");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setMustChangePassword(false);
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody DTOForgotPassword dto) {
        passwordResetService.requestPasswordReset(dto.getEmail());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody DTOResetPassword dto) {
        passwordResetService.resetPassword(dto.getToken(), dto.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    private DTOToken buildToken(User user) {
        DTOToken token = new DTOToken();
        token.setUserId(user.getId());
        token.setAuthorities(user.getAuthorities().stream()
                .map(Authority::getName).collect(Collectors.joining(";")));
        token.setJwtToken(jwtUtilService.generateToken(new SecurityUser(user)));
        token.setWorkshopId(user.getWorkshop().getId());
        token.setWorkshopName(user.getWorkshop().getWorkshopName());
        token.setMustChangePassword(user.getMustChangePassword());
        return token;
    }
}
