package com.warriors.sponsors.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SponsorResponse {

    private Long id;
    private String nomEntreprise;
    private String contactEmail;
    private String logoUrl;
}
