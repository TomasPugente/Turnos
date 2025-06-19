package com.grupo12.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

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
