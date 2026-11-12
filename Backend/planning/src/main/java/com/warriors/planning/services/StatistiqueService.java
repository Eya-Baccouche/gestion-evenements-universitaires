package com.warriors.planning.services;

import com.warriors.planning.dto.StatistiqueDTO;
import com.warriors.planning.entities.ReservationSalle;
import com.warriors.planning.entities.Salle;
import com.warriors.planning.entities.enums.StatutReservation;
import com.warriors.planning.repositories.ReservationSalleRepository;
import com.warriors.planning.repositories.SalleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service de statistiques (R-A6 : taux d'occupation des salles).
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatistiqueService {

    private final SalleRepository salleRepository;
    private final ReservationSalleRepository reservationRepository;

    public StatistiqueDTO getStatistiques() {
        List<Salle> salles = salleRepository.findAll();
        List<ReservationSalle> reservations = reservationRepository.findAll();

        long enAttente = reservations.stream()
                .filter(r -> r.getStatut() == StatutReservation.EN_ATTENTE).count();
        long confirmees = reservations.stream()
                .filter(r -> r.getStatut() == StatutReservation.CONFIRMEE).count();
        long annulees = reservations.stream()
                .filter(r -> r.getStatut() == StatutReservation.ANNULEE).count();
        long refusees = reservations.stream()
                .filter(r -> r.getStatut() == StatutReservation.REFUSEE).count();

        // Taux d'occupation : réservations (EN_ATTENTE + CONFIRMEE) / total salles * 100
        Map<String, Double> tauxParSalle = new LinkedHashMap<>();
        Map<String, Integer> topSalles = new LinkedHashMap<>();

        Map<String, Long> countParSalle = salles.stream()
                .collect(Collectors.toMap(
                        Salle::getNom,
                        s -> reservations.stream()
                                .filter(r -> r.getSalle().getId().equals(s.getId()))
                                .filter(r -> r.getStatut() == StatutReservation.EN_ATTENTE
                                        || r.getStatut() == StatutReservation.CONFIRMEE)
                                .count(),
                        (a, b) -> a,
                        LinkedHashMap::new));

        long totalReservationsActives = countParSalle.values().stream().mapToLong(Long::longValue).sum();

        countParSalle.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> {
                    double taux = totalReservationsActives > 0
                            ? (e.getValue() * 100.0 / totalReservationsActives) : 0.0;
                    tauxParSalle.put(e.getKey(), Math.round(taux * 100.0) / 100.0);
                });

        // Top 5 salles
        countParSalle.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(e -> topSalles.put(e.getKey(), e.getValue().intValue()));

        return StatistiqueDTO.builder()
                .totalSalles(salles.size())
                .totalReservations(reservations.size())
                .reservationsEnAttente((int) enAttente)
                .reservationsConfirmees((int) confirmees)
                .reservationsAnnulees((int) annulees)
                .reservationsRefusees((int) refusees)
                .tauxOccupationParSalle(tauxParSalle)
                .topSalles(topSalles)
                .build();
    }
}
