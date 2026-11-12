package com.warriors.statistiques.service;

import com.warriors.statistiques.entity.Instantane;
import com.warriors.statistiques.entity.StatEvenement;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StatistiquesService {

    List<StatEvenement> getAll(LocalDate from, LocalDate to);

    Optional<StatEvenement> getByEvenementId(Long evenementId);

    StatEvenement create(StatEvenement stat);

    StatEvenement update(Long evenementId, StatEvenement newStat);

    boolean delete(Long evenementId);

    List<Instantane> getTimeline(Long evenementId);

    Instantane addInstantane(Instantane instantane);

    Map<String, Object> getOverview();
}
