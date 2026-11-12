package com.warriors.planning.services;

import com.warriors.planning.dto.SalleDTO;
import com.warriors.planning.dto.StatutDTO;
import com.warriors.planning.entities.Batiment;
import com.warriors.planning.entities.Salle;
import com.warriors.planning.entities.enums.StatutSalle;
import com.warriors.planning.entities.enums.TypeSalle;
import com.warriors.planning.exceptions.BusinessException;
import com.warriors.planning.exceptions.ResourceNotFoundException;
import com.warriors.planning.repositories.BatimentRepository;
import com.warriors.planning.repositories.ReservationSalleRepository;
import com.warriors.planning.repositories.SalleRepository;
import com.warriors.planning.entities.enums.StatutReservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Service métier des salles.
 * Règles : R-A1 (CRUD), R-A2 (changement statut), R-A7 (suppression),
 *          R-O5 (recherche disponibilité), R-S5 (salle disponible).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SalleService {

    private final SalleRepository salleRepository;
    private final BatimentRepository batimentRepository;
    private final ReservationSalleRepository reservationRepository;

    @Transactional(readOnly = true)
    public List<SalleDTO> findAll() {
        return salleRepository.findAll().stream()
                .map(SalleDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SalleDTO> findByFiltres(TypeSalle type, StatutSalle statut, Long batimentId) {
        return salleRepository.findAll().stream()
                .filter(s -> type == null || s.getTypeSalle() == type)
                .filter(s -> statut == null || s.getStatut() == statut)
                .filter(s -> batimentId == null || s.getBatiment().getId().equals(batimentId))
                .map(SalleDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public SalleDTO findById(Long id) {
        return salleRepository.findById(id)
                .map(SalleDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Salle introuvable avec l'id " + id));
    }

    // ---------- R-O5 : recherche de salles disponibles ----------
    @Transactional(readOnly = true)
    public List<SalleDTO> findSallesDisponibles(TypeSalle typeSalle,
                                                Integer capaciteMin,
                                                Long batimentId,
                                                LocalDate date,
                                                LocalTime heureDebut,
                                                LocalTime heureFin) {
        if (date == null || heureDebut == null || heureFin == null) {
            throw new BusinessException(
                    "Date, heure de début et heure de fin sont obligatoires pour la recherche de disponibilité");
        }
        if (!heureFin.isAfter(heureDebut)) {
            throw new BusinessException("L'heure de fin doit être après l'heure de début");
        }
        return salleRepository.findSallesDisponibles(
                        typeSalle, capaciteMin, batimentId, date, heureDebut, heureFin)
                .stream()
                .map(SalleDTO::fromEntity)
                .toList();
    }

    // ---------- R-A1 : création ----------
    public SalleDTO create(SalleDTO dto) {
        Batiment batiment = batimentRepository.findById(dto.getBatimentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bâtiment introuvable avec l'id " + dto.getBatimentId()));

        if (salleRepository.existsByNomAndBatimentId(dto.getNom(), dto.getBatimentId())) {
            throw new BusinessException("Une salle '" + dto.getNom()
                    + "' existe déjà dans ce bâtiment");
        }

        Salle salle = Salle.builder()
                .nom(dto.getNom())
                .typeSalle(dto.getTypeSalle())
                .capacite(dto.getCapacite())
                .etage(dto.getEtage())
                .batiment(batiment)
                .equipements(dto.getEquipements())
                .statut(dto.getStatut() != null ? dto.getStatut() : StatutSalle.DISPONIBLE)
                .description(dto.getDescription())
                .build();
        return SalleDTO.fromEntity(salleRepository.save(salle));
    }

    // ---------- R-A1 : modification ----------
    public SalleDTO update(Long id, SalleDTO dto) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Salle introuvable avec l'id " + id));

        if (!salle.getNom().equals(dto.getNom())
                && salleRepository.existsByNomAndBatimentId(dto.getNom(),
                        dto.getBatimentId() != null ? dto.getBatimentId()
                                : salle.getBatiment().getId())) {
            throw new BusinessException("Une salle '" + dto.getNom()
                    + "' existe déjà dans ce bâtiment");
        }

        if (dto.getBatimentId() != null
                && !dto.getBatimentId().equals(salle.getBatiment().getId())) {
            Batiment batiment = batimentRepository.findById(dto.getBatimentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Bâtiment introuvable avec l'id " + dto.getBatimentId()));
            salle.setBatiment(batiment);
        }

        salle.setNom(dto.getNom());
        salle.setTypeSalle(dto.getTypeSalle());
        salle.setCapacite(dto.getCapacite());
        salle.setEtage(dto.getEtage());
        salle.setEquipements(dto.getEquipements());
        if (dto.getStatut() != null) {
            salle.setStatut(dto.getStatut());
        }
        salle.setDescription(dto.getDescription());
        return SalleDTO.fromEntity(salleRepository.save(salle));
    }

    // ---------- R-A7 : suppression (pas de réservations actives) ----------
    public void delete(Long id) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Salle introuvable avec l'id " + id));

        boolean hasReservationsActives = reservationRepository.findBySalleId(id).stream()
                .anyMatch(r -> r.getStatut() == StatutReservation.EN_ATTENTE
                        || r.getStatut() == StatutReservation.CONFIRMEE);

        if (hasReservationsActives) {
            throw new BusinessException(
                    "Impossible de supprimer la salle : des réservations actives (EN_ATTENTE ou CONFIRMEE) existent");
        }
        salleRepository.delete(salle);
    }

    // ---------- R-A2 : changement de statut ----------
    public SalleDTO changerStatut(Long id, StatutDTO dto) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Salle introuvable avec l'id " + id));
        salle.setStatut(dto.getStatut());
        return SalleDTO.fromEntity(salleRepository.save(salle));
    }
}
