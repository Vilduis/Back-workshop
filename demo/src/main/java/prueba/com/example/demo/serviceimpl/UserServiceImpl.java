package prueba.com.example.demo.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import prueba.com.example.demo.dtos.DTOUser;
import prueba.com.example.demo.entities.User;
import prueba.com.example.demo.exceptions.ResourceNotFoundException;
import prueba.com.example.demo.repositories.UserRepository;
import prueba.com.example.demo.security.SecurityUser;
import prueba.com.example.demo.security.TenantContext;
import prueba.com.example.demo.services.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User findByEmail(String email) {
        User userFound = userRepository.findByEmail(email);
        if (userFound == null) {
            throw new ResourceNotFoundException("User with email: " + email + " can not be found");
        }
        return userFound;
    }

    @Override
    public User findById(Long id) {
        Long workshopId = TenantContext.getWorkshopId();
        User userFound = (workshopId != null)
                ? userRepository.findByIdAndWorkshopId(id, workshopId).orElse(null)
                : userRepository.findById(id).orElse(null);
        if (userFound == null) {
            throw new ResourceNotFoundException("User with id:" + id + " can not be found");
        }
        return userFound;
    }

    @Override
    public User updateUser(DTOUser dtoUser) {
        User userFound = findById(dtoUser.getId());
        userFound.setEmail(dtoUser.getEmail());
        if (dtoUser.getActive() != null) {
            userFound.setActive(dtoUser.getActive());
        }
        return userRepository.save(userFound);
    }

    @Override
    public List<User> listAllUser() {
        Long workshopId = TenantContext.requireWorkshopId();
        return userRepository.findAllByWorkshopId(workshopId);
    }

    @Override
    public void deleteUser(Long id) {
        User userFound = findById(id);
        userRepository.delete(userFound);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new SecurityUser(findByEmail(email));
    }
}
