package com.warriors.planning.controllers;

import com.warriors.planning.dto.ReservationCreateDTO;
import com.warriors.planning.dto.ReservationDTO;
import com.warriors.planning.entities.enums.StatutReservation;
import com.warriors.planning.services.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * API des réservations.
 * - Organisateur : créer (R-O1), modifier (R-O2), annuler (R-O3), consulter les siennes (R-O4).
 * - Admin        : consulter tout (R-A5), valider (R-A3), refuser (R-A3), annuler (R-A4).
 */
@RestController
@RequestMapping("/api/planning/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // ---------- R-A5 : toutes les réservations (admin) ----------
    @GetMapping
    public ResponseEntity<List<ReservationDTO>> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    // ---------- R-A5 : réservations filtrées ----------
    @GetMapping("/filtres")
    public ResponseEntity<List<ReservationDTO>> findFiltrees(
            @RequestParam(required = false) StatutReservation statut,
            @RequestParam(required = false) Long salleId,
            @RequestParam(required = false) String demandePar,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(reservationService.findFiltrees(
                statut, salleId, demandePar, dateDebut, dateFin));
    }

    // ---------- R-O4 : mes réservations (organisateur) ----------
    @GetMapping("/mes-reservations")
    public ResponseEntity<List<ReservationDTO>> mesReservations(@RequestParam String demandePar) {
        return ResponseEntity.ok(reservationService.findByDemandeur(demandePar));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.findById(id));
    }

    // ---------- R-O1 : créer une demande ----------
    @PostMapping
    public ResponseEntity<ReservationDTO> create(@Valid @RequestBody ReservationCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.create(dto));
    }

    // ---------- R-O2 : modifier sa demande (si EN_ATTENTE) ----------
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> update(@PathVariable Long id,
                                                 @Valid @RequestBody ReservationCreateDTO dto) {
        return ResponseEntity.ok(reservationService.update(id, dto));
    }

    // ---------- R-O3 / R-A4 : annuler ----------
    @PatchMapping("/{id}/annuler")
    public ResponseEntity<ReservationDTO> annuler(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.annuler(id));
    }

    // ---------- R-A3 : valider (admin) ----------
    @PatchMapping("/{id}/valider")
    public ResponseEntity<ReservationDTO> valider(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.valider(id));
    }

    // ---------- R-A3 : refuser (admin, motif obligatoire) ----------
    @PatchMapping("/{id}/refuser")
    public ResponseEntity<ReservationDTO> refuser(@PathVariable Long id,
                                                  @RequestBody Map<String, String> body) {
        String motif = body.get("motifRefus");
        return ResponseEntity.ok(reservationService.refuser(id, motif));
    }
}
