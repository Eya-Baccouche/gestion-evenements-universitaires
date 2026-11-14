package com.warriors.sponsors.config;

import com.warriors.sponsors.messaging.ConventionPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "sponsors.messaging.enabled", havingValue = "false")
public class NoMessagingConfig {

    private static final Logger log = LoggerFactory.getLogger(NoMessagingConfig.class);

    @Bean
    public ConventionPublisher conventionPublisher() {
        return message -> log.info("RabbitMQ desactive, convention.validee non publiee : {}", message.getConventionId());
    }
}
