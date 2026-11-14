package com.warriors.sponsors.service;

import com.warriors.sponsors.dto.PackRequest;
import com.warriors.sponsors.dto.PackResponse;
import com.warriors.sponsors.entity.PackSponsoring;
import com.warriors.sponsors.exception.ConflictException;
import com.warriors.sponsors.exception.NotFoundException;
import com.warriors.sponsors.repository.ConventionRepository;
import com.warriors.sponsors.repository.PackSponsoringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PackSponsoringService {

    private final PackSponsoringRepository packRepository;
    private final ConventionRepository conventionRepository;

    public PackResponse creer(PackRequest request) {
        if (packRepository.existsByNomPack(request.getNomPack())) {
            throw new ConflictException("Un pack existe deja avec ce nom.");
        }
        PackSponsoring pack = new PackSponsoring();
        appliquer(pack, request);
        return toResponse(packRepository.save(pack));
    }

    @Transactional(readOnly = true)
    public List<PackResponse> lister() {
        return packRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PackResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    public PackResponse modifier(Long id, PackRequest request) {
        PackSponsoring pack = findOrThrow(id);
        if (packRepository.existsByNomPackAndIdNot(request.getNomPack(), id)) {
            throw new ConflictException("Un pack existe deja avec ce nom.");
        }
        appliquer(pack, request);
        return toResponse(packRepository.save(pack));
    }

    public void supprimer(Long id) {
        PackSponsoring pack = findOrThrow(id);
        if (conventionRepository.existsByPack_Id(id)) {
            throw new ConflictException("Ce pack est deja utilise par une convention.");
        }
        packRepository.delete(pack);
    }

    public PackSponsoring findOrThrow(Long id) {
        return packRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pack introuvable : " + id));
    }

    private void appliquer(PackSponsoring pack, PackRequest request) {
        pack.setNomPack(request.getNomPack().trim());
        pack.setTarif(request.getTarif());
        pack.setStandInclus(request.getStandInclus());
    }

    private PackResponse toResponse(PackSponsoring pack) {
        return PackResponse.builder()
                .id(pack.getId())
                .nomPack(pack.getNomPack())
                .tarif(pack.getTarif())
                .standInclus(pack.getStandInclus())
                .build();
    }
}
