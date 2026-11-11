package com.universite.evenements.service;

import com.universite.evenements.dto.EvenementRequest;
import com.universite.evenements.dto.EvenementResponse;
import com.universite.evenements.enums.StatutEvenement;
import com.universite.evenements.enums.TypeEvenement;

import java.util.List;

public interface EvenementService {

    EvenementResponse creer(EvenementRequest request);
    List<EvenementResponse> listerTous();
    List<EvenementResponse> filtrer(TypeEvenement type, StatutEvenement statut);
    EvenementResponse getById(Long id);
    EvenementResponse modifier(Long id, EvenementRequest request);
    EvenementResponse publier(Long id);
    EvenementResponse annuler(Long id);
}
