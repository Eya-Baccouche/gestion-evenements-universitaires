package com.warriors.statistiques.repository;

import com.warriors.statistiques.entity.Instantane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstantaneRepository extends JpaRepository<Instantane, Long> {
    List<Instantane> findByEvenementIdOrderByDateAsc(Long evenementId);
}
