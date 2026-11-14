package com.warriors.sponsors.controller;

import com.warriors.sponsors.dto.PackRequest;
import com.warriors.sponsors.dto.PackResponse;
import com.warriors.sponsors.service.PackSponsoringService;
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
@RequestMapping("/api/packs")
@RequiredArgsConstructor
public class PackSponsoringController {

    private final PackSponsoringService packService;

    @PostMapping
    public ResponseEntity<PackResponse> creer(@Valid @RequestBody PackRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packService.creer(request));
    }

    @GetMapping
    public ResponseEntity<List<PackResponse>> lister() {
        return ResponseEntity.ok(packService.lister());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(packService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackResponse> modifier(
            @PathVariable Long id,
            @Valid @RequestBody PackRequest request) {
        return ResponseEntity.ok(packService.modifier(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        packService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
