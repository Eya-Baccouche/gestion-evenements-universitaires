package com.warriors.sponsors.client;

import com.warriors.sponsors.exception.BusinessException;
import com.warriors.sponsors.exception.NotFoundException;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EvenementGatewayTest {

    @Mock
    private EvenementsClient evenementsClient;

    @InjectMocks
    private EvenementGateway evenementGateway;

    @Test
    void accepteUnEvenementPublie() {
        EvenementClientDto dto = new EvenementClientDto();
        dto.setId(10L);
        dto.setStatut("PUBLIE");
        when(evenementsClient.getEvenement(10L)).thenReturn(dto);

        assertDoesNotThrow(() -> evenementGateway.verifierEvenementPublie(10L));
    }

    @Test
    void refuseUnEvenementBrouillon() {
        EvenementClientDto dto = new EvenementClientDto();
        dto.setId(10L);
        dto.setStatut("BROUILLON");
        when(evenementsClient.getEvenement(10L)).thenReturn(dto);

        assertThrows(BusinessException.class, () -> evenementGateway.verifierEvenementPublie(10L));
    }

    @Test
    void evenementInconnu() {
        Request request = Request.create(
                Request.HttpMethod.GET,
                "http://localhost:8081/evenements/events/99",
                Collections.emptyMap(),
                null,
                new RequestTemplate());
        when(evenementsClient.getEvenement(99L))
                .thenThrow(new FeignException.NotFound("introuvable", request, null, Collections.emptyMap()));

        assertThrows(NotFoundException.class, () -> evenementGateway.verifierEvenementPublie(99L));
    }
}
