package com.warriors.planning.entities.enums;

/**
 * Cycle de vie d'une réservation.
 *
 *  Création ─► EN_ATTENTE ──valider──► CONFIRMEE ──annuler──► ANNULEE
 *                  │                      │
 *                refuser                 annuler
 *                  ▼                      ▼
 *               REFUSEE               ANNULEE
 */
public enum StatutReservation {
    EN_ATTENTE,
    CONFIRMEE,
    ANNULEE,
    REFUSEE
}
