package prueba.com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prueba.com.example.demo.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);

    List<User> findAllByWorkshopId(Long workshopId);
    Optional<User> findByIdAndWorkshopId(Long id, Long workshopId);
}
