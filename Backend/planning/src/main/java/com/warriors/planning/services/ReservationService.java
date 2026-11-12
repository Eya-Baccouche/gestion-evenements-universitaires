package com.warriors.planning.services;

import com.warriors.planning.dto.ReservationCreateDTO;
import com.warriors.planning.dto.ReservationDTO;
import com.warriors.planning.entities.ReservationSalle;
import com.warriors.planning.entities.Salle;
import com.warriors.planning.entities.enums.RoleDemandeur;
import com.warriors.planning.entities.enums.StatutReservation;
import com.warriors.planning.entities.enums.StatutSalle;
import com.warriors.planning.exceptions.BusinessException;
import com.warriors.planning.exceptions.ResourceNotFoundException;
import com.warriors.planning.repositories.ReservationSalleRepository;
import com.warriors.planning.repositories.SalleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service métier des réservations.
 *
 * Règles simples : R-S1 (anti-chevauchement), R-S2 (heureFin > heureDebut),
 *                  R-S3 (date future), R-S4 (capacité), R-S5 (salle disponible).
 * Règles admin    : R-A3 (valider/refuser), R-A4 (annuler confirmée), R-A5 (consultation).
 * Règles orga     : R-O1 (créer), R-O2 (modifier si EN_ATTENTE), R-O3 (annuler), R-O4 (consulter).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationSalleRepository reservationRepository;
    private final SalleRepository salleRepository;

    // ================================================================
    // Lecture
    // ================================================================

    @Transactional(readOnly = true)
    public List<ReservationDTO> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReservationDTO findById(Long id) {
        return reservationRepository.findById(id)
                .map(ReservationDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Réservation introuvable avec l'id " + id));
    }

    // ---------- R-O4 : mes réservations ----------
    @Transactional(readOnly = true)
    public List<ReservationDTO> findByDemandeur(String demandePar) {
        return reservationRepository.findByDemandePar(demandePar).stream()
                .map(ReservationDTO::fromEntity)
                .toList();
    }

    // ---------- R-A5 : réservations filtrées (admin) ----------
    @Transactional(readOnly = true)
    public List<ReservationDTO> findFiltrees(StatutReservation statut,
                                            Long salleId,
                                            String demandePar,
                                            LocalDate dateDebut,
                                            LocalDate dateFin) {
        return reservationRepository.findReservationsFiltrees(
                        statut, salleId, demandePar, dateDebut, dateFin).stream()
                .map(ReservationDTO::fromEntity)
                .toList();
    }

    // ---------- R-O6 : planning d'une salle ----------
    @Transactional(readOnly = true)
    public List<ReservationDTO> findPlanningSalle(Long salleId, LocalDate dateDebut, LocalDate dateFin) {
        return reservationRepository.findPlanningSalle(salleId, dateDebut, dateFin).stream()
                .map(ReservationDTO::fromEntity)
                .toList();
    }

    // ================================================================
    // R-O1 : Création d'une réservation
    // ================================================================
    public ReservationDTO create(ReservationCreateDTO dto) {
        // R-S2 : heureFin > heureDebut
        if (!dto.getHeureFin().isAfter(dto.getHeureDebut())) {
            throw new BusinessException("L'heure de fin doit être après l'heure de début");
        }

        Salle salle = salleRepository.findById(dto.getSalleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Salle introuvable avec l'id " + dto.getSalleId()));

        // R-S5 : la salle doit être DISPONIBLE
        if (salle.getStatut() != StatutSalle.DISPONIBLE) {
            throw new BusinessException(
                    "La salle '" + salle.getNom() + "' n'est pas disponible (statut: "
                            + salle.getStatut() + ")");
        }

        // R-S4 : capacité suffisante
        if (dto.getNombrePersonnes() > salle.getCapacite()) {
            throw new BusinessException(
                    "La salle '" + salle.getNom() + "' a une capacité de "
                            + salle.getCapacite() + " personnes, insuffisante pour "
                            + dto.getNombrePersonnes() + " personnes");
        }

        // R-S1 : pas de chevauchement de créneau
        if (reservationRepository.existsChevauchement(
                dto.getSalleId(), dto.getDateReservation(),
                dto.getHeureDebut(), dto.getHeureFin(), null)) {
            throw new BusinessException(
                    "Conflit de créneau : la salle '" + salle.getNom()
                            + "' est déjà réservée le " + dto.getDateReservation()
                            + " entre " + dto.getHeureDebut() + " et " + dto.getHeureFin());
        }

        ReservationSalle reservation = ReservationSalle.builder()
                .salle(salle)
                .titre(dto.getTitre())
                .description(dto.getDescription())
                .dateReservation(dto.getDateReservation())
                .heureDebut(dto.getHeureDebut())
                .heureFin(dto.getHeureFin())
                .statut(StatutReservation.EN_ATTENTE)
                .demandePar(dto.getDemandePar())
                .roleDemandeur(dto.getRoleDemandeur())
                .evenementId(dto.getEvenementId())
                .nombrePersonnes(dto.getNombrePersonnes())
                .build();

        return ReservationDTO.fromEntity(reservationRepository.save(reservation));
    }

    // ================================================================
    // R-O2 : Modification (organisateur, uniquement si EN_ATTENTE)
    // ================================================================
    public ReservationDTO update(Long id, ReservationCreateDTO dto) {
        ReservationSalle r = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Réservation introuvable avec l'id " + id));

        // R-O2 : ne peut modifier que si EN_ATTENTE
        if (r.getStatut() != StatutReservation.EN_ATTENTE) {
            throw new BusinessException(
                    "Une réservation ne peut être modifiée que si elle est EN_ATTENTE (statut actuel: "
                            + r.getStatut() + ")");
        }

        // R-S2
        if (!dto.getHeureFin().isAfter(dto.getHeureDebut())) {
            throw new BusinessException("L'heure de fin doit être après l'heure de début");
        }

        Salle salle = salleRepository.findById(dto.getSalleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Salle introuvable avec l'id " + dto.getSalleId()));

        // R-S5
        if (salle.getStatut() != StatutSalle.DISPONIBLE) {
            throw new BusinessException(
                    "La salle '" + salle.getNom() + "' n'est pas disponible (statut: "
                            + salle.getStatut() + ")");
        }

        // R-S4
        if (dto.getNombrePersonnes() > salle.getCapacite()) {
            throw new BusinessException(
                    "Capacité insuffisante: la salle '" + salle.getNom()
                            + "' accueille " + salle.getCapacite()
                            + " personnes max (demandé: " + dto.getNombrePersonnes() + ")");
        }

        // R-S1 : on exclut la réservation courante du contrôle de chevauchement
        if (reservationRepository.existsChevauchement(
                dto.getSalleId(), dto.getDateReservation(),
                dto.getHeureDebut(), dto.getHeureFin(), id)) {
            throw new BusinessException(
                    "Conflit de créneau pour la salle '" + salle.getNom() + "'");
        }

        r.setSalle(salle);
        r.setTitre(dto.getTitre());
        r.setDescription(dto.getDescription());
        r.setDateReservation(dto.getDateReservation());
        r.setHeureDebut(dto.getHeureDebut());
        r.setHeureFin(dto.getHeureFin());
        r.setNombrePersonnes(dto.getNombrePersonnes());
        if (dto.getEvenementId() != null) {
            r.setEvenementId(dto.getEvenementId());
        }
        return ReservationDTO.fromEntity(reservationRepository.save(r));
    }

    // ================================================================
    // R-O3 / R-A4 : Annulation
    // ================================================================
    public ReservationDTO annuler(Long id) {
        ReservationSalle r = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Réservation introuvable avec l'id " + id));

        if (r.getStatut() == StatutReservation.ANNULEE) {
            throw new BusinessException("La réservation est déjà annulée");
        }
        if (r.getStatut() == StatutReservation.REFUSEE) {
            throw new BusinessException("Une réservation refusée ne peut pas être annulée");
        }

        r.setStatut(StatutReservation.ANNULEE);
        return ReservationDTO.fromEntity(reservationRepository.save(r));
    }

    // ================================================================
    // R-A3 : Validation par l'admin (EN_ATTENTE -> CONFIRMEE)
    // ================================================================
    public ReservationDTO valider(Long id) {
        ReservationSalle r = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Réservation introuvable avec l'id " + id));

        if (r.getStatut() != StatutReservation.EN_ATTENTE) {
            throw new BusinessException(
                    "Seules les réservations EN_ATTENTE peuvent être validées (statut actuel: "
                            + r.getStatut() + ")");
        }

        // Re-vérification du chevauchement (une autre réservation a pu être confirmée)
        if (reservationRepository.existsChevauchement(
                r.getSalle().getId(), r.getDateReservation(),
                r.getHeureDebut(), r.getHeureFin(), id)) {
            throw new BusinessException(
                    "Conflit de créneau détecté : une autre réservation occupe déjà ce créneau");
        }

        r.setStatut(StatutReservation.CONFIRMEE);
        return ReservationDTO.fromEntity(reservationRepository.save(r));
    }

    // ================================================================
    // R-A3 : Refus par l'admin (EN_ATTENTE -> REFUSEE, motif obligatoire)
    // ================================================================
    public ReservationDTO refuser(Long id, String motif) {
        ReservationSalle r = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Réservation introuvable avec l'id " + id));

        if (r.getStatut() != StatutReservation.EN_ATTENTE) {
            throw new BusinessException(
                    "Seules les réservations EN_ATTENTE peuvent être refusées (statut actuel: "
                            + r.getStatut() + ")");
        }

        if (motif == null || motif.isBlank()) {
            throw new BusinessException("Le motif de refus est obligatoire");
        }

        r.setStatut(StatutReservation.REFUSEE);
        r.setMotifRefus(motif);
        return ReservationDTO.fromEntity(reservationRepository.save(r));
    }
}
