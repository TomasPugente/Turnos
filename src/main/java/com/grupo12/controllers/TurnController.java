package com.grupo12.controllers;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import java.security.Principal;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.grupo12.models.TurnDTO;
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
    	System.out.println("[DEBUG] Entró al método GET /select");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("[DEBUG] Entró a Authentication");
        String username = auth.getName();
        System.out.println("[DEBUG] Entró a auth.getName()");

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado."));
        System.out.println("[DEBUG] userService.findByUsername(username)");
        Client client = clientService.getByUsername(user)
                .orElseThrow(() -> new ClientNotFoundException("Cliente no encontrado."));
        System.out.println("[DEBUG] clientService.getByUsername(user)");

        RequestTurnDTO requestTurnDTO = new RequestTurnDTO();
        System.out.println("[DEBUG] new RequestTurnDTO()");
        requestTurnDTO.setClientId(client.getIdPerson());
        System.out.println("[DEBUG] client.getIdPerson()");

        model.addAttribute("requestTurnDTO", requestTurnDTO);  // ESTE es clave!
        System.out.println("[DEBUG] model.addAttribute(\"requestTurnDTO\", requestTurnDTO)");

        List<Turn> turnosDisponibles = new ArrayList<>();
        try {
            turnosDisponibles = turnService.getAvailableTurns();
            System.out.println("[DEBUG] Cantidad de turnos disponibles: " + turnosDisponibles.size());
            turnosDisponibles.forEach(t -> System.out.println(t.getIdTurn() + " - " + t.getDate() + " - " + t.getEmployee()));
            System.out.println("[DEBUG] turnService.getAvailableTurns(); ejecutado");
            model.addAttribute("turnos", turnosDisponibles); // ✅ ESTA LÍNEA FALTABA
        } catch (Exception e) {
            System.out.println("[ERROR] Falló getAvailableTurns(): " + e.getMessage());
            e.printStackTrace(); // Esto te da la traza exacta
            model.addAttribute("error", "Ocurrió un error al obtener los turnos.");
            return "client/select-turn"; // o una página de error si preferís
        };
        return "client/select-turn";
    }
    /*@GetMapping("/select")
    public String selectTurn(Model model) {
    	
        if (result.hasErrors()) {
            model.addAttribute("turnos", turnService.getAvailableTurns());
            model.addAttribute("requestTurnDTO", dto);  // <-- Importante!
            return "client/select-turn";
        }
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
        model.addAttribute("turnos", turnService.getAvailableTurns());
        return "client/select-turn";
    }*/
    

    
    /*@PostMapping("/select")
    public String asignTurn(@ModelAttribute("requestTurnDTO") @Valid RequestTurnDTO dto, BindingResult result, Model model) {
    	System.out.println("[DEBUG] Entró al método POST /select");
        if (result.hasErrors()) {
            model.addAttribute("turnos", turnService.getAvailableTurns());
            model.addAttribute("requestTurnDTO", dto);
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
    }*/
    
    @PostMapping("/select")
    public String asignTurn(@ModelAttribute("requestTurnDTO") @Valid RequestTurnDTO dto, BindingResult result, Model model) {
        System.out.println("[DEBUG] Turno ID: " + dto.getTurnId());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("[DEBUG] Username logueado: " + username);

        Optional<Client> optionalClient = clientService.getByUsername(username);
        Optional<Turn> optionalTurn = turnService.getTurnById(dto.getTurnId());

        System.out.println("[DEBUG] Turno encontrado: " + optionalTurn.isPresent());
        System.out.println("[DEBUG] Cliente encontrado: " + optionalClient.isPresent());

        if (optionalTurn.isPresent() && optionalClient.isPresent()) {
            turnService.assignTurnToClient(optionalTurn.get(), optionalClient.get());
            return "redirect:/client/select?success";
        }

        model.addAttribute("error", "Cliente o turno no válido");
        model.addAttribute("turnos", turnService.getAvailableTurns());
        model.addAttribute("requestTurnDTO", dto);
        return "client/select-turn";
    }
    

    
    /*@PostMapping("/select")
    public String asignTurn(@ModelAttribute @Valid RequestTurnDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("turnos", turnService.getAvailableTurns());
            model.addAttribute("requestTurnDTO", dto);  // <--- obligatorio
            return "client/select-turn";
        }

        Optional<Client> optionalClient = clientService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Optional<Turn> optionalTurn = turnService.getTurnById(dto.getTurnId());

        if (optionalTurn.isPresent() && optionalClient.isPresent()) {
            turnService.assignTurnToClient(optionalTurn.get(), optionalClient.get());
            return "redirect:/client/select?success";
        }

        model.addAttribute("error", "Cliente o turno no válido");
        model.addAttribute("turnos", turnService.getAvailableTurns());
        model.addAttribute("requestTurnDTO", dto);  // <--- obligatorio también aquí
        return "client/select-turn";
    }*/
    
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
    
    @GetMapping("/enable-multiple-form")
    public String showEnableMultipleForm() {
        return "turns/enable-multiple"; // Cambiá la ruta si lo pusiste en otra carpeta
    }



	    // CU008 - Llamar turno para empleado
	 //@PostMapping("/turns/llamar/{id}")
	 @GetMapping("/llamar/{id}")
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
	            @RequestParam Long serviceId,
	            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
	            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
	            @RequestParam int durationMinutes) {

	        return turnService.enableMultipleTurns(employeeId, serviceId, startDate, endDate, durationMinutes);
	    }
	    
	 // CU010 - Registrar asistencia o inasistencia
	   /*@PutMapping("/estado")
	   
	    public TurnDTO updateTurnStatus(@RequestParam int id, @RequestParam String newStatus) {
	        return turnService.updateTurnStatus(id, newStatus);
	    }*/
	   
	   @GetMapping("/estado")
	   public TurnDTO updateTurnStatusGET(@RequestParam int id, @RequestParam String newStatus) {
	       return turnService.updateTurnStatus(id, newStatus);
	   }

	    // CU011 - Obtener turnos para recordatorios (sólo uso interno o de prueba)
	    @GetMapping("/recordatorios")
	    public List<TurnDTO> getTurnsForReminder(
	            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
	            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
	        return turnService.getUpComingTurnsForReminders(from, to);
	    }
}

