package com.grupo12.controllers.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.grupo12.exceptions.MissingTurnDataException;
import com.grupo12.models.TurnDTO;
import com.grupo12.models.TurnMultipleDTO;
import com.grupo12.services.IEmployeeService;
import com.grupo12.services.IServiceService;
import com.grupo12.services.ITurnService;


@Controller
@RequestMapping("/web/turns")
public class TurnWebController {

    @Autowired
    private ITurnService turnService;
    
    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IServiceService serviceService;
    

    //vistas para CU011
    @GetMapping("/generados")
    public String mostrarTurnosGenerados(
            @RequestParam int employeeId,
            @RequestParam int serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam int durationMinutes,
            Model model) {
    	TurnDTO dto = new TurnDTO();
		   dto.setEmployeeIdPerson(employeeId);
	    	dto.setServiceId(serviceId);
	    	dto.setStartTime(startDate);
	    	dto.setEndTime(endDate);
	    	dto.setDurationMinutes(durationMinutes);
        List<TurnDTO> turnos = turnService.enableMultipleTurns(dto);
        model.addAttribute("turnos", turnos);
        return "turns/listTurns"; // nombre del archivo lista_turnos.html
    }


    @GetMapping("/turnos/seleccionar")
    public String mostrarTurnosDisponibles(
    		
            @RequestParam int employeeId,
            @RequestParam int serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam int durationMinutes,
            Model model) {
    	TurnDTO dto = new TurnDTO();
    	dto.setEmployeeIdPerson(employeeId);
    	dto.setServiceId(serviceId);
    	dto.setStartTime(startDate);
    	dto.setEndTime(endDate);
    	dto.setDurationMinutes(durationMinutes);

    	List<TurnDTO> turnos = turnService.enableMultipleTurns(dto);
        model.addAttribute("turnos", turnos);
        return "turns/turnsView"; // Esto se refiere a un archivo `turnos_disponibles.html` en `src/main/resources/templates`
    }
    
    @GetMapping("/form")
    public String mostrarFormulario() {
        return "turns/generateTurn"; // sin la extensión .html
    }
    
    @GetMapping("/reminders")
    public String mostrarTurnosParaRecordatorio(Model model) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        List<TurnDTO> turnos=turnService.findUpcomingTurns(now, tomorrow);
        model.addAttribute("turnos", turnos);
        return "turns/reminders";
    }
    @PostMapping("/send-reminders")
    public String enviarRecordatorios(
            @RequestParam(value = "selectedTurnIds", required = false) List<Integer> ids,
            RedirectAttributes redirectAttributes,
            Model model) {

        List<TurnDTO> enviados;

        if (ids == null || ids.isEmpty()) {
            // Envío automático para todos los turnos próximos
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime tomorrow = now.plusDays(1);
         //   enviados = turnService.getUpComingTurnsForReminders(now, tomorrow); // ya incluye el envío
            enviados=turnService.findUpcomingTurns(now, tomorrow);
            redirectAttributes.addFlashAttribute("mensaje", "Se enviaron recordatorios a todos los turnos próximos.");
        } else {
            // Envío manual sólo a los seleccionados
            enviados = turnService.sendRemindersTo(ids);
            redirectAttributes.addFlashAttribute("mensaje", "Se enviaron recordatorios a " + enviados.size() + " turno(s) seleccionado(s).");
        }

        // Marcar como enviados en memoria
        for (TurnDTO dto : enviados) {
            dto.setReminderSent(true);
        }

        model.addAttribute("enviados", enviados);
        return "turns/remindersResult";
    }
    //vista para CU010
    @GetMapping("/update-status-view")
    public String updateTurnStatusGET(@RequestParam int id, @RequestParam String newStatus, Model model) {
        TurnDTO turno = turnService.updateTurnStatus(id, newStatus);
        model.addAttribute("turno", turno);
        return "turns/status-updated";
    }
    
    //vista para CU008 
    @GetMapping("/call-next-view")
    public String callNextTurn(@RequestParam("employeeId") int employeeId, Model model) {
        TurnDTO turno = turnService.callNextTurnForEmployee(employeeId);
        model.addAttribute("turno", turno);
        return "turns/called-turn"; // nombre de la vista Thymeleaf en src/main/resources/templates
    }
    
    //vistas para CU009
    @GetMapping("/enable/single-form")
    public String mostrarFormularioSingle(Model model) {
        model.addAttribute("turno", new TurnDTO());
        model.addAttribute("empleados", employeeService.getAll()); // Lista de empleados
        model.addAttribute("servicios", serviceService.getAllDTO()); // Lista de servicios
        return "turns/generateSingleTurn";
    }

    @PostMapping("/enable/single")
    public String habilitarTurnoUnico(@ModelAttribute TurnDTO turno, RedirectAttributes redirectAttributes) {
    	  System.out.println("🔧 POST recibido: " + turno);
    	  
    	  try {
      	    turnService.enableSingleTurn(turno);
      	    redirectAttributes.addFlashAttribute("successMessage", "Turno habilitado correctamente.");
    	  } catch (MissingTurnDataException ex) {
    		redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
    	  }

    	 return "redirect:/web/turns/enable/single-form";// o la vista que quieras
    }
    
    /*@PostMapping("/enable/single")
    public String habilitarTurnoUnico(@ModelAttribute TurnDTO turno, Model model) {
        try {
            turnService.enableSingleTurn(turno);
            model.addAttribute("successMessage", "Turno habilitado correctamente.");
        } catch (MissingTurnDataException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }

        // Volvés directo a la vista
        model.addAttribute("turno", turno);
        model.addAttribute("empleados", employeeService.getAll()); // ⚠️ Asegurate de volver a cargar estos
        model.addAttribute("servicios", serviceService.getAllDTO());
        return "redirect:/web/turns/enable/single-form"; // o el nombre correcto de tu vista
    }*/

    
    @GetMapping("/enable/multiple-form")
    public String mostrarFormularioMultiple(Model model) {
        model.addAttribute("turnMultiple", new TurnDTO());
        model.addAttribute("empleados", employeeService.getAll());
        model.addAttribute("servicios", serviceService.getAll());
        return "turns/generateMultipleTurns";
    }

    @PostMapping("/enable/multiple")
    public String habilitarTurnosMultiples(@ModelAttribute TurnDTO dto, RedirectAttributes redirectAttributes) {
        turnService.enableMultipleTurns(dto);
        redirectAttributes.addFlashAttribute("mensaje", "Turnos generados correctamente.");
        return "redirect:/web/turns/enable/multiple-form";
    }
}