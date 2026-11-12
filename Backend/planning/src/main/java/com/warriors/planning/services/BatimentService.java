package com.warriors.planning.services;

import com.warriors.planning.dto.BatimentDTO;
import com.warriors.planning.entities.Batiment;
import com.warriors.planning.exceptions.BusinessException;
import com.warriors.planning.exceptions.ResourceNotFoundException;
import com.warriors.planning.repositories.BatimentRepository;
import com.warriors.planning.repositories.SalleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service métier des bâtiments (rôle ADMIN).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BatimentService {

    private final BatimentRepository batimentRepository;
    private final SalleRepository salleRepository;

    @Transactional(readOnly = true)
    public List<BatimentDTO> findAll() {
        return batimentRepository.findAll().stream()
                .map(BatimentDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public BatimentDTO findById(Long id) {
        return batimentRepository.findById(id)
                .map(BatimentDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bâtiment introuvable avec l'id " + id));
    }

    public BatimentDTO create(BatimentDTO dto) {
        if (batimentRepository.existsByNom(dto.getNom())) {
            throw new BusinessException("Un bâtiment avec le nom '" + dto.getNom() + "' existe déjà");
        }
        Batiment b = Batiment.builder()
                .nom(dto.getNom())
                .adresse(dto.getAdresse())
                .nbEtages(dto.getNbEtages())
                .build();
        return BatimentDTO.fromEntity(batimentRepository.save(b));
    }

    public BatimentDTO update(Long id, BatimentDTO dto) {
        Batiment b = batimentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bâtiment introuvable avec l'id " + id));

        // Vérifie l'unicité du nom si on le change
        if (!b.getNom().equals(dto.getNom()) && batimentRepository.existsByNom(dto.getNom())) {
            throw new BusinessException("Un bâtiment avec le nom '" + dto.getNom() + "' existe déjà");
        }

        b.setNom(dto.getNom());
        b.setAdresse(dto.getAdresse());
        b.setNbEtages(dto.getNbEtages());
        return BatimentDTO.fromEntity(batimentRepository.save(b));
    }

    public void delete(Long id) {
        Batiment b = batimentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bâtiment introuvable avec l'id " + id));

        // R-A7 (variante) : on ne supprime pas un bâtiment contenant des salles
        if (!salleRepository.findByBatimentId(id).isEmpty()) {
            throw new BusinessException(
                    "Impossible de supprimer le bâtiment : des salles y sont encore rattachées");
        }
        batimentRepository.delete(b);
    }
}
