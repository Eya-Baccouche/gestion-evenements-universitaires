package com.warriors.sponsors.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PackResponse {

    private Long id;
    private String nomPack;
    private Double tarif;
    private Boolean standInclus;
}
