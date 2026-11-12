package com.warriors.planning.repositories;

import com.warriors.planning.entities.Batiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BatimentRepository extends JpaRepository<Batiment, Long> {

    boolean existsByNom(String nom);

    Optional<Batiment> findByNom(String nom);
}
