package com.warriors.planning.entities;

import com.warriors.planning.entities.enums.RoleDemandeur;
import com.warriors.planning.entities.enums.StatutReservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entité ReservationSalle — une demande/réservation de salle sur un créneau.
 */
@Entity
@Table(name = "reservation_salle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationSalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La salle est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 200, message = "Le titre ne doit pas dépasser 200 caractères")
    @Column(name = "titre", nullable = false, length = 200)
    private String titre;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "La date de réservation est obligatoire")
    @FutureOrPresent(message = "La date de réservation ne peut pas être dans le passé")
    @Column(name = "date_reservation", nullable = false)
    private LocalDate dateReservation;

    @NotNull(message = "L'heure de début est obligatoire")
    @Column(name = "heure_debut", nullable = false)
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin est obligatoire")
    @Column(name = "heure_fin", nullable = false)
    private LocalTime heureFin;

    @NotNull(message = "Le statut est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 20)
    @Builder.Default
    private StatutReservation statut = StatutReservation.EN_ATTENTE;

    @NotBlank(message = "Le demandeur est obligatoire")
    @Size(max = 150, message = "Le demandeur ne doit pas dépasser 150 caractères")
    @Column(name = "demande_par", nullable = false, length = 150)
    private String demandePar;

    @NotNull(message = "Le rôle du demandeur est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(name = "role_demandeur", nullable = false, length = 20)
    private RoleDemandeur roleDemandeur;

    @Column(name = "evenement_id")
    private Long evenementId;

    @NotNull(message = "Le nombre de personnes est obligatoire")
    @Min(value = 1, message = "Le nombre de personnes doit être au moins 1")
    @Column(name = "nombre_personnes", nullable = false)
    private Integer nombrePersonnes;

    @Column(name = "motif_refus", columnDefinition = "TEXT")
    private String motifRefus;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }
}
