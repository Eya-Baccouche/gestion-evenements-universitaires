package com.warriors.planning.controllers;

import com.warriors.planning.dto.SalleDTO;
import com.warriors.planning.dto.StatutDTO;
import com.warriors.planning.entities.enums.StatutSalle;
import com.warriors.planning.entities.enums.TypeSalle;
import com.warriors.planning.services.ReservationService;
import com.warriors.planning.services.SalleService;
import com.warriors.planning.dto.ReservationDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * API des salles.
 * - Lecture et recherche disponibles pour tous.
 * - Création / modification / suppression / changement de statut réservés ADMIN.
 */
@RestController
@RequestMapping("/api/planning/salles")
@RequiredArgsConstructor
public class SalleController {

    private final SalleService salleService;
    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<SalleDTO>> findAll(
            @RequestParam(required = false) TypeSalle type,
            @RequestParam(required = false) StatutSalle statut,
            @RequestParam(required = false) Long batimentId) {
        return ResponseEntity.ok(salleService.findByFiltres(type, statut, batimentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalleDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(salleService.findById(id));
    }

    // ---------- R-O5 : recherche de salles disponibles ----------
    @GetMapping("/disponibles")
    public ResponseEntity<List<SalleDTO>> findDisponibles(
            @RequestParam(required = false) TypeSalle typeSalle,
            @RequestParam(required = false) Integer capaciteMin,
            @RequestParam(required = false) Long batimentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureFin) {
        return ResponseEntity.ok(salleService.findSallesDisponibles(
                typeSalle, capaciteMin, batimentId, date, heureDebut, heureFin));
    }

    // ---------- R-O6 : planning d'une salle ----------
    @GetMapping("/{id}/planning")
    public ResponseEntity<List<ReservationDTO>> planningSalle(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(reservationService.findPlanningSalle(id, dateDebut, dateFin));
    }

    // ---------- R-A1 : CRUD salle (ADMIN) ----------
    @PostMapping
    public ResponseEntity<SalleDTO> create(@Valid @RequestBody SalleDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(salleService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalleDTO> update(@PathVariable Long id,
                                            @Valid @RequestBody SalleDTO dto) {
        return ResponseEntity.ok(salleService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- R-A2 : changement de statut ----------
    @PatchMapping("/{id}/statut")
    public ResponseEntity<SalleDTO> changerStatut(@PathVariable Long id,
                                                  @Valid @RequestBody StatutDTO dto) {
        return ResponseEntity.ok(salleService.changerStatut(id, dto));
    }
}
