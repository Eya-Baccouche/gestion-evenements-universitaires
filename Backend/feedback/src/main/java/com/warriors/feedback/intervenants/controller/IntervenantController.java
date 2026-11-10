package com.warriors.feedback.intervenants.controller;

import com.warriors.feedback.intervenants.model.Affectation;
import com.warriors.feedback.intervenants.model.Intervenant;
import com.warriors.feedback.intervenants.service.IntervenantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IntervenantController {

    private final IntervenantService intervenantService;

    public IntervenantController(IntervenantService intervenantService) {
        this.intervenantService = intervenantService;
    }

    @PostMapping("/speakers")
    public ResponseEntity<Intervenant> creerIntervenant(@Valid @RequestBody Intervenant intervenant) {
        return ResponseEntity.status(HttpStatus.CREATED).body(intervenantService.creerIntervenant(intervenant));
    }

    @PostMapping("/assignments")
    public ResponseEntity<Affectation> affecter(@Valid @RequestBody Affectation affectation) {
        return ResponseEntity.status(HttpStatus.CREATED).body(intervenantService.affecter(affectation));
    }

    @GetMapping("/speakers/{id}/assignments")
    public List<Affectation> getAffectations(@PathVariable("id") Long intervenantId) {
        return intervenantService.getAffectations(intervenantId);
    }
}
