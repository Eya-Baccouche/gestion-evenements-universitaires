package com.warriors.feedback.intervenants.service;

import com.warriors.feedback.feedback.client.EvenementsClient;
import com.warriors.feedback.intervenants.model.Affectation;
import com.warriors.feedback.intervenants.model.Intervenant;
import com.warriors.feedback.intervenants.repository.AffectationRepository;
import com.warriors.feedback.intervenants.repository.IntervenantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class IntervenantService {

    private final IntervenantRepository intervenantRepository;
    private final AffectationRepository affectationRepository;
    private final EvenementsClient evenementsClient;

    public IntervenantService(IntervenantRepository intervenantRepository,
                               AffectationRepository affectationRepository,
                               EvenementsClient evenementsClient) {
        this.intervenantRepository = intervenantRepository;
        this.affectationRepository = affectationRepository;
        this.evenementsClient = evenementsClient;
    }

    public Intervenant creerIntervenant(Intervenant intervenant) {
        return intervenantRepository.save(intervenant);
    }

    public Affectation affecter(Affectation affectation) {
        if (affectationRepository.existsByIntervenantIdAndEvenementId(
                affectation.getIntervenantId(), affectation.getEvenementId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Deja affecte a cet evenement");
        }
        try {
            evenementsClient.getEvenement(affectation.getEvenementId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Service Evenements indisponible, reessayez");
        }
        return affectationRepository.save(affectation);
    }

    public List<Affectation> getAffectations(Long intervenantId) {
        return affectationRepository.findByIntervenantId(intervenantId);
    }
}
