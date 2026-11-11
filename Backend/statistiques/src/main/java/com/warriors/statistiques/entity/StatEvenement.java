package com.warriors.statistiques.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "stat_evenements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatEvenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long evenementId;

    private String titre;

    private LocalDate dateEvenement;

    private String statut = "PUBLIE"; // PUBLIE, ANNULE, TERMINE

    private Integer inscrits = 0;
    private Integer presents = 0;
    private Double tauxPresence = 0.0;
    private Double noteMoyenne = 0.0;
    private Integer nbAvis = 0;

    public void calculerTauxPresence() {
        if (inscrits != null && inscrits > 0 && presents != null) {
            this.tauxPresence = Math.round(((double) presents / inscrits * 100.0) * 100.0) / 100.0;
        } else {
            this.tauxPresence = 0.0;
        }
    }
}
