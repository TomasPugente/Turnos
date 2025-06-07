
package com.grupo12.controllers;
import com.grupo12.models.TurnDTO;
import com.grupo12.models.TurnMultipleDTO;
import com.grupo12.services.ITurnService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/turns")
public class TurnController {
	 @Autowired
	    private ITurnService turnService;
	 

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

