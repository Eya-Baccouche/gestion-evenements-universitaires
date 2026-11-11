package com.universite.evenements.service.impl;

import com.universite.evenements.dto.EvenementRequest;
import com.universite.evenements.dto.EvenementResponse;
import com.universite.evenements.entity.Evenement;
import com.universite.evenements.enums.StatutEvenement;
import com.universite.evenements.enums.StatutInscription;
import com.universite.evenements.enums.TypeEvenement;
import com.universite.evenements.repository.EvenementRepository;
import com.universite.evenements.repository.InscriptionRepository;
import com.universite.evenements.service.EvenementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EvenementServiceImpl implements EvenementService {

    private final EvenementRepository evenementRepository;
    private final InscriptionRepository inscriptionRepository;

    @Override
    public EvenementResponse creer(EvenementRequest request) {
        Evenement e = new Evenement();
        appliquerRequest(e, request);
        return toResponse(evenementRepository.save(e));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvenementResponse> listerTous() {
        return evenementRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvenementResponse> filtrer(TypeEvenement type, StatutEvenement statut) {
        List<Evenement> liste;

        if (type != null && statut != null) {
            liste = evenementRepository.findByTypeAndStatut(type, statut);
        } else if (type != null) {
            liste = evenementRepository.findByType(type);
        } else if (statut != null) {
            liste = evenementRepository.findByStatut(statut);
        } else {
            liste = evenementRepository.findAll();
        }

        return liste.stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EvenementResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public EvenementResponse modifier(Long id, EvenementRequest request) {
        Evenement e = findOrThrow(id);

        if (e.getStatut() != StatutEvenement.BROUILLON) {
            throw new RuntimeException("Seul un événement en statut BROUILLON peut être modifié.");
        }

        appliquerRequest(e, request);
        return toResponse(evenementRepository.save(e));
    }

    @Override
    public EvenementResponse publier(Long id) {
        Evenement e = findOrThrow(id);

        if (e.getStatut() == StatutEvenement.PUBLIE) {
            throw new RuntimeException("L'événement est déjà publié.");
        }
        if (e.getStatut() == StatutEvenement.ANNULE) {
            throw new RuntimeException("Un événement annulé ne peut pas être publié.");
        }
        if (e.getStatut() == StatutEvenement.TERMINE) {
            throw new RuntimeException("Un événement terminé ne peut pas être republié.");
        }

        e.setStatut(StatutEvenement.PUBLIE);
        return toResponse(evenementRepository.save(e));
    }

    @Override
    public EvenementResponse annuler(Long id) {
        Evenement e = findOrThrow(id);

        if (e.getStatut() == StatutEvenement.ANNULE) {
            throw new RuntimeException("L'événement est déjà annulé.");
        }
        if (e.getStatut() == StatutEvenement.TERMINE) {
            throw new RuntimeException("Un événement terminé ne peut pas être annulé.");
        }

        e.setStatut(StatutEvenement.ANNULE);
        return toResponse(evenementRepository.save(e));
    }

    private Evenement findOrThrow(Long id) {
        return evenementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement introuvable avec l'id : " + id));
    }

    private void appliquerRequest(Evenement e, EvenementRequest req) {
        e.setTitre(req.getTitre());
        e.setDescription(req.getDescription());
        e.setType(req.getType());
        e.setDate(req.getDate());
        e.setSalleId(req.getSalleId());
        e.setCapacite(req.getCapacite());
    }

    private EvenementResponse toResponse(Evenement e) {
        long inscrits = inscriptionRepository
                .countByEvenementIdAndStatut(e.getId(), StatutInscription.CONFIRMEE);

        return EvenementResponse.builder()
                .id(e.getId())
                .titre(e.getTitre())
                .description(e.getDescription())
                .type(e.getType())
                .date(e.getDate())
                .salleId(e.getSalleId())
                .capacite(e.getCapacite())
                .statut(e.getStatut())
                .nombreInscrits(inscrits)
                .build();
    }
}
