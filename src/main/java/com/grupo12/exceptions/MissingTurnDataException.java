package com.grupo12.exceptions;


public class MissingTurnDataException extends RuntimeException {
    
    public MissingTurnDataException(String message) {
        super(message);
    }
}
