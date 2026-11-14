package com.warriors.sponsors.controller;

import com.warriors.sponsors.dto.SponsorRequest;
import com.warriors.sponsors.dto.SponsorResponse;
import com.warriors.sponsors.service.SponsorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sponsors")
@RequiredArgsConstructor
public class SponsorController {

    private final SponsorService sponsorService;

    @PostMapping
    public ResponseEntity<SponsorResponse> creer(@Valid @RequestBody SponsorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sponsorService.creer(request));
    }

    @GetMapping
    public ResponseEntity<List<SponsorResponse>> lister() {
        return ResponseEntity.ok(sponsorService.lister());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SponsorResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sponsorService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SponsorResponse> modifier(
            @PathVariable Long id,
            @Valid @RequestBody SponsorRequest request) {
        return ResponseEntity.ok(sponsorService.modifier(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        sponsorService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
