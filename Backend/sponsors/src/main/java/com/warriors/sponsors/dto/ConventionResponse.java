package com.warriors.sponsors.dto;

import com.warriors.sponsors.enums.StatutPaiement;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConventionResponse {

    private Long id;
    private Long sponsorId;
    private String nomEntreprise;
    private Long packId;
    private String nomPack;
    private Boolean standInclus;
    private Long evenementId;
    private Double budgetAlloue;
    private StatutPaiement statutPaiement;
}
