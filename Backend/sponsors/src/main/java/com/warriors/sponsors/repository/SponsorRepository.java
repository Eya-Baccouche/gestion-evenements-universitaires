package com.warriors.sponsors.repository;

import com.warriors.sponsors.entity.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SponsorRepository extends JpaRepository<Sponsor, Long> {

    boolean existsByContactEmail(String contactEmail);

    boolean existsByContactEmailAndIdNot(String contactEmail, Long id);
}
