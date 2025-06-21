package com.grupo12.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Opcional: captura errores generales
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + ex.getMessage());
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public String handleClientNotFound(ClientNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "client/select-turn";
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "client/select-turn"; // o la vista que quieras
    }

    @ExceptionHandler(TurnNotFoundException.class)
    public String handleTurnNotFound(TurnNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "client/select-turn";
    }

    @ExceptionHandler(TurnNotAvailableException.class)
    public String handleTurnNotAvailable(TurnNotAvailableException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "client/select-turn";
    }
}
