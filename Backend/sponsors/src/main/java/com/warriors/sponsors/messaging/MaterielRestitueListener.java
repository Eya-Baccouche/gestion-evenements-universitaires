package com.warriors.sponsors.messaging;

import com.warriors.sponsors.config.RabbitConfig;
import com.warriors.sponsors.entity.ConventionPartenariat;
import com.warriors.sponsors.repository.ConventionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "sponsors.messaging.enabled", havingValue = "true", matchIfMissing = true)
public class MaterielRestitueListener {

    private static final Logger log = LoggerFactory.getLogger(MaterielRestitueListener.class);

    private final ConventionRepository conventionRepository;

    @RabbitListener(queues = RabbitConfig.QUEUE_MATERIEL_RESTITUE)
    public void onMaterielRestitue(MaterielRestitueMessage message) {
        if (message == null || message.getEvenementId() == null) {
            log.warn("Message materiel.restitue ignore : evenementId manquant");
            return;
        }
        int conventions = conventionRepository.findByEvenementId(message.getEvenementId()).size();
        log.info(
                "Materiel {} restitue pour l'evenement {} : {} convention(s) de partenariat concernee(s)",
                message.getEquipementId(),
                message.getEvenementId(),
                conventions
        );
    }
}
