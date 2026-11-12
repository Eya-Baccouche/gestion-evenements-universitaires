package com.warriors.planning.controllers;

import com.warriors.planning.dto.StatistiqueDTO;
import com.warriors.planning.services.StatistiqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API des statistiques — réservée au rôle ADMIN (R-A6).
 */
@RestController
@RequestMapping("/api/planning/statistiques")
@RequiredArgsConstructor
public class StatistiqueController {

    private final StatistiqueService statistiqueService;

    @GetMapping("/occupation")
    public ResponseEntity<StatistiqueDTO> getStatistiques() {
        return ResponseEntity.ok(statistiqueService.getStatistiques());
    }
}
