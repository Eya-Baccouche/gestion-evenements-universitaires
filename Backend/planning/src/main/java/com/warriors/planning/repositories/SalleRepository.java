package com.warriors.planning.repositories;

import com.warriors.planning.entities.Salle;
import com.warriors.planning.entities.enums.StatutSalle;
import com.warriors.planning.entities.enums.TypeSalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Long> {

    List<Salle> findByBatimentId(Long batimentId);

    List<Salle> findByTypeSalleAndStatut(TypeSalle typeSalle, StatutSalle statut);

    List<Salle> findByStatut(StatutSalle statut);

    boolean existsByNomAndBatimentId(String nom, Long batimentId);

    /**
     * Recherche de salles disponibles répondant à des critères.
     * Retourne les salles DISPONIBLE, de capacité suffisante, du type demandé,
     * n'ayant aucune réservation active qui chevauche le créneau donné.
     */
    @Query("""
        SELECT s FROM Salle s
        WHERE s.statut = com.warriors.planning.entities.enums.StatutSalle.DISPONIBLE
          AND (:typeSalle IS NULL OR s.typeSalle = :typeSalle)
          AND (:capaciteMin IS NULL OR s.capacite >= :capaciteMin)
          AND (:batimentId IS NULL OR s.batiment.id = :batimentId)
          AND NOT EXISTS (
              SELECT r FROM ReservationSalle r
              WHERE r.salle.id = s.id
                AND r.dateReservation = :date
                AND r.statut IN (com.warriors.planning.entities.enums.StatutReservation.EN_ATTENTE,
                                 com.warriors.planning.entities.enums.StatutReservation.CONFIRMEE)
                AND r.heureDebut < :heureFin
                AND r.heureFin   > :heureDebut
          )
        ORDER BY s.capacite ASC
    """)
    List<Salle> findSallesDisponibles(
            @Param("typeSalle") TypeSalle typeSalle,
            @Param("capaciteMin") Integer capaciteMin,
            @Param("batimentId") Long batimentId,
            @Param("date") LocalDate date,
            @Param("heureDebut") LocalTime heureDebut,
            @Param("heureFin") LocalTime heureFin);
}
