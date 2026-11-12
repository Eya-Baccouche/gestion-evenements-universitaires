package com.warriors.planning.exceptions;

/**
 * Levée quand une règle métier est violée (400).
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
