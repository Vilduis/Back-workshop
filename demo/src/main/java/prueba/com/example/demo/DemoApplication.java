package prueba.com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import prueba.com.example.demo.entities.Authority;
import prueba.com.example.demo.entities.User;
import prueba.com.example.demo.repositories.AuthorityRepository;
import prueba.com.example.demo.repositories.UserRepository;

import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(AuthorityRepository authorityRepository,
							   UserRepository userRepository,
							   PasswordEncoder passwordEncoder) {
		return args -> {

			// --- Authorities (solo si no existen) ---
			if (authorityRepository.findByName("ADMIN") == null) {
				Authority adminAuth = new Authority();
				adminAuth.setName("ADMIN");
				authorityRepository.save(adminAuth);
			}

			if (authorityRepository.findByName("TECNICO") == null) {
				Authority tecnicoAuth = new Authority();
				tecnicoAuth.setName("TECNICO");
				authorityRepository.save(tecnicoAuth);
			}

			// --- Usuario Admin inicial (solo si no existe) ---
			if (userRepository.findByEmail("admin@admin.com") == null) {
				User admin = new User();
				admin.setEmail("admin@admin.com");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setActive(true);
				admin.setAuthorities(List.of(authorityRepository.findByName("ADMIN")));
				userRepository.save(admin);
			}
		};
	}
}

