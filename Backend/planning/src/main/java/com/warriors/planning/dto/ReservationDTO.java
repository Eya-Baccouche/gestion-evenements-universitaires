package com.warriors.planning.dto;

import com.warriors.planning.entities.ReservationSalle;
import com.warriors.planning.entities.enums.RoleDemandeur;
import com.warriors.planning.entities.enums.StatutReservation;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO de lecture d'une réservation.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDTO {

    private Long id;
    private Long salleId;
    private String salleNom;
    private String batimentNom;
    private String titre;
    private String description;
    private LocalDate dateReservation;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private StatutReservation statut;
    private String demandePar;
    private RoleDemandeur roleDemandeur;
    private Long evenementId;
    private Integer nombrePersonnes;
    private Integer capaciteSalle;
    private String motifRefus;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    public static ReservationDTO fromEntity(ReservationSalle r) {
        return ReservationDTO.builder()
                .id(r.getId())
                .salleId(r.getSalle() != null ? r.getSalle().getId() : null)
                .salleNom(r.getSalle() != null ? r.getSalle().getNom() : null)
                .batimentNom(r.getSalle() != null && r.getSalle().getBatiment() != null
                        ? r.getSalle().getBatiment().getNom() : null)
                .titre(r.getTitre())
                .description(r.getDescription())
                .dateReservation(r.getDateReservation())
                .heureDebut(r.getHeureDebut())
                .heureFin(r.getHeureFin())
                .statut(r.getStatut())
                .demandePar(r.getDemandePar())
                .roleDemandeur(r.getRoleDemandeur())
                .evenementId(r.getEvenementId())
                .nombrePersonnes(r.getNombrePersonnes())
                .capaciteSalle(r.getSalle() != null ? r.getSalle().getCapacite() : null)
                .motifRefus(r.getMotifRefus())
                .dateCreation(r.getDateCreation())
                .dateModification(r.getDateModification())
                .build();
    }
}
