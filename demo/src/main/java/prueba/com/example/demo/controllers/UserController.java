package prueba.com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import prueba.com.example.demo.dtos.DTOToken;
import prueba.com.example.demo.dtos.DTOUser;
import prueba.com.example.demo.entities.Authority;
import prueba.com.example.demo.entities.User;
import prueba.com.example.demo.security.JwtUtilService;
import prueba.com.example.demo.security.SecurityUser;
import prueba.com.example.demo.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    JwtUtilService jwtUtilService;
    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping("/users")
    public ResponseEntity<List<DTOUser>> getAllUsers()
    {
        List<User> users = userService.listAllUser();
        List<DTOUser> dtos = users.stream().map(user -> {
            DTOUser dto = new DTOUser();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setActive(user.getActive());
            dto.setAuthorities(
                user.getAuthorities() != null && !user.getAuthorities().isEmpty()
                    ? user.getAuthorities().stream()
                        .map(Authority::getName)
                        .collect(Collectors.joining(";"))
                    : ""
            );
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<DTOUser> getUser(@PathVariable("id") Long id)
    {
        User user = userService.findById(id);
        DTOUser dto = new DTOUser();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setActive(user.getActive());
        dto.setAuthorities(
            user.getAuthorities() != null && !user.getAuthorities().isEmpty()
                ? user.getAuthorities().stream()
                    .map(Authority::getName)
                    .collect(Collectors.joining(";"))
                : ""
        );
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/users/login")
    public ResponseEntity<DTOToken> login(@RequestBody DTOUser dtoUser)
    {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dtoUser.getEmail(), dtoUser.getPassword()));

        DTOToken dtotoken = new DTOToken();
        User userFound = userService.findByEmail(dtoUser.getEmail());

        dtotoken.setUserId(userFound.getId());
        dtotoken.setAuthorities( userFound.getAuthorities().stream().map(Authority::getName).collect(Collectors.joining(";","","")) );
        dtotoken.setJwtToken(jwtUtilService.generateToken(new SecurityUser(userFound)));

        return new ResponseEntity<>(dtotoken, HttpStatus.OK);
    }

    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody DTOUser dtoUser)
    {
        User user = userService.insertUser(dtoUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody DTOUser dtoUser)
    {
        User user = userService.updateUser(dtoUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id)
    {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}