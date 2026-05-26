package prueba.com.example.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import prueba.com.example.demo.dtos.DTOCreateTechnical;
import prueba.com.example.demo.dtos.DTOTechnical;
import prueba.com.example.demo.entities.Technical;
import prueba.com.example.demo.services.TechnicalService;

import java.util.List;

@RestController
@RequestMapping("")
@CrossOrigin("*")
public class TechnicalController {

    @Autowired
    TechnicalService technicalService;

    @GetMapping("/technicals")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICAL')")
    public ResponseEntity<List<Technical>> getAllTechnicals() {
        return ResponseEntity.ok(technicalService.listAllTechnicals());
    }

    @GetMapping("/technicals/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TECHNICAL')")
    public ResponseEntity<Technical> getTechnical(@PathVariable Long id) {
        return ResponseEntity.ok(technicalService.findById(id));
    }

    @PostMapping("/technicals")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Technical> createTechnical(@Valid @RequestBody DTOCreateTechnical dto) {
        return new ResponseEntity<>(technicalService.createTechnical(dto), HttpStatus.CREATED);
    }

    @PutMapping("/technicals/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Technical> updateTechnical(@PathVariable Long id, @RequestBody DTOTechnical dto) {
        dto.setId(id);
        return ResponseEntity.ok(technicalService.updateTechnical(dto));
    }

    @DeleteMapping("/technicals/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteTechnical(@PathVariable Long id) {
        technicalService.deleteTechnical(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
