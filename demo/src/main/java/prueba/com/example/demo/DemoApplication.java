package prueba.com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import prueba.com.example.demo.entities.Authority;
import prueba.com.example.demo.repositories.AuthorityRepository;

@SpringBootApplication
public class
DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner seedAuthorities(AuthorityRepository authorityRepository) {
		return args -> {
			ensureAuthority(authorityRepository, "ADMIN");
			ensureAuthority(authorityRepository, "TECHNICAL");
		};
	}

	private void ensureAuthority(AuthorityRepository repo, String name) {
		if (repo.findByName(name) == null) {
			Authority a = new Authority();
			a.setName(name);
			repo.save(a);
		}
	}
}
