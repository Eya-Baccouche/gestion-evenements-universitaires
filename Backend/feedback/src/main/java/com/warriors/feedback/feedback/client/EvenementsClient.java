package com.warriors.feedback.feedback.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "evenements", url = "${events.service.url}")
public interface EvenementsClient {

    @GetMapping("/events/{id}")
    EvenementDto getEvenement(@PathVariable("id") Long id);
}
