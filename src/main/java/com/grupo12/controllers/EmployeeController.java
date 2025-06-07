package com.grupo12.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.grupo12.models.TurnDTO;
import com.grupo12.services.ITurnService;
import com.grupo12.services.implementation.ServiceService;
import com.grupo12.entities.TurnStatus;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.grupo12.security.EmployeeDetails;


@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private ITurnService turnService;
	@Autowired
	private Service service;
	@Autowired
	private ServiceService serviceService;
	
	//Simula obtener el ID del empleado logueado. Hay q adaptarlo para usarlo con Spring Security
	private int getCurrentLoggedInEmployeeId() {
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		if(authentication==null || authentication.isAuthenticated()) {
			throw new IllegalStateException("No hay un usuario autenticado");
		}
		Object principal=authentication.getPrincipal();
		if(principal instanceof EmployeeDetails) {
			try {
				EmployeeDetails employeeDetails=(EmployeeDetails)principal;
				return Integer.parseInt(((UserDetails)principal).getUsername());
			}catch (NumberFormatException e) {
				throw new IllegalStateException("El nombre de usuario no es un ID de empleado numerico ",e);
			}
		}
		//return employeeDetails.getIdPerson();
		return Integer.parseInt(((UserDetails)principal).getUsername());
	}
	
	//CU008: Llamar turno
	//Muestra la lista de turnos pendientes del empleado para llamar
	@GetMapping("/turns")
	public String showEmployeeTurns(Model model) {
		int employeeId=getCurrentLoggedInEmployeeId();
		List<TurnDTO> allEmployeeTurns=turnService.getTurnsByEmployeeId(employeeId);
		//Filtrar solo los turnos PENDIENTES para la vista de "Llamar turno"
		List<TurnDTO> pendingTurns=allEmployeeTurns.stream().filter(turn -> turn.getStatus().equals(TurnStatus.PENDIENTE.name())).collect(Collectors.toList());
		model.addAttribute("pendingTurns",pendingTurns);
		model.addAttribute(employeeId);//Pasar el ID del empleado para el formulario
		return "employee/turns-list";//Mapea a src/main/resources/templates/employee/turns-list.html
	}
	
	//Accion para llamar al proximo turno
	@PostMapping("/turns/call-next")
	public String callNextTurn(@RequestParam("employeeId")int employeeId,RedirectAttributes redirectAttributes){
		try {
			TurnDTO calledTurn=turnService.callNextTurnForEmployee(employeeId);
			redirectAttributes.addFlashAttribute("successMessage","Turno #"+calledTurn.getIdTurn()+" de "+calledTurn.getClientName()+" ahora 'En Atencion'.");
			
		}catch(IllegalStateException| EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());
		}catch(Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage","Error al llamar turno:"+e.getMessage());
		}
		return "redirect:/employee/turns";
	}
	
	//CU010: Registrar Asistencia/Inasistencia
	//Muestra todos los turnos del empleado para gestionar su estado
	@GetMapping("/turns/manage")
	public String showManageTurns(Model model) {
		int employeeId=getCurrentLoggedInEmployeeId();
		List<TurnDTO> employeeTurns=turnService.getTurnsByEmployeeId(employeeId);
		model.addAttribute("employeeTurns ",employeeTurns);
		return "employee/manage-turns";//Mapea a src/main/resources/templates/employee/manage-turns.html
	}
	
	@PostMapping("/turns/update-status")
	public String updateTurnStatus(@RequestParam("turnId") int turnId,@RequestParam("newStatus")String newStatus,RedirectAttributes redirectAttributes) {
		try {
			TurnDTO updateTurn=turnService.updateTurnStatus(turnId, newStatus);
			redirectAttributes.addFlashAttribute("successMessage)"," Estado del turno #"+updateTurn.getIdTurn()+" actualizado a "+updateTurn.getStatus()+".");
			
		}catch (IllegalArgumentException | EntityNotFoundException | IllegalStateException e) {
			redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());
		}catch(Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage"," Error al actualizar el estado del turno: "+e.getMessage());
		}
		return "redirect:/employee/turns/manage";
	}
	
	//CU009: Habilitar turnos
	//Muestra el formulario para habilitar nuevos turnos
	@GetMapping("/turns/enable")
	public String showEnableTurnsForm(Model model) {
		model.addAttribute("turnDTO",new TurnDTO());
		model.addAttribute("employeeId",getCurrentLoggedInEmployeeId());
		model.addAttribute("services",serviceService.getAllServices());//cargar la lista de servicios si el empleado debe seleccionarlos
		
		return "employee/enable-turns";//Mapea a src/main/resources/templates/employee/enable.turns.html
	}
	
	//Procesa la solicitud para habilitar un solo turno
	@PostMapping("/turns/enable")
	public String enableTurn(@ModelAttribute TurnDTO turnDTO, RedirectAttributes redirectAttributes) {
		try {
			//Asigna el empleado logueado al turno que se va a habilitar
			turnDTO.setEmployeeIdPerson(getCurrentLoggedInEmployeeId());
			turnDTO.setStatus(TurnStatus.PENDIENTE.name());//Asegura que se crea como PENDIENTE
			TurnDTO enabledTurn=turnService.enableSingleTurn(turnDTO);
			redirectAttributes.addFlashAttribute("successMesage"," Turno habilitado con exito. ID: "+enabledTurn.getIdTurn()+" para "+enabledTurn.getServiceName()+".");
			return "redirect:/employee/turns/manage";//Redirige a la vista de gestion de turnos
		}catch (IllegalArgumentException | EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMesage","Ocurrio un error inesperado al habilitar el turno.");
			return "redirect:/employee/turns/enable";
		}
	}
	
	//Un controlador para habilitar multiples turnos
	@GetMapping("/turns/enable-multiple")
	public String showEnableMultipleTurnsForm(Model model) {
		model.addAttribute("employeeId",getCurrentLoggedInEmployeeId());
		return "employee/enable-multiple-turns";
	}
	@PostMapping("/turns/enable-multiple")
	public String enableMultipleTurns(@RequestParam("employeeId")int employeeId,
			@RequestParam("serviceId")int serviceId,@RequestParam("startDate")LocalDateTime startDate,
			@RequestParam("durationMinutes")int durationMinutes,RedirectAttributes redirectAttributes) {
		try {
			List<TurnDTO>createdTurns=turnService.enableMultipleTurns(employeeId, serviceId, startDate, startDate, durationMinutes);
			redirectAttributes.addFlashAttribute("successMessage"," Se habilitaron "+createdTurns.size()+" turnos existosamente");
			return "redirect:/employee/turns/manage";
		}catch (IllegalArgumentException | EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage"," Error al habilitar multiples turnos: "+e.getMessage());
			return "redirect:/employee/turns/enable-multiple";
		}
	}
}
