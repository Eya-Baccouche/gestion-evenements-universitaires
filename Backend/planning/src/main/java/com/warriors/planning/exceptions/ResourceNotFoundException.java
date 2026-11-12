package com.warriors.planning.exceptions;

/**
 * Levée quand une ressource demandée n'existe pas (404).
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
