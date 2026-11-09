package com.warriors.feedback.intervenants.repository;

import com.warriors.feedback.intervenants.model.Affectation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AffectationRepository extends JpaRepository<Affectation, Long> {
    List<Affectation> findByIntervenantId(Long intervenantId);
    boolean existsByIntervenantIdAndEvenementId(Long intervenantId, Long evenementId);
}
