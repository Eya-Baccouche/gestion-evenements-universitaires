package com.universite.evenements.controller;

import com.universite.evenements.dto.EvenementRequest;
import com.universite.evenements.dto.EvenementResponse;
import com.universite.evenements.enums.StatutEvenement;
import com.universite.evenements.enums.TypeEvenement;
import com.universite.evenements.service.EvenementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EvenementController {

    private final EvenementService evenementService;

    @PostMapping
    public ResponseEntity<EvenementResponse> creer(
            @Valid @RequestBody EvenementRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(evenementService.creer(request));
    }

    @GetMapping
    public ResponseEntity<List<EvenementResponse>> lister(
            @RequestParam(required = false) TypeEvenement type,
            @RequestParam(required = false) StatutEvenement statut) {
        return ResponseEntity.ok(evenementService.filtrer(type, statut));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvenementResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(evenementService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvenementResponse> modifier(
            @PathVariable Long id,
            @Valid @RequestBody EvenementRequest request) {
        return ResponseEntity.ok(evenementService.modifier(id, request));
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<EvenementResponse> publier(@PathVariable Long id) {
        return ResponseEntity.ok(evenementService.publier(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<EvenementResponse> annuler(@PathVariable Long id) {
        return ResponseEntity.ok(evenementService.annuler(id));
    }
}
