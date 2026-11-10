package com.warriors.feedback.feedback.controller;

import com.warriors.feedback.feedback.model.Avis;
import com.warriors.feedback.feedback.service.AvisService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/{id}/feedback")
public class FeedbackController {

    private final AvisService avisService;

    public FeedbackController(AvisService avisService) {
        this.avisService = avisService;
    }

    @PostMapping
    public ResponseEntity<Avis> deposerAvis(@PathVariable("id") Long evenementId, @Valid @RequestBody Avis avis) {
        return ResponseEntity.status(HttpStatus.CREATED).body(avisService.deposerAvis(evenementId, avis));
    }

    @GetMapping
    public List<Avis> getAvis(@PathVariable("id") Long evenementId) {
        return avisService.getAvisForEvenement(evenementId);
    }
}
