package com.universite.evenements.service;

import com.universite.evenements.dto.InscriptionRequest;
import com.universite.evenements.dto.InscriptionResponse;

import java.util.List;

public interface InscriptionService {

    InscriptionResponse inscrire(Long evenementId, InscriptionRequest request);
    List<InscriptionResponse> listerParEvenement(Long evenementId);
    InscriptionResponse getById(Long id);
    void annuler(Long id);
}
