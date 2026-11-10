package com.warriors.feedback.feedback.service;

import com.warriors.feedback.feedback.client.EvenementsClient;
import com.warriors.feedback.feedback.model.Avis;
import com.warriors.feedback.feedback.repository.AvisRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class AvisService {

    private final AvisRepository avisRepository;
    private final EvenementsClient evenementsClient;

    public AvisService(AvisRepository avisRepository, EvenementsClient evenementsClient) {
        this.avisRepository = avisRepository;
        this.evenementsClient = evenementsClient;
    }

    public Avis deposerAvis(Long evenementId, Avis avis) {
        // shortcut: no real presence check yet (Check-in service not wired), only duplicate check
        if (avisRepository.existsByEvenementIdAndParticipantId(evenementId, avis.getParticipantId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Avis deja depose pour cet evenement");
        }
        avis.setEvenementId(evenementId);
        return avisRepository.save(avis);
    }

    public List<Avis> getAvisForEvenement(Long evenementId) {
        return avisRepository.findByEvenementId(evenementId);
    }
}
