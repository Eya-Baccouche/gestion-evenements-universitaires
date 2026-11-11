package com.universite.evenements.service.impl;

import com.universite.evenements.dto.InscriptionRequest;
import com.universite.evenements.dto.InscriptionResponse;
import com.universite.evenements.entity.Evenement;
import com.universite.evenements.entity.Inscription;
import com.universite.evenements.enums.StatutEvenement;
import com.universite.evenements.enums.StatutInscription;
import com.universite.evenements.repository.EvenementRepository;
import com.universite.evenements.repository.InscriptionRepository;
import com.universite.evenements.service.InscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InscriptionServiceImpl implements InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final EvenementRepository evenementRepository;

    @Override
    public InscriptionResponse inscrire(Long evenementId, InscriptionRequest request) {
        Evenement evenement = evenementRepository.findByIdWithLock(evenementId)
                .orElseThrow(() -> new RuntimeException("Événement introuvable avec l'id : " + evenementId));

        if (evenement.getStatut() != StatutEvenement.PUBLIE) {
            throw new RuntimeException("Les inscriptions ne sont ouvertes que pour les événements PUBLIÉS.");
        }

        boolean dejaInscrit = inscriptionRepository
                .existsByEvenementIdAndParticipantId(evenementId, request.getParticipantId());
        if (dejaInscrit) {
            throw new RuntimeException("Ce participant est déjà inscrit à cet événement.");
        }

        long nombreInscrits = inscriptionRepository
                .countByEvenementIdAndStatut(evenementId, StatutInscription.CONFIRMEE);
        if (nombreInscrits >= evenement.getCapacite()) {
            throw new RuntimeException("Capacité maximale atteinte.");
        }

        Inscription inscription = new Inscription();
        inscription.setEvenement(evenement);
        inscription.setParticipantId(request.getParticipantId());

        return toResponse(inscriptionRepository.save(inscription));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscriptionResponse> listerParEvenement(Long evenementId) {
        if (!evenementRepository.existsById(evenementId)) {
            throw new RuntimeException("Événement introuvable avec l'id : " + evenementId);
        }
        return inscriptionRepository.findByEvenementId(evenementId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InscriptionResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public void annuler(Long id) {
        Inscription inscription = findOrThrow(id);

        if (inscription.getStatut() == StatutInscription.ANNULEE) {
            throw new RuntimeException("Cette inscription est déjà annulée.");
        }

        inscription.setStatut(StatutInscription.ANNULEE);
        inscriptionRepository.save(inscription);
    }

    private Inscription findOrThrow(Long id) {
        return inscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription introuvable avec l'id : " + id));
    }

    private InscriptionResponse toResponse(Inscription i) {
        Evenement e = i.getEvenement();
        return InscriptionResponse.builder()
                .id(i.getId())
                .participantId(i.getParticipantId())
                .statut(i.getStatut())
                .date(i.getDate())
                .evenementId(e != null ? e.getId() : null)
                .evenementTitre(e != null ? e.getTitre() : null)
                .evenementDate(e != null ? e.getDate() : null)
                .build();
    }
}
