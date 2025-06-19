
package com.grupo12.controllers;

import java.security.Principal;
import java.util.List;


import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.grupo12.entities.Turn;
import com.grupo12.services.ITurnService;
import com.grupo12.services.IUserService;
import com.grupo12.entities.Client;
import com.grupo12.entities.Turn;
import com.grupo12.entities.TurnStatus;
import com.grupo12.entities.User;
import com.grupo12.exceptions.ClientNotFoundException;
import com.grupo12.exceptions.UserNotFoundException;
import com.grupo12.models.RequestTurnDTO;
import com.grupo12.services.IClientService;
import com.grupo12.services.ITurnService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/client")
public class TurnController {

    @Autowired
    private ITurnService turnService;
    
    @Autowired
    private IClientService clientService;
    
    @Autowired
    @Qualifier("userService")
    private IUserService userService;
    
    
    @GetMapping("/select")
    public String selectTurn(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado."));

        Client client = clientService.getByUsername(user)
                .orElseThrow(() -> new ClientNotFoundException("Cliente no encontrado."));

        // Crear el DTO y setear el clientId automáticamente
        RequestTurnDTO requestTurnDTO = new RequestTurnDTO();
        requestTurnDTO.setClientId(client.getIdPerson());

        model.addAttribute("requestTurnDTO", requestTurnDTO);

        // Asumimos que ya tenés una lista de turnos disponibles
        List<Turn> turnosDisponibles = turnService.getAvailableTurns(); 
        model.addAttribute("turnos", turnosDisponibles);
        
        System.out.println("Tamaño de la lista de turnos: " + turnosDisponibles.size());
        turnosDisponibles.forEach(t -> System.out.println("Turno ID: " + t.getIdTurn()));

        return "client/select-turn";
    }

    
    @PostMapping("/select")
    public String asignTurn(@ModelAttribute @Valid RequestTurnDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("turnos", turnService.getAvailableTurns());
            return "client/select-turn";
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Client> optionalClient = clientService.getByUsername(username); // nuevo
        Optional<Turn> optionalTurn = turnService.getTurnById(dto.getTurnId());

        if (optionalTurn.isPresent() && optionalClient.isPresent()) {
            turnService.assignTurnToClient(optionalTurn.get(), optionalClient.get());
            return "redirect:/client/select?success";
        }

        model.addAttribute("error", "Cliente o turno no válido");
        model.addAttribute("turnos", turnService.getAvailableTurns());
        return "client/select-turn";
    }
    
    @GetMapping("/my-turns")
    public String showTurns(Model model) {
        // Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> optionalUser = userService.findByUsername(username);
        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "client/my-turns";
        }

        Optional<Client> optionalClient = clientService.getByUsername(optionalUser.get());
        if (optionalClient.isEmpty()) {
            model.addAttribute("error", "Cliente no encontrado.");
            return "client/my-turns";
        }

        // Obtener los turnos del cliente
        Map<String, List<Turn>> turnosAgrupados = turnService.getTurnsGroupedByStatus(optionalClient.get());
        model.addAttribute("turnosAgrupados", turnosAgrupados);

        return "client/my-turns";
    }
    



    @PostMapping("/my-turns")
    public String processTurns(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> optionalUser = userService.findByUsername(username);
        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "client/my-turns";
        }

        Optional<Client> optionalClient = clientService.getByUsername(optionalUser.get());
        if (optionalClient.isEmpty()) {
            model.addAttribute("error", "Cliente no encontrado.");
            return "client/my-turns";
        }

        Map<String, List<Turn>> groupedTurns = turnService.getTurnsGroupedByStatus(optionalClient.get());
        model.addAttribute("turnosAgrupados", groupedTurns);

        return "client/my-turns";
    }

    
    @PostMapping("/cancel-turn")
    public String cancelTurn(@RequestParam("turnId") Integer turnId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> optionalUser = userService.findByUsername(username);
        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "redirect:/client/my-turns";
        }

        Optional<Client> optionalClient = clientService.getByUsername(optionalUser.get());
        if (optionalClient.isEmpty()) {
            model.addAttribute("error", "Cliente no encontrado.");
            return "redirect:/client/my-turns";
        }

        Optional<Turn> optionalTurn = turnService.getTurnById(turnId);
        if (optionalTurn.isPresent()) {
            Turn turno = optionalTurn.get();
            
            // Validar que el turno pertenezca al cliente logueado
            if (turno.getClient() != null && turno.getClient().getIdPerson().equals(optionalClient.get().getIdPerson())) {
                if (turno.getStatus() == TurnStatus.PENDIENTE || turno.getStatus() == TurnStatus.EN_ATENCION) {
                    turno.setStatus(TurnStatus.CANCELADO);
                } else if (turno.getStatus() == TurnStatus.CANCELADO) {
                    turno.setStatus(TurnStatus.PENDIENTE);
                }
                turnService.save(turno);
            }
        }

        return "redirect:/client/my-turns";
    }



    
}
