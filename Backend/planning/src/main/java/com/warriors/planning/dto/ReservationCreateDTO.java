package com.warriors.planning.dto;

import com.warriors.planning.entities.enums.RoleDemandeur;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO pour la création d'une réservation (organisateur / admin).
 * Contrôle de saisie via Bean Validation.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationCreateDTO {

    @NotNull(message = "La salle est obligatoire")
    private Long salleId;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 200, message = "Le titre ne doit pas dépasser 200 caractères")
    private String titre;

    @Size(max = 2000, message = "La description ne doit pas dépasser 2000 caractères")
    private String description;

    @NotNull(message = "La date de réservation est obligatoire")
    @FutureOrPresent(message = "La date de réservation ne peut pas être dans le passé")
    private LocalDate dateReservation;

    @NotNull(message = "L'heure de début est obligatoire")
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin est obligatoire")
    private LocalTime heureFin;

    @NotBlank(message = "Le demandeur est obligatoire")
    @Size(max = 150, message = "Le demandeur ne doit pas dépasser 150 caractères")
    private String demandePar;

    @NotNull(message = "Le rôle du demandeur est obligatoire")
    private RoleDemandeur roleDemandeur;

    private Long evenementId;

    @NotNull(message = "Le nombre de personnes est obligatoire")
    @Min(value = 1, message = "Le nombre de personnes doit être au moins 1")
    private Integer nombrePersonnes;

    /** Motif de refus — obligatoire uniquement lors d'un refus admin. */
    @Size(max = 1000, message = "Le motif ne doit pas dépasser 1000 caractères")
    private String motifRefus;
}
