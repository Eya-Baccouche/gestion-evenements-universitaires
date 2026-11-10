package com.warriors.statistiques.repository;

import com.warriors.statistiques.entity.StatEvenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatEvenementRepository extends JpaRepository<StatEvenement, Long> {
    Optional<StatEvenement> findByEvenementId(Long evenementId);
    void deleteByEvenementId(Long evenementId);
}
