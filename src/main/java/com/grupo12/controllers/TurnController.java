<<<<<<< Updated upstream
/*package com.grupo12.controllers;

import java.security.Principal;
import java.util.List;
=======
package com.grupo12.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
>>>>>>> Stashed changes

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
<<<<<<< Updated upstream
import org.springframework.web.bind.annotation.GetMapping;
=======
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
>>>>>>> Stashed changes
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

<<<<<<< Updated upstream
import com.grupo12.entities.Turn;
import com.grupo12.services.ITurnService;

@Controller
@RequestMapping("/turns")
=======
import com.grupo12.entities.Client;
import com.grupo12.entities.Turn;
import com.grupo12.entities.TurnStatus;
import com.grupo12.models.RequestTurnDTO;
import com.grupo12.services.IClientService;
import com.grupo12.services.ITurnService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/client")
>>>>>>> Stashed changes
public class TurnController {

    @Autowired
    private ITurnService turnService;
<<<<<<< Updated upstream

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
=======
    
    @Autowired
    private IClientService clientService;

   /* @GetMapping("/form")
    public String mostrarTurnosDisponibles(Model model) {
        List<Turn> turnosDisponibles = turnService.GetAvailableTurns();
        model.addAttribute("turnos", turnosDisponibles);
        return "turno/formulario-turno";
    }

    @PostMapping("/reserve-turn")
    public String reservarTurno(@RequestParam Long idTurno, Principal principal) {
        turnService.reserveAppointment(idTurno, principal.getName()); // suponiendo username = dni o email
        return "redirect:/turnos/mis-turnos";
    }*/
    
    @GetMapping("/select")
    public String mostrarFormulario(Model model) {
        List<Turn> availableTurns = turnService.getAvailableTurns()
        	    .stream()
        	    .filter(t -> t.getEmployee() != null && t.getDate() != null)
        	    .collect(Collectors.toList());
        
       
        // DEBUG:
        System.out.println("Turnos disponibles:");
        availableTurns.forEach(t -> System.out.println(t.getIdTurn()));
        
        model.addAttribute("turnos", availableTurns);
        model.addAttribute("requestTurnDTO", new RequestTurnDTO());
        return "client/select_turn";
    }
    
    @PostMapping("/select")
    public String asignarTurno(@ModelAttribute @Valid RequestTurnDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("turnos", turnService.getAvailableTurns());
            return "client/select_turn";
        }
        

        Optional<Turn> optionalTurn = turnService.getTurnById(dto.getTurnId());
        Optional<Client> optionalClient = clientService.getById(dto.getClientId());

        if (optionalTurn.isPresent() && optionalClient.isPresent()) {
            turnService.assignTurnToClient(optionalTurn.get(), optionalClient.get());
            return "redirect:/";
        }

        model.addAttribute("error", "Cliente o turno no válido");
        model.addAttribute("turnos", turnService.getAvailableTurns());
        return "client/select_turn";
    }
    
    @GetMapping("/my-turns")
    public String showTurns(@RequestParam(value = "clientId", required = false) Integer clientId, Model model) {
        if (clientId != null) {
            Optional<Client> optionalClient = clientService.getById(clientId);
            if (optionalClient.isPresent()) {
                Map<String, List<Turn>> turnosAgrupados = turnService.getTurnsGroupedByStatus(optionalClient.get());
                model.addAttribute("turnosAgrupados", turnosAgrupados);
            } else {
                model.addAttribute("error", "Cliente no encontrado.");
            }
        }

        model.addAttribute("clientId", clientId);
        return "client/my-turns";
    }

    @PostMapping("/my-turns")
    public String processTurns(@RequestParam("clientId") Integer clientId, Model model) {
        Optional<Client> optionalClient = clientService.getById(clientId);
        

        if (optionalClient.isPresent()) {
            Map<String, List<Turn>> groupedturns = turnService.getTurnsGroupedByStatus(optionalClient.get());
            model.addAttribute("turnosAgrupados", groupedturns);
        } else {
            model.addAttribute("error", "Cliente no encontrado.");
        }

        model.addAttribute("clientId", clientId);
        return "client/my-turns";
    }
    
    @PostMapping("/cancel-turn")
    public String cancelTurn(@RequestParam("turnId") Integer turnId, @RequestParam("clientId") Integer clientId, Model model) {
        Optional<Turn> optionalTurn = turnService.getTurnById(turnId);
        
        if (optionalTurn.isPresent()) {
            Turn turno = optionalTurn.get();
            if (turno.getStatus() == TurnStatus.PENDIENTE || turno.getStatus() == TurnStatus.EN_ATENCION) {
                turno.setStatus(TurnStatus.CANCELADO);
                
            }else if (turno.getStatus() == TurnStatus.CANCELADO) {
                turno.setStatus(TurnStatus.PENDIENTE);
            }
            turnService.save(turno); // Debes tener este método
        }

        return "redirect:/client/my-turns?clientId=" + clientId;
    }


    
}
>>>>>>> Stashed changes
