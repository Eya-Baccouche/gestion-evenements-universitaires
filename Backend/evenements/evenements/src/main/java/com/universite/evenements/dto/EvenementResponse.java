package com.universite.evenements.dto;

import com.universite.evenements.enums.StatutEvenement;
import com.universite.evenements.enums.TypeEvenement;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EvenementResponse {

    private Long id;
    private String titre;
    private String description;
    private TypeEvenement type;
    private LocalDateTime date;
    private Long salleId;
    private Integer capacite;
    private StatutEvenement statut;
    private long nombreInscrits;
}
