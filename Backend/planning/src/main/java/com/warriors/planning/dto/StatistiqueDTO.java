package com.warriors.planning.dto;

import lombok.*;

import java.util.Map;

/**
 * DTO pour les statistiques d'occupation des salles (R-A6).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatistiqueDTO {

    private Integer totalSalles;
    private Integer totalReservations;
    private Integer reservationsEnAttente;
    private Integer reservationsConfirmees;
    private Integer reservationsAnnulees;
    private Integer reservationsRefusees;

    /** Taux d'occupation par salle (nom salle → pourcentage). */
    private Map<String, Double> tauxOccupationParSalle;

    /** Top 5 salles les plus réservées (nom salle → nombre de réservations). */
    private Map<String, Integer> topSalles;
}
