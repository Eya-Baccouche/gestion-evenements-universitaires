package com.warriors.planning.entities;

import com.warriors.planning.entities.enums.StatutSalle;
import com.warriors.planning.entities.enums.TypeSalle;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Entité Salle — réservable par les organisateurs.
 */
@Entity
@Table(name = "salle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de la salle est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull(message = "Le type de salle est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(name = "type_salle", nullable = false, length = 30)
    private TypeSalle typeSalle;

    @NotNull(message = "La capacité est obligatoire")
    @Min(value = 1, message = "La capacité doit être au moins 1")
    @Column(name = "capacite", nullable = false)
    private Integer capacite;

    @NotNull(message = "L'étage est obligatoire")
    @Min(value = 0, message = "L'étage ne peut pas être négatif")
    @Column(name = "etage", nullable = false)
    private Integer etage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batiment_id", nullable = false)
    @NotNull(message = "Le bâtiment est obligatoire")
    private Batiment batiment;

    @Size(max = 500, message = "La liste d'équipements ne doit pas dépasser 500 caractères")
    @Column(name = "equipements", length = 500)
    private String equipements;

    @NotNull(message = "Le statut est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 20)
    @Builder.Default
    private StatutSalle statut = StatutSalle.DISPONIBLE;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
