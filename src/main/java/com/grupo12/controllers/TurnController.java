package com.grupo12.controllers;
import com.grupo12.entities.Client;
import com.grupo12.entities.Turn;
import com.grupo12.entities.TurnStatus;
import com.grupo12.entities.User;
import com.grupo12.exceptions.ClientNotFoundException;
import com.grupo12.exceptions.UserNotFoundException;
import com.grupo12.models.RequestTurnDTO;
import com.grupo12.models.TurnDTO;
import com.grupo12.models.TurnMultipleDTO;
import com.grupo12.services.IClientService;
import com.grupo12.services.ITurnService;
import com.grupo12.services.IUserService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


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
    
    // CU004 - Solicitar Turno
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

        // Asumo que tengo una lista de turnos disponibles
        List<Turn> turnosDisponibles = turnService.getAvailableTurns(); 
        model.addAttribute("turnos", turnosDisponibles);
        
        System.out.println("Tamaño de la lista de turnos: " + turnosDisponibles.size());
        turnosDisponibles.forEach(t -> System.out.println("Turno ID: " + t.getIdTurn()));

        return "client/select-turn";
    }

    // Al presionar en confirmar turno se guarda en la base de datos    
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
    
     // CU005 - Ver mis turnos
     // Se muestran los turnos del cliente logueado
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
    


    // Muestra los Agrupa los turnos clasificandolos por pendientes o cancelados
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

    
    // CU007 - Cancelar Turno
    // Puede cancelar o reactivar el turno presionando un boton
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
            	    turno.setPreviousStatus(turno.getStatus()); // Guarda el estado actual
            	    turno.setStatus(TurnStatus.CANCELADO);
            	} else if (turno.getStatus() == TurnStatus.CANCELADO) {
            	    if (turno.getPreviousStatus() != null) {
            	        turno.setStatus(turno.getPreviousStatus());
            	        turno.setPreviousStatus(null); // Limpia el estado previo
            	    } else {
            	        turno.setStatus(TurnStatus.PENDIENTE); // Por defecto
            	    }
            	}
            	
                turnService.save(turno);
            }
        }

        return "redirect:/client/my-turns";
    }



	 
	// CU009 - Habilitar un solo turno
    @GetMapping("/enable/single")
    public String showEnableSingleTurnForm(Model model) {
        TurnDTO turno = new TurnDTO();
        model.addAttribute("turno", turno);

        model.addAttribute("empleados", userService.findAllEmployees()); // ← tu método para traer empleados
        model.addAttribute("servicios", turnService.getAllServicios()); // ← o como traigas los servicios

        return "turns/generateSingleTurn";
    }
    
	  // @PostMapping("/enable/single") //--> para usarlo en la aplicacion postman
		 @PostMapping("/enable/single")
		 public TurnDTO enableSingleTurn(
		     @RequestParam int employeeId,
		     @RequestParam int serviceId,
		     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
		     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
		 ) {
			 TurnDTO turn = new TurnDTO();
		    turn.setEmployeeIdPerson(employeeId); // ✅ Correcto
		    turn.setServiceId(serviceId);        // ✅ Correcto
		    turn.setStartTime(startDate);
		    turn.setEndTime(endDate);
		    return turnService.enableSingleTurn(turn);
		 }

	    // CU009 - Habilitar multiples turnos
	    //@PostMapping("/enable/multiple")//--> para usarlo en la aplicacion postman
	   @GetMapping("/enable/multiple")//---> para usarlo al mostrar en el explorador
	    public List<TurnDTO> enableMultipleTurns(
	            @RequestParam int employeeId,
	            @RequestParam int serviceId,
	            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
	            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
	            @RequestParam int durationMinutes) {
		   TurnDTO dto = new TurnDTO();
		   dto.setEmployeeIdPerson(employeeId);
	    	dto.setServiceId(serviceId);
	    	dto.setStartTime(startDate);
	    	dto.setEndTime(endDate);
	    	dto.setDurationMinutes(durationMinutes);

		  // List<TurnDTO> turnos = turnService.enableMultipleTurns(dto);
	        return turnService.enableMultipleTurns(dto);
	    }
	    
	 // CU010 - Registrar asistencia o inasistencia
	   /*@PutMapping("/estado")
	   
	    public TurnDTO updateTurnStatus(@RequestParam int id, @RequestParam String newStatus) {
	        return turnService.updateTurnStatus(id, newStatus);
	    }*/
	   
	   @GetMapping("/update-status")
	   public TurnDTO updateTurnStatusGET(@RequestParam int id, @RequestParam String newStatus) {
		   
	       return turnService.updateTurnStatus(id, newStatus);
	   }

	    // CU011 - Obtener turnos para recordatorios (sólo uso interno o de prueba)
	    @GetMapping("/recordatorios")
	    public List<TurnDTO> getTurnsForReminder(
	            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
	            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
	    //    return turnService.getUpComingTurnsForReminders(from, to);
	        return turnService.findUpcomingTurns(from, to);
	    }

}



