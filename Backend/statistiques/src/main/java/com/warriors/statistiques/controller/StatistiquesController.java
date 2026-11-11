package com.warriors.statistiques.controller;

import com.warriors.statistiques.entity.Instantane;
import com.warriors.statistiques.entity.StatEvenement;
import com.warriors.statistiques.service.StatistiquesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/stats")
@CrossOrigin(origins = "*")
public class StatistiquesController {

    @Autowired
    private StatistiquesService statistiquesService;

    // 1. GET /stats/events/{id} - Consulter les statistiques d'un evenement (200, 404)
    @GetMapping("/events/{id}")
    public ResponseEntity<StatEvenement> getById(@PathVariable Long id) {
        Optional<StatEvenement> stat = statistiquesService.getByEvenementId(id);
        return stat.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 2. GET /stats/events?from=&to= - Lister les statistiques de tous les evenements (200)
    @GetMapping("/events")
    public List<StatEvenement> getAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return statistiquesService.getAll(from, to);
    }

    // 3. GET /stats/overview - Indicateurs globaux pour le tableau de bord (200)
    @GetMapping("/overview")
    public Map<String, Object> getOverview() {
        return statistiquesService.getOverview();
    }

    // 4. GET /stats/events/{id}/timeline - Historique temporel d'un evenement (200)
    @GetMapping("/events/{id}/timeline")
    public List<Instantane> getTimeline(@PathVariable Long id) {
        return statistiquesService.getTimeline(id);
    }
}
