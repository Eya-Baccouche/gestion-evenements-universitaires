package com.warriors.planning.entities.enums;

/**
 * Statut d'une salle.
 * DISPONIBLE   : réservable
 * EN_MAINTENANCE : ne peut pas recevoir de réservations
 * FERME        : hors service (rénovation, etc.)
 */
public enum StatutSalle {
    DISPONIBLE,
    EN_MAINTENANCE,
    FERME
}
