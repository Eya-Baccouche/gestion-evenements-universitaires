package com.warriors.sponsors.controller;

import com.warriors.sponsors.dto.ConventionRequest;
import com.warriors.sponsors.dto.ConventionResponse;
import com.warriors.sponsors.dto.TraiterSollicitationRequest;
import com.warriors.sponsors.service.ConventionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conventions")
@RequiredArgsConstructor
public class ConventionController {

    private final ConventionService conventionService;

    @PostMapping("/sollicitations")
    public ResponseEntity<ConventionResponse> solliciter(@Valid @RequestBody ConventionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(conventionService.solliciter(request));
    }

    @PostMapping
    public ResponseEntity<ConventionResponse> souscrire(@Valid @RequestBody ConventionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(conventionService.souscrire(request));
    }

    @PutMapping("/{id}/traiter")
    public ResponseEntity<ConventionResponse> traiter(
            @PathVariable Long id,
            @Valid @RequestBody TraiterSollicitationRequest request) {
        return ResponseEntity.ok(conventionService.traiter(id, request.getDecision()));
    }

    @PutMapping("/{id}/valider")
    public ResponseEntity<ConventionResponse> valider(@PathVariable Long id) {
        return ResponseEntity.ok(conventionService.valider(id));
    }

    @GetMapping
    public ResponseEntity<List<ConventionResponse>> lister(
            @RequestParam(required = false) Long sponsorId,
            @RequestParam(required = false) Long evenementId) {
        return ResponseEntity.ok(conventionService.lister(sponsorId, evenementId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConventionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(conventionService.getById(id));
    }
}
