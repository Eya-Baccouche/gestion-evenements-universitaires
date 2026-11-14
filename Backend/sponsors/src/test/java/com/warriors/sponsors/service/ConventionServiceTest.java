package com.warriors.sponsors.service;

import com.warriors.sponsors.client.EvenementGateway;
import com.warriors.sponsors.dto.ConventionRequest;
import com.warriors.sponsors.dto.ConventionResponse;
import com.warriors.sponsors.entity.PackSponsoring;
import com.warriors.sponsors.entity.Sponsor;
import com.warriors.sponsors.enums.DecisionSollicitation;
import com.warriors.sponsors.enums.StatutPaiement;
import com.warriors.sponsors.exception.BusinessException;
import com.warriors.sponsors.exception.ConflictException;
import com.warriors.sponsors.messaging.ConventionPublisher;
import com.warriors.sponsors.messaging.ConventionValideeMessage;
import com.warriors.sponsors.repository.ConventionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConventionServiceTest {

    @Mock
    private ConventionRepository conventionRepository;
    @Mock
    private SponsorService sponsorService;
    @Mock
    private PackSponsoringService packService;
    @Mock
    private EvenementGateway evenementGateway;
    @Mock
    private ConventionPublisher conventionPublisher;

    private ConventionService conventionService;
    private Sponsor sponsor;
    private PackSponsoring pack;

    @BeforeEach
    void setUp() {
        conventionService = new ConventionService(
                conventionRepository, sponsorService, packService, evenementGateway, conventionPublisher);

        sponsor = new Sponsor();
        sponsor.setId(1L);
        sponsor.setNomEntreprise("Nexio Conseil");
        sponsor.setContactEmail("contact@nexio.tn");

        pack = new PackSponsoring();
        pack.setId(2L);
        pack.setNomPack("Gold");
        pack.setTarif(1500.0);
        pack.setStandInclus(true);
    }

    @Test
    void solliciterCreeUneConventionEnAttente() {
        when(sponsorService.findOrThrow(1L)).thenReturn(sponsor);
        when(packService.findOrThrow(2L)).thenReturn(pack);
        when(conventionRepository.existsBySponsor_IdAndEvenementIdAndStatutPaiementNot(1L, 10L, StatutPaiement.REFUSEE))
                .thenReturn(false);
        when(conventionRepository.save(any())).thenAnswer(invocation -> {
            var convention = invocation.getArgument(0, com.warriors.sponsors.entity.ConventionPartenariat.class);
            convention.setId(5L);
            return convention;
        });

        ConventionResponse response = conventionService.solliciter(request(null));

        assertEquals(StatutPaiement.EN_ATTENTE, response.getStatutPaiement());
        assertEquals(1500.0, response.getBudgetAlloue());
        assertEquals(10L, response.getEvenementId());
        verify(evenementGateway).verifierEvenementPublie(10L);
        verify(conventionPublisher, never()).publier(any());
    }

    @Test
    void solliciterBloqueSiLeMontantNeCorrespondPasAuPack() {
        when(sponsorService.findOrThrow(1L)).thenReturn(sponsor);
        when(packService.findOrThrow(2L)).thenReturn(pack);

        assertThrows(BusinessException.class, () -> conventionService.solliciter(request(900.0)));
        verify(conventionRepository, never()).save(any());
    }

    @Test
    void solliciterBloqueSiEvenementNonPublie() {
        when(sponsorService.findOrThrow(1L)).thenReturn(sponsor);
        when(packService.findOrThrow(2L)).thenReturn(pack);
        doThrow(new BusinessException("L'evenement doit etre au statut PUBLIE"))
                .when(evenementGateway).verifierEvenementPublie(10L);

        assertThrows(BusinessException.class, () -> conventionService.solliciter(request(null)));
        verify(conventionRepository, never()).save(any());
    }

    @Test
    void souscrireRefuseUnDoublonActif() {
        when(sponsorService.findOrThrow(1L)).thenReturn(sponsor);
        when(packService.findOrThrow(2L)).thenReturn(pack);
        when(conventionRepository.existsBySponsor_IdAndEvenementIdAndStatutPaiementNot(1L, 10L, StatutPaiement.REFUSEE))
                .thenReturn(true);

        assertThrows(ConflictException.class, () -> conventionService.souscrire(request(null)));
    }

    @Test
    void traiterAcceptePuisValiderPublieConventionValidee() {
        var convention = new com.warriors.sponsors.entity.ConventionPartenariat();
        convention.setId(5L);
        convention.setSponsor(sponsor);
        convention.setPack(pack);
        convention.setEvenementId(10L);
        convention.setBudgetAlloue(1500.0);
        convention.setStatutPaiement(StatutPaiement.EN_ATTENTE);
        when(conventionRepository.findById(5L)).thenReturn(Optional.of(convention));
        when(conventionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ConventionResponse acceptee = conventionService.traiter(5L, DecisionSollicitation.ACCEPTER);
        assertEquals(StatutPaiement.ACCEPTEE, acceptee.getStatutPaiement());

        ConventionResponse payee = conventionService.valider(5L);
        assertEquals(StatutPaiement.PAYE, payee.getStatutPaiement());

        ArgumentCaptor<ConventionValideeMessage> captor = ArgumentCaptor.forClass(ConventionValideeMessage.class);
        verify(conventionPublisher).publier(captor.capture());
        assertEquals(5L, captor.getValue().getConventionId());
        assertEquals(true, captor.getValue().getStandInclus());
        assertEquals("Gold", captor.getValue().getNomPack());
    }

    @Test
    void validerRefuseUneSollicitationEncoreEnAttente() {
        var convention = new com.warriors.sponsors.entity.ConventionPartenariat();
        convention.setId(5L);
        convention.setStatutPaiement(StatutPaiement.EN_ATTENTE);
        when(conventionRepository.findById(5L)).thenReturn(Optional.of(convention));

        assertThrows(BusinessException.class, () -> conventionService.valider(5L));
        verify(conventionPublisher, never()).publier(any());
    }

    private ConventionRequest request(Double budget) {
        ConventionRequest request = new ConventionRequest();
        request.setSponsorId(1L);
        request.setPackId(2L);
        request.setEvenementId(10L);
        request.setBudgetAlloue(budget);
        return request;
    }
}
