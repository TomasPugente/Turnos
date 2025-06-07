/*package com.grupo12.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.grupo12.entities.Turn;
import com.grupo12.services.ITurnService;

@Controller
@RequestMapping("/turns")
public class TurnController {

    @Autowired
    private ITurnService turnService;

    @GetMapping("/form")
    public String mostrarTurnosDisponibles(Model model) {
        List<Turn> availableTurns = turnService.GetAvailableAppointments();
        model.addAttribute("turnos", availableTurns);
        return "turno/formulario-turno";
    }

    @PostMapping("/reservar")
    public String reservarTurno(@RequestParam Long idTurn, Principal principal) {
    	//no podria pasar el id de cliente en vez de el username? 	
    	turnService.reserveTurn(idTurn, principal.getName()); // suponiendo username = dni o email
        return "redirect:/turnos/mis-turnos";
    }
}*/
