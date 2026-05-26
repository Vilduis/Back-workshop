package prueba.com.example.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import prueba.com.example.demo.dtos.DTOUpdateWorkshop;
import prueba.com.example.demo.dtos.DTOWorkshop;
import prueba.com.example.demo.entities.Workshop;
import prueba.com.example.demo.services.WorkshopService;

@RestController
@RequestMapping("/workshops")
@CrossOrigin("*")
public class WorkshopController {

    @Autowired
    private WorkshopService workshopService;

    @GetMapping("/{id}")
    public ResponseEntity<DTOWorkshop> getWorkshop(@PathVariable("id") Long id) {
        return ResponseEntity.ok(toDto(workshopService.findById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DTOWorkshop> updateWorkshop(@PathVariable("id") Long id,
                                                     @Valid @RequestBody DTOUpdateWorkshop dto) {
        return ResponseEntity.ok(toDto(workshopService.updateWorkshop(id, dto)));
    }

    private DTOWorkshop toDto(Workshop w) {
        DTOWorkshop dto = new DTOWorkshop();
        dto.setId(w.getId());
        dto.setWorkshopName(w.getWorkshopName());
        dto.setOwnerName(w.getOwnerName());
        dto.setEmail(w.getEmail());
        dto.setPhone(w.getPhone());
        dto.setAddress(w.getAddress());
        dto.setPlan(w.getPlan() != null ? w.getPlan().name() : null);
        dto.setActive(w.getActive());
        return dto;
    }
}
