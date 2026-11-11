package com.universite.evenements.entity;

import com.universite.evenements.enums.StatutEvenement;
import com.universite.evenements.enums.TypeEvenement;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evenements")
@Getter
@Setter
@NoArgsConstructor
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titre;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeEvenement type;

    @Column(nullable = false)
    private LocalDateTime date;

    private Long salleId;

    @Column(nullable = false)
    private Integer capacite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutEvenement statut;

    @OneToMany(mappedBy = "evenement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Inscription> inscriptions = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (statut == null) {
            statut = StatutEvenement.BROUILLON;
        }
    }
}
