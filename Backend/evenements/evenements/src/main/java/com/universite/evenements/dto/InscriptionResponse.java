package com.universite.evenements.dto;

import com.universite.evenements.enums.StatutInscription;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class InscriptionResponse {

    private Long id;
    private Long participantId;
    private StatutInscription statut;
    private LocalDateTime date;
    private Long evenementId;
    private String evenementTitre;
    private LocalDateTime evenementDate;
}
