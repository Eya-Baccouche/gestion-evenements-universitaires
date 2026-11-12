package com.warriors.planning.dto;

import com.warriors.planning.entities.enums.StatutSalle;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO pour changer le statut d'une salle (R-A2).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatutDTO {

    @NotNull(message = "Le nouveau statut est obligatoire")
    private StatutSalle statut;
}
