package com.warriors.sponsors.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "packs_sponsoring")
@Getter
@Setter
@NoArgsConstructor
public class PackSponsoring {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nomPack;

    @Column(nullable = false)
    private Double tarif;

    @Column(nullable = false)
    private Boolean standInclus;
}
