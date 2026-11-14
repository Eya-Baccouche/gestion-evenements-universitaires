package com.warriors.sponsors.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConventionRequest {

    @NotNull(message = "Le sponsor est obligatoire")
    private Long sponsorId;

    @NotNull(message = "Le pack est obligatoire")
    private Long packId;

    @NotNull(message = "L'evenement est obligatoire")
    private Long evenementId;

    @Positive(message = "Le budget alloue doit etre positif")
    private Double budgetAlloue;
}
