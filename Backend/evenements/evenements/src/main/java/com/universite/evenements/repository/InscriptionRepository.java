package com.universite.evenements.repository;

import com.universite.evenements.entity.Inscription;
import com.universite.evenements.enums.StatutInscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    boolean existsByEvenementIdAndParticipantId(Long evenementId, Long participantId);

    long countByEvenementIdAndStatut(Long evenementId, StatutInscription statut);

    List<Inscription> findByEvenementId(Long evenementId);

    Optional<Inscription> findByEvenementIdAndParticipantId(Long evenementId, Long participantId);
}
