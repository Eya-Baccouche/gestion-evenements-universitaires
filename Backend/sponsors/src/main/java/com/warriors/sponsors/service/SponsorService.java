package com.warriors.sponsors.service;

import com.warriors.sponsors.dto.SponsorRequest;
import com.warriors.sponsors.dto.SponsorResponse;
import com.warriors.sponsors.entity.Sponsor;
import com.warriors.sponsors.exception.ConflictException;
import com.warriors.sponsors.exception.NotFoundException;
import com.warriors.sponsors.repository.ConventionRepository;
import com.warriors.sponsors.repository.SponsorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SponsorService {

    private final SponsorRepository sponsorRepository;
    private final ConventionRepository conventionRepository;

    public SponsorResponse creer(SponsorRequest request) {
        if (sponsorRepository.existsByContactEmail(request.getContactEmail())) {
            throw new ConflictException("Un sponsor existe deja avec cet email.");
        }
        Sponsor sponsor = new Sponsor();
        appliquer(sponsor, request);
        return toResponse(sponsorRepository.save(sponsor));
    }

    @Transactional(readOnly = true)
    public List<SponsorResponse> lister() {
        return sponsorRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SponsorResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    public SponsorResponse modifier(Long id, SponsorRequest request) {
        Sponsor sponsor = findOrThrow(id);
        if (sponsorRepository.existsByContactEmailAndIdNot(request.getContactEmail(), id)) {
            throw new ConflictException("Un sponsor existe deja avec cet email.");
        }
        appliquer(sponsor, request);
        return toResponse(sponsorRepository.save(sponsor));
    }

    public void supprimer(Long id) {
        Sponsor sponsor = findOrThrow(id);
        if (conventionRepository.existsBySponsor_Id(id)) {
            throw new ConflictException("Ce sponsor est deja lie a une convention.");
        }
        sponsorRepository.delete(sponsor);
    }

    public Sponsor findOrThrow(Long id) {
        return sponsorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sponsor introuvable : " + id));
    }

    private void appliquer(Sponsor sponsor, SponsorRequest request) {
        sponsor.setNomEntreprise(request.getNomEntreprise().trim());
        sponsor.setContactEmail(request.getContactEmail().trim());
        sponsor.setLogoUrl(request.getLogoUrl());
    }

    private SponsorResponse toResponse(Sponsor sponsor) {
        return SponsorResponse.builder()
                .id(sponsor.getId())
                .nomEntreprise(sponsor.getNomEntreprise())
                .contactEmail(sponsor.getContactEmail())
                .logoUrl(sponsor.getLogoUrl())
                .build();
    }
}
