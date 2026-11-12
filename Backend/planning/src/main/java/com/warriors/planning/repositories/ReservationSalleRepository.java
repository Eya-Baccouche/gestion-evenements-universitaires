package com.warriors.planning.repositories;

import com.warriors.planning.entities.ReservationSalle;
import com.warriors.planning.entities.enums.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationSalleRepository extends JpaRepository<ReservationSalle, Long> {

    List<ReservationSalle> findByDemandePar(String demandePar);

    List<ReservationSalle> findByStatut(StatutReservation statut);

    List<ReservationSalle> findBySalleId(Long salleId);

    List<ReservationSalle> findBySalleIdAndDateReservation(Long salleId, LocalDate date);

    /**
     * Vérifie l'existence d'un chevauchement de créneau pour une salle donnée.
     * Utilisé par la règle métier R-S1.
     */
    @Query("""
        SELECT COUNT(r) > 0 FROM ReservationSalle r
        WHERE r.salle.id = :salleId
          AND r.dateReservation = :date
          AND r.statut IN (com.warriors.planning.entities.enums.StatutReservation.EN_ATTENTE,
                           com.warriors.planning.entities.enums.StatutReservation.CONFIRMEE)
          AND (:excludeId IS NULL OR r.id <> :excludeId)
          AND r.heureDebut < :heureFin
          AND r.heureFin   > :heureDebut
    """)
    boolean existsChevauchement(
            @Param("salleId") Long salleId,
            @Param("date") LocalDate date,
            @Param("heureDebut") java.time.LocalTime heureDebut,
            @Param("heureFin") java.time.LocalTime heureFin,
            @Param("excludeId") Long excludeId);

    /**
     * Réservations d'une salle sur une période (planning).
     */
    @Query("""
        SELECT r FROM ReservationSalle r
        WHERE r.salle.id = :salleId
          AND r.dateReservation BETWEEN :dateDebut AND :dateFin
        ORDER BY r.dateReservation ASC, r.heureDebut ASC
    """)
    List<ReservationSalle> findPlanningSalle(
            @Param("salleId") Long salleId,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);

    /**
     * Réservations filtrées (admin) — tous les critères optionnels.
     */
    @Query("""
        SELECT r FROM ReservationSalle r
        WHERE (:statut IS NULL OR r.statut = :statut)
          AND (:salleId IS NULL OR r.salle.id = :salleId)
          AND (:demandePar IS NULL OR r.demandePar = :demandePar)
          AND (:dateDebut IS NULL OR r.dateReservation >= :dateDebut)
          AND (:dateFin IS NULL OR r.dateReservation <= :dateFin)
        ORDER BY r.dateReservation DESC, r.heureDebut ASC
    """)
    List<ReservationSalle> findReservationsFiltrees(
            @Param("statut") StatutReservation statut,
            @Param("salleId") Long salleId,
            @Param("demandePar") String demandePar,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);
}
