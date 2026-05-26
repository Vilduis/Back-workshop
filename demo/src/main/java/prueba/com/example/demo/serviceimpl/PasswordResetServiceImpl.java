package prueba.com.example.demo.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prueba.com.example.demo.entities.PasswordResetToken;
import prueba.com.example.demo.entities.User;
import prueba.com.example.demo.exceptions.InvalidDataException;
import prueba.com.example.demo.repositories.PasswordResetTokenRepository;
import prueba.com.example.demo.repositories.UserRepository;
import prueba.com.example.demo.services.EmailService;
import prueba.com.example.demo.services.PasswordResetService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetServiceImpl.class);
    private static final long TOKEN_TTL_MINUTES = 30;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null || user.getActive() == null || !user.getActive()) {
            log.info("Solicitud de reset para email no existente o inactivo: {}", email);
            return;
        }

        tokenRepository.invalidateAllForUser(user.getId());

        String token = generateToken();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(TOKEN_TTL_MINUTES));
        resetToken.setUsed(false);
        tokenRepository.save(resetToken);

        String base = frontendUrl.endsWith("/") ? frontendUrl.substring(0, frontendUrl.length() - 1) : frontendUrl;
        String resetUrl = base + "/reset-password?token=" + token;

        String displayName = user.getWorkshop() != null ? user.getWorkshop().getOwnerName() : user.getEmail();
        emailService.sendPasswordResetEmail(user.getEmail(), displayName, resetUrl);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidDataException("Token inválido"));

        if (Boolean.TRUE.equals(resetToken.getUsed())) {
            throw new InvalidDataException("Este token ya fue utilizado");
        }
        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidDataException("El token ha expirado, solicita uno nuevo");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        log.info("Contraseña reseteada exitosamente para usuario {}", user.getEmail());
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
