package com.warriors.feedback.intervenants.repository;

import com.warriors.feedback.intervenants.model.Intervenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntervenantRepository extends JpaRepository<Intervenant, Long> {
}
