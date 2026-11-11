package com.universite.evenements.dto;

import com.universite.evenements.enums.TypeEvenement;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EvenementRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 150, message = "Le titre ne peut pas dépasser 150 caractères")
    private String titre;

    @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères")
    private String description;

    @NotNull(message = "Le type est obligatoire")
    private TypeEvenement type;

    @NotNull(message = "La date est obligatoire")
    private LocalDateTime date;

    private Long salleId;

    @NotNull(message = "La capacité est obligatoire")
    @Min(value = 1, message = "La capacité doit être strictement positive")
    private Integer capacite;
}
