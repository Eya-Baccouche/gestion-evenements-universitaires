package com.warriors.sponsors.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackRequest {

    @NotBlank(message = "Le nom du pack est obligatoire")
    @Size(max = 100)
    private String nomPack;

    @NotNull(message = "Le tarif est obligatoire")
    @Positive(message = "Le tarif doit etre positif")
    private Double tarif;

    @NotNull(message = "Indiquer si le stand est inclus")
    private Boolean standInclus;
}
