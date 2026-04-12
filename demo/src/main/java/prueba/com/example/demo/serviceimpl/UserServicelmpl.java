package prueba.com.example.demo.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import prueba.com.example.demo.dtos.DTOUser;
import prueba.com.example.demo.entities.Authority;
import prueba.com.example.demo.entities.User;
import prueba.com.example.demo.exceptions.ResourceNotFoundException;
import prueba.com.example.demo.repositories.UserRepository;
import prueba.com.example.demo.security.SecurityUser;
import prueba.com.example.demo.services.AuthorityService;
import prueba.com.example.demo.services.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServicelmpl implements UserService, UserDetailsService {
    @Autowired
    AuthorityService authorityService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User findByEmail(String email)
    {
        User userFound = userRepository.findByEmail(email);
        if(userFound==null)
        {
            throw new ResourceNotFoundException("User with email: "+ email + " can not be found");
        }
        return userFound;
    }

    @Override
    public User findById(Long id)
    {
        User userFound= userRepository.findById(id).orElse(null);
        if (userFound==null)
        {
            throw  new ResourceNotFoundException("User with id:"+id+"can not be found");
        }
        return userFound;
    }

    public List<Authority> authorityListFromString(String authorityString) {

        List<String> authorityListString =  List.of(authorityString.split(";"));

        List<Authority> authorityList = new ArrayList<>();

        for (String authorityStringItem: authorityListString){
            Authority authority = authorityService.findByName(authorityStringItem);
            authorityList.add(authority);
        }

        return  authorityList;
    }

    @Override
    public User insertUser(DTOUser dtoUser)
    {
        User usernew = new User();
        usernew.setEmail(dtoUser.getEmail());
        usernew.setPassword(passwordEncoder.encode(dtoUser.getPassword()));
        usernew.setActive(true);
        usernew.setAuthorities(authorityListFromString(dtoUser.getAuthorities()));

        return userRepository.save(usernew);
    }
    @Override
    public User updateUser(DTOUser dtoUser)
    {
        User userFound = findById(dtoUser.getId());
        userFound.setEmail(dtoUser.getEmail());
        userFound.setPassword(passwordEncoder.encode(dtoUser.getPassword()));
        userFound.setActive(true);
        return userRepository.save(userFound);
    }

    @Override
    public List<User> listAllUser()
    {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id)
    {
        User userFound = userRepository.findById(id).orElse(null);
        if(userFound==null)
        {
            throw new ResourceNotFoundException("User with id:"+id+"can not be found");
        }
        userRepository.delete(userFound);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new SecurityUser(findByEmail(email));
    }

}