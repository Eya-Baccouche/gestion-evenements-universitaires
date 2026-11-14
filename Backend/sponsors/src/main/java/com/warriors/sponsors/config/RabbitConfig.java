package com.warriors.sponsors.config;

import com.warriors.sponsors.messaging.ConventionPublisher;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "sponsors.messaging.enabled", havingValue = "true", matchIfMissing = true)
public class RabbitConfig {

    public static final String EXCHANGE = "evenements.topic";
    public static final String ROUTING_CONVENTION_VALIDEE = "convention.validee";
    public static final String ROUTING_MATERIEL_RESTITUE = "materiel.restitue";
    public static final String QUEUE_MATERIEL_RESTITUE = "sponsors.materiel.restitue";

    @Bean
    public TopicExchange evenementsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue materielRestitueQueue() {
        return new Queue(QUEUE_MATERIEL_RESTITUE, true);
    }

    @Bean
    public Binding materielRestitueBinding(Queue materielRestitueQueue, TopicExchange evenementsExchange) {
        return BindingBuilder.bind(materielRestitueQueue)
                .to(evenementsExchange)
                .with(ROUTING_MATERIEL_RESTITUE);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public ConventionPublisher conventionPublisher(RabbitTemplate rabbitTemplate) {
        return message -> rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_CONVENTION_VALIDEE, message);
    }
}
