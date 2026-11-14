package com.warriors.sponsors.client;

import com.warriors.sponsors.exception.BusinessException;
import com.warriors.sponsors.exception.NotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EvenementGateway {

    private final EvenementsClient evenementsClient;

    public void verifierEvenementPublie(Long evenementId) {
        EvenementClientDto evenement;
        try {
            evenement = evenementsClient.getEvenement(evenementId);
        } catch (FeignException.NotFound ex) {
            throw new NotFoundException("Evenement introuvable : " + evenementId);
        } catch (FeignException ex) {
            throw new BusinessException("Impossible de verifier l'evenement : le service evenements est indisponible.");
        }

        if (evenement == null || evenement.getId() == null) {
            throw new NotFoundException("Evenement introuvable : " + evenementId);
        }
        if (!"PUBLIE".equalsIgnoreCase(evenement.getStatut())) {
            throw new BusinessException("L'evenement doit etre au statut PUBLIE pour enregistrer une convention.");
        }
    }
}
