package com.warriors.sponsors.dto;

import com.warriors.sponsors.enums.DecisionSollicitation;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TraiterSollicitationRequest {

    @NotNull(message = "La decision est obligatoire")
    private DecisionSollicitation decision;
}
