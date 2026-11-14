package com.warriors.sponsors.repository;

import com.warriors.sponsors.entity.ConventionPartenariat;
import com.warriors.sponsors.enums.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConventionRepository extends JpaRepository<ConventionPartenariat, Long> {

    boolean existsBySponsor_IdAndEvenementIdAndStatutPaiementNot(
            Long sponsorId, Long evenementId, StatutPaiement statut);

    boolean existsBySponsor_Id(Long sponsorId);

    boolean existsByPack_Id(Long packId);

    List<ConventionPartenariat> findByEvenementId(Long evenementId);

    List<ConventionPartenariat> findBySponsor_Id(Long sponsorId);
}
