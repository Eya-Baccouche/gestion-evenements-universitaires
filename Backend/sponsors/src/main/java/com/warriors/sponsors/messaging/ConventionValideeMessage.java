package com.warriors.sponsors.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConventionValideeMessage {

    private Long conventionId;
    private Long sponsorId;
    private String nomEntreprise;
    private Long packId;
    private String nomPack;
    private Boolean standInclus;
    private Long evenementId;
    private Double budgetAlloue;
}
