package com.warriors.planning.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité Bâtiment — contient plusieurs salles.
 */
@Entity
@Table(name = "batiment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Batiment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du bâtiment est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    @Column(name = "nom", nullable = false, unique = true)
    private String nom;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères")
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @NotNull(message = "Le nombre d'étages est obligatoire")
    @Min(value = 1, message = "Le nombre d'étages doit être au moins 1")
    @Column(name = "nb_etages", nullable = false)
    private Integer nbEtages;

    @OneToMany(mappedBy = "batiment", cascade = CascadeType.REMOVE, orphanRemoval = false)
    @Builder.Default
    private List<Salle> salles = new ArrayList<>();
}
