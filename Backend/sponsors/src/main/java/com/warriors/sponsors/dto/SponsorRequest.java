package com.warriors.sponsors.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SponsorRequest {

    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    @Size(max = 150)
    private String nomEntreprise;

    @NotBlank(message = "L'email de contact est obligatoire")
    @Email(message = "L'email de contact est invalide")
    @Size(max = 150)
    private String contactEmail;

    @Size(max = 500)
    private String logoUrl;
}
