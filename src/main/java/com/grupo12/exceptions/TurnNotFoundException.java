package com.grupo12.exceptions;

public class TurnNotFoundException extends RuntimeException {
    public TurnNotFoundException(Long id) {
        super("No se encontró el turno con ID: " + id);
    }
}