package com.warriors.planning.dto;

import com.warriors.planning.entities.Batiment;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO pour la création / mise à jour d'un bâtiment.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatimentDTO {

    private Long id;

    @NotBlank(message = "Le nom du bâtiment est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String nom;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères")
    private String adresse;

    @NotNull(message = "Le nombre d'étages est obligatoire")
    @Min(value = 1, message = "Le nombre d'étages doit être au moins 1")
    private Integer nbEtages;

    /** Nombre de salles dans ce bâtiment (lecture seule). */
    private Integer nbSalles;

    public static BatimentDTO fromEntity(Batiment b) {
        return BatimentDTO.builder()
                .id(b.getId())
                .nom(b.getNom())
                .adresse(b.getAdresse())
                .nbEtages(b.getNbEtages())
                .nbSalles(b.getSalles() != null ? b.getSalles().size() : 0)
                .build();
    }
}
