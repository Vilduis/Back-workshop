package prueba.com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prueba.com.example.demo.entities.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    public Authority findByName(String name);
}
