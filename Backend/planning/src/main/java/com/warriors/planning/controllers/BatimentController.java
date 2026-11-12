package com.warriors.planning.controllers;

import com.warriors.planning.dto.BatimentDTO;
import com.warriors.planning.services.BatimentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API des bâtiments — réservée au rôle ADMIN.
 */
@RestController
@RequestMapping("/api/planning/batiments")
@RequiredArgsConstructor
public class BatimentController {

    private final BatimentService batimentService;

    @GetMapping
    public ResponseEntity<List<BatimentDTO>> findAll() {
        return ResponseEntity.ok(batimentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatimentDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(batimentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BatimentDTO> create(@Valid @RequestBody BatimentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(batimentService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BatimentDTO> update(@PathVariable Long id,
                                              @Valid @RequestBody BatimentDTO dto) {
        return ResponseEntity.ok(batimentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        batimentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
