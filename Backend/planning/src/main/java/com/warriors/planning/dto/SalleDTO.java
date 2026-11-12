package com.warriors.planning.dto;

import com.warriors.planning.entities.Salle;
import com.warriors.planning.entities.enums.StatutSalle;
import com.warriors.planning.entities.enums.TypeSalle;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalleDTO {

    private Long id;

    @NotBlank(message = "Le nom de la salle est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String nom;

    @NotNull(message = "Le type de salle est obligatoire")
    private TypeSalle typeSalle;

    @NotNull(message = "La capacité est obligatoire")
    @Min(value = 1, message = "La capacité doit être au moins 1")
    private Integer capacite;

    @NotNull(message = "L'étage est obligatoire")
    @Min(value = 0, message = "L'étage ne peut pas être négatif")
    private Integer etage;

    @NotNull(message = "Le bâtiment est obligatoire")
    private Long batimentId;

    private String batimentNom;

    @Size(max = 500, message = "Les équipements ne doivent pas dépasser 500 caractères")
    private String equipements;

    private StatutSalle statut;

    private String description;

    public static SalleDTO fromEntity(Salle s) {
        return SalleDTO.builder()
                .id(s.getId())
                .nom(s.getNom())
                .typeSalle(s.getTypeSalle())
                .capacite(s.getCapacite())
                .etage(s.getEtage())
                .batimentId(s.getBatiment() != null ? s.getBatiment().getId() : null)
                .batimentNom(s.getBatiment() != null ? s.getBatiment().getNom() : null)
                .equipements(s.getEquipements())
                .statut(s.getStatut())
                .description(s.getDescription())
                .build();
    }
}
