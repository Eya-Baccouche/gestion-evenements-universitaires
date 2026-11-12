package com.warriors.statistiques.service.impl;

import com.warriors.statistiques.entity.Instantane;
import com.warriors.statistiques.entity.StatEvenement;
import com.warriors.statistiques.repository.InstantaneRepository;
import com.warriors.statistiques.repository.StatEvenementRepository;
import com.warriors.statistiques.service.StatistiquesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StatistiquesServiceImpl implements StatistiquesService {

    @Autowired
    private StatEvenementRepository statEvenementRepository;

    @Autowired
    private InstantaneRepository instantaneRepository;

    @Override
    public List<StatEvenement> getAll(LocalDate from, LocalDate to) {
        if (from != null && to != null) {
            return statEvenementRepository.findByDateEvenementBetween(from, to);
        }
        return statEvenementRepository.findAll();
    }

    @Override
    public Optional<StatEvenement> getByEvenementId(Long evenementId) {
        return statEvenementRepository.findByEvenementId(evenementId);
    }

    @Override
    public StatEvenement create(StatEvenement stat) {
        stat.calculerTauxPresence();
        StatEvenement saved = statEvenementRepository.save(stat);

        // Enregistrer un instantane initial
        Instantane instantane = new Instantane();
        instantane.setEvenementId(saved.getEvenementId());
        instantane.setDate(LocalDateTime.now());
        instantane.setInscrits(saved.getInscrits());
        instantane.setPresents(saved.getPresents());
        instantaneRepository.save(instantane);

        return saved;
    }

    @Override
    public StatEvenement update(Long evenementId, StatEvenement newStat) {
        Optional<StatEvenement> optional = statEvenementRepository.findByEvenementId(evenementId);
        if (optional.isPresent()) {
            StatEvenement stat = optional.get();
            if (newStat.getTitre() != null) stat.setTitre(newStat.getTitre());
            if (newStat.getDateEvenement() != null) stat.setDateEvenement(newStat.getDateEvenement());
            if (newStat.getStatut() != null) stat.setStatut(newStat.getStatut());
            if (newStat.getInscrits() != null) stat.setInscrits(newStat.getInscrits());
            if (newStat.getPresents() != null) stat.setPresents(newStat.getPresents());
            if (newStat.getNoteMoyenne() != null) stat.setNoteMoyenne(newStat.getNoteMoyenne());
            if (newStat.getNbAvis() != null) stat.setNbAvis(newStat.getNbAvis());
            stat.calculerTauxPresence();

            StatEvenement updated = statEvenementRepository.save(stat);

            // Nouvel instantane lors de la mise a jour
            Instantane instantane = new Instantane();
            instantane.setEvenementId(updated.getEvenementId());
            instantane.setDate(LocalDateTime.now());
            instantane.setInscrits(updated.getInscrits());
            instantane.setPresents(updated.getPresents());
            instantaneRepository.save(instantane);

            return updated;
        }
        return null;
    }

    @Override
    public boolean delete(Long evenementId) {
        Optional<StatEvenement> optional = statEvenementRepository.findByEvenementId(evenementId);
        if (optional.isPresent()) {
            statEvenementRepository.delete(optional.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Instantane> getTimeline(Long evenementId) {
        return instantaneRepository.findByEvenementIdOrderByDateAsc(evenementId);
    }

    @Override
    public Instantane addInstantane(Instantane instantane) {
        if (instantane.getDate() == null) {
            instantane.setDate(LocalDateTime.now());
        }
        return instantaneRepository.save(instantane);
    }

    @Override
    public Map<String, Object> getOverview() {
        List<StatEvenement> list = statEvenementRepository.findAll();
        long totalEvenements = list.size();
        long totalInscrits = 0;
        long totalPresents = 0;
        double sumNotes = 0;
        long totalAvis = 0;
        int countWithNotes = 0;

        for (StatEvenement s : list) {
            if (s.getInscrits() != null) totalInscrits += s.getInscrits();
            if (s.getPresents() != null) totalPresents += s.getPresents();
            if (s.getNbAvis() != null) totalAvis += s.getNbAvis();
            if (s.getNoteMoyenne() != null && s.getNbAvis() != null && s.getNbAvis() > 0) {
                sumNotes += s.getNoteMoyenne();
                countWithNotes++;
            }
        }

        double tauxGlobal = totalInscrits > 0 ? ((double) totalPresents / totalInscrits) * 100.0 : 0.0;
        double noteGlobale = countWithNotes > 0 ? sumNotes / countWithNotes : 0.0;

        Map<String, Object> overview = new HashMap<>();
        overview.put("totalEvenements", totalEvenements);
        overview.put("totalInscrits", totalInscrits);
        overview.put("totalPresents", totalPresents);
        overview.put("tauxPresenceGlobal", Math.round(tauxGlobal * 100.0) / 100.0);
        overview.put("noteMoyenneGlobale", Math.round(noteGlobale * 100.0) / 100.0);
        overview.put("totalAvis", totalAvis);

        return overview;
    }
}

