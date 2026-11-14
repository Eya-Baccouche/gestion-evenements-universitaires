package com.warriors.sponsors.repository;

import com.warriors.sponsors.entity.PackSponsoring;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackSponsoringRepository extends JpaRepository<PackSponsoring, Long> {

    boolean existsByNomPack(String nomPack);

    boolean existsByNomPackAndIdNot(String nomPack, Long id);
}
