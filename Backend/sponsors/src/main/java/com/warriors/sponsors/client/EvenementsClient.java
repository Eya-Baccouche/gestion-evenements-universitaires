package com.warriors.sponsors.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "evenements", url = "${evenements.service.url}")
public interface EvenementsClient {

    @GetMapping("/events/{id}")
    EvenementClientDto getEvenement(@PathVariable("id") Long id);
}
