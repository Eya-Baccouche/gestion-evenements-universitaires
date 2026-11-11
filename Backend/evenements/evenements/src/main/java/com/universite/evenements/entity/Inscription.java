package com.universite.evenements.entity;

import com.universite.evenements.enums.StatutInscription;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "inscriptions",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_evenement_participant",
            columnNames = {"evenement_id", "participant_id"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evenement_id", nullable = false)
    private Evenement evenement;

    @Column(name = "participant_id", nullable = false)
    private Long participantId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutInscription statut;

    @Column(nullable = false)
    private LocalDateTime date;

    @PrePersist
    public void prePersist() {
        if (date == null) {
            date = LocalDateTime.now();
        }
        if (statut == null) {
            statut = StatutInscription.CONFIRMEE;
        }
    }
}
