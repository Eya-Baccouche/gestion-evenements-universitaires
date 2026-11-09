package com.warriors.feedback.intervenants.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"intervenantId", "evenementId"}))
public class Affectation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long intervenantId;

    @NotNull
    private Long evenementId;

    @Enumerated(EnumType.STRING)
    private RoleAffectation role;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIntervenantId() { return intervenantId; }
    public void setIntervenantId(Long intervenantId) { this.intervenantId = intervenantId; }

    public Long getEvenementId() { return evenementId; }
    public void setEvenementId(Long evenementId) { this.evenementId = evenementId; }

    public RoleAffectation getRole() { return role; }
    public void setRole(RoleAffectation role) { this.role = role; }
}
