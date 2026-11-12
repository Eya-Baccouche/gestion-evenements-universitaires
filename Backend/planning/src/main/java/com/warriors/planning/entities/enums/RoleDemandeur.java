package com.warriors.planning.entities.enums;

/**
 * Rôle de la personne qui demande la réservation.
 * ADMIN        : accès complet (CRUD salles/bâtiments, validation réservations)
 * ORGANISATEUR : crée et gère ses propres réservations
 */
public enum RoleDemandeur {
    ADMIN,
    ORGANISATEUR
}
