package com.universite.evenements.repository;

import com.universite.evenements.entity.Evenement;
import com.universite.evenements.enums.StatutEvenement;
import com.universite.evenements.enums.TypeEvenement;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    List<Evenement> findByType(TypeEvenement type);

    List<Evenement> findByStatut(StatutEvenement statut);

    List<Evenement> findByTypeAndStatut(TypeEvenement type, StatutEvenement statut);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Evenement e WHERE e.id = :id")
    Optional<Evenement> findByIdWithLock(@Param("id") Long id);
}
