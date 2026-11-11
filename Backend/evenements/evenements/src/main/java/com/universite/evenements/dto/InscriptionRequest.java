package com.universite.evenements.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InscriptionRequest {

    @NotNull(message = "L'identifiant du participant est obligatoire")
    private Long participantId;
}
