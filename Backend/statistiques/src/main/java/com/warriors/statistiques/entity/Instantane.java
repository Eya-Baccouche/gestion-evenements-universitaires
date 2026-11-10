package com.warriors.statistiques.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "instantanes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Instantane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long evenementId;

    private LocalDateTime date = LocalDateTime.now();
    private Integer inscrits = 0;
    private Integer presents = 0;
}
