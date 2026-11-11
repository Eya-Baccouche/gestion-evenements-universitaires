package com.universite.evenements.controller;

import com.universite.evenements.dto.InscriptionRequest;
import com.universite.evenements.dto.InscriptionResponse;
import com.universite.evenements.service.InscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InscriptionController {

    private final InscriptionService inscriptionService;

    @PostMapping("/events/{evenementId}/registrations")
    public ResponseEntity<InscriptionResponse> inscrire(
            @PathVariable Long evenementId,
            @Valid @RequestBody InscriptionRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(inscriptionService.inscrire(evenementId, request));
    }

    @GetMapping("/events/{evenementId}/registrations")
    public ResponseEntity<List<InscriptionResponse>> listerParEvenement(
            @PathVariable Long evenementId) {
        return ResponseEntity.ok(inscriptionService.listerParEvenement(evenementId));
    }

    @GetMapping("/registrations/{id}")
    public ResponseEntity<InscriptionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(inscriptionService.getById(id));
    }

    @DeleteMapping("/registrations/{id}")
    public ResponseEntity<Void> annuler(@PathVariable Long id) {
        inscriptionService.annuler(id);
        return ResponseEntity.noContent().build();
    }
}
