package prueba.com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import prueba.com.example.demo.dtos.DTOUser;
import prueba.com.example.demo.entities.Authority;
import prueba.com.example.demo.entities.User;
import prueba.com.example.demo.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
@CrossOrigin("*")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<DTOUser>> getAllUsers() {
        List<User> users = userService.listAllUser();
        List<DTOUser> dtos = users.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<DTOUser> getUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(toDto(userService.findById(id)), HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody DTOUser dtoUser) {
        dtoUser.setId(id);
        return new ResponseEntity<>(userService.updateUser(dtoUser), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private DTOUser toDto(User user) {
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
    }
}
