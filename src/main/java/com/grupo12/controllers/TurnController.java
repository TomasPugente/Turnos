package com.grupo12.controllers;
import com.grupo12.entities.Turn;
import com.grupo12.entities.TurnStatus;
import com.grupo12.models.TurnDTO;
import com.grupo12.models.TurnMultipleDTO;
import com.grupo12.services.ITurnService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/turns")
public class TurnController {
	 @Autowired
	    private ITurnService turnService;
	 
	 
	    /*@GetMapping("/select")
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
	    }*/
	 

	    // CU008 - Llamar turno para empleado
	 //@PostMapping("/turns/llamar/{id}")
	 @GetMapping("/call-next/{id}")
	 public TurnDTO callNextTurn(@PathVariable("id") int employeeId) {
		  return turnService.callNextTurnForEmployee(employeeId);
	 }
	 
	// CU009 - Habilitar un solo turno
	  // @PostMapping("/enable/single") //--> para usarlo en la aplicacion postman
		 @GetMapping("/enable/single")
		 public TurnDTO enableSingleTurn(
		     @RequestParam int employeeId,
		     @RequestParam int serviceId,
		     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
		     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
		 ) {
			 TurnDTO turn = new TurnDTO();
		    turn.setEmployeeIdPerson(employeeId); // ✅ Correcto
		    turn.setIdServicio(serviceId);        // ✅ Correcto
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
		   TurnMultipleDTO dto = new TurnMultipleDTO();
		   dto.setEmployeeIdPerson(employeeId);
	    	dto.setIdServicio(serviceId);
	    	dto.setStartDate(startDate);
	    	dto.setEndDate(endDate);
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




