package prueba.com.example.demo.services;

import prueba.com.example.demo.dtos.DTOUser;
import prueba.com.example.demo.entities.User;

import java.util.List;

public interface UserService {
    public User findByEmail(String email);
    public User findById(Long id);
    public User insertUser(DTOUser dtoUser);
    public User updateUser(DTOUser dtoUser);
    public List<User> listAllUser();
    public void deleteUser(Long id);
}
