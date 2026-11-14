package com.warriors.sponsors.service;

import com.warriors.sponsors.client.EvenementGateway;
import com.warriors.sponsors.dto.ConventionRequest;
import com.warriors.sponsors.dto.ConventionResponse;
import com.warriors.sponsors.entity.ConventionPartenariat;
import com.warriors.sponsors.entity.PackSponsoring;
import com.warriors.sponsors.entity.Sponsor;
import com.warriors.sponsors.enums.DecisionSollicitation;
import com.warriors.sponsors.enums.StatutPaiement;
import com.warriors.sponsors.exception.BusinessException;
import com.warriors.sponsors.exception.ConflictException;
import com.warriors.sponsors.exception.NotFoundException;
import com.warriors.sponsors.messaging.ConventionPublisher;
import com.warriors.sponsors.messaging.ConventionValideeMessage;
import com.warriors.sponsors.repository.ConventionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConventionService {

    private final ConventionRepository conventionRepository;
    private final SponsorService sponsorService;
    private final PackSponsoringService packService;
    private final EvenementGateway evenementGateway;
    private final ConventionPublisher conventionPublisher;

    public ConventionResponse solliciter(ConventionRequest request) {
        ConventionPartenariat convention = preparer(request);
        convention.setStatutPaiement(StatutPaiement.EN_ATTENTE);
        return toResponse(conventionRepository.save(convention));
    }

    public ConventionResponse souscrire(ConventionRequest request) {
        ConventionPartenariat convention = preparer(request);
        convention.setStatutPaiement(StatutPaiement.ACCEPTEE);
        return toResponse(conventionRepository.save(convention));
    }

    public ConventionResponse traiter(Long id, DecisionSollicitation decision) {
        ConventionPartenariat convention = findOrThrow(id);
        if (convention.getStatutPaiement() != StatutPaiement.EN_ATTENTE) {
            throw new BusinessException("Seule une sollicitation en attente peut etre acceptee ou refusee.");
        }
        if (decision == DecisionSollicitation.ACCEPTER) {
            convention.setStatutPaiement(StatutPaiement.ACCEPTEE);
        } else {
            convention.setStatutPaiement(StatutPaiement.REFUSEE);
        }
        return toResponse(conventionRepository.save(convention));
    }

    public ConventionResponse valider(Long id) {
        ConventionPartenariat convention = findOrThrow(id);
        if (convention.getStatutPaiement() != StatutPaiement.ACCEPTEE) {
            throw new BusinessException("Seule une convention acceptee peut etre validee financierement.");
        }
        evenementGateway.verifierEvenementPublie(convention.getEvenementId());
        verifierMontantEligible(convention.getPack(), convention.getBudgetAlloue());

        convention.setStatutPaiement(StatutPaiement.PAYE);
        ConventionPartenariat saved = conventionRepository.save(convention);
        conventionPublisher.publier(toMessage(saved));
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ConventionResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<ConventionResponse> lister(Long sponsorId, Long evenementId) {
        List<ConventionPartenariat> conventions;
        if (sponsorId != null) {
            conventions = conventionRepository.findBySponsor_Id(sponsorId);
        } else if (evenementId != null) {
            conventions = conventionRepository.findByEvenementId(evenementId);
        } else {
            conventions = conventionRepository.findAll();
        }

        if (sponsorId != null && evenementId != null) {
            conventions = conventions.stream()
                    .filter(c -> evenementId.equals(c.getEvenementId()))
                    .toList();
        }
        return conventions.stream().map(this::toResponse).toList();
    }

    private ConventionPartenariat preparer(ConventionRequest request) {
        Sponsor sponsor = sponsorService.findOrThrow(request.getSponsorId());
        PackSponsoring pack = packService.findOrThrow(request.getPackId());
        evenementGateway.verifierEvenementPublie(request.getEvenementId());
        verifierMontantEligible(pack, request.getBudgetAlloue());

        if (conventionRepository.existsBySponsor_IdAndEvenementIdAndStatutPaiementNot(
                sponsor.getId(), request.getEvenementId(), StatutPaiement.REFUSEE)) {
            throw new ConflictException("Une convention existe deja pour ce sponsor et cet evenement.");
        }

        ConventionPartenariat convention = new ConventionPartenariat();
        convention.setSponsor(sponsor);
        convention.setPack(pack);
        convention.setEvenementId(request.getEvenementId());
        convention.setBudgetAlloue(pack.getTarif());
        return convention;
    }

    private void verifierMontantEligible(PackSponsoring pack, Double budgetDemande) {
        if (pack.getTarif() == null || pack.getTarif() <= 0) {
            throw new BusinessException("Le montant sponsorise n'est pas eligible : le tarif du pack doit etre positif.");
        }
        if (budgetDemande != null && Double.compare(budgetDemande, pack.getTarif()) != 0) {
            throw new BusinessException(
                    "Le montant sponsorise n'est pas eligible : il doit etre egal au tarif du pack (" + pack.getTarif() + ")."
            );
        }
    }

    private ConventionPartenariat findOrThrow(Long id) {
        return conventionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Convention introuvable : " + id));
    }

    private ConventionValideeMessage toMessage(ConventionPartenariat convention) {
        ConventionValideeMessage message = new ConventionValideeMessage();
        message.setConventionId(convention.getId());
        message.setSponsorId(convention.getSponsor().getId());
        message.setNomEntreprise(convention.getSponsor().getNomEntreprise());
        message.setPackId(convention.getPack().getId());
        message.setNomPack(convention.getPack().getNomPack());
        message.setStandInclus(convention.getPack().getStandInclus());
        message.setEvenementId(convention.getEvenementId());
        message.setBudgetAlloue(convention.getBudgetAlloue());
        return message;
    }

    private ConventionResponse toResponse(ConventionPartenariat convention) {
        return ConventionResponse.builder()
                .id(convention.getId())
                .sponsorId(convention.getSponsor().getId())
                .nomEntreprise(convention.getSponsor().getNomEntreprise())
                .packId(convention.getPack().getId())
                .nomPack(convention.getPack().getNomPack())
                .standInclus(convention.getPack().getStandInclus())
                .evenementId(convention.getEvenementId())
                .budgetAlloue(convention.getBudgetAlloue())
                .statutPaiement(convention.getStatutPaiement())
                .build();
    }
}
