package prueba.com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<List<Technical>> getAllTechnicals() {
        return ResponseEntity.ok(technicalService.listAllTechnicals());
    }

    @GetMapping("/technicals/{id}")
    public ResponseEntity<Technical> getTechnical(@PathVariable Long id) {
        return ResponseEntity.ok(technicalService.findById(id));
    }

    @PostMapping("/technicals")
    public ResponseEntity<Technical> createTechnical(@RequestBody DTOTechnical dto) {
        return new ResponseEntity<>(technicalService.insertTechnical(dto), HttpStatus.CREATED);
    }

    @PutMapping("/technicals/{id}")
    public ResponseEntity<Technical> updateTechnical(@PathVariable Long id, @RequestBody DTOTechnical dto) {
        dto.setId(id);
        return ResponseEntity.ok(technicalService.updateTechnical(dto));
    }

    @DeleteMapping("/technicals/{id}")
    public ResponseEntity<Void> deleteTechnical(@PathVariable Long id) {
        technicalService.deleteTechnical(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

