package com.warriors.feedback.feedback.repository;

import com.warriors.feedback.feedback.model.Avis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvisRepository extends JpaRepository<Avis, Long> {
    List<Avis> findByEvenementId(Long evenementId);
    boolean existsByEvenementIdAndParticipantId(Long evenementId, Long participantId);
}
