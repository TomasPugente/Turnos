package com.grupo12.services.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.grupo12.models.TurnDTO;
import com.grupo12.services.ITurnService;

import com.grupo12.converters.TurnConverter;
import com.grupo12.entities.Client;
import com.grupo12.entities.Employee;
//import com.grupo12.entities.Service;
import com.grupo12.entities.Turn;
import com.grupo12.entities.TurnStatus;
import com.grupo12.repositories.IClientRepository;
import com.grupo12.repositories.IEmployeeRepository;
import com.grupo12.repositories.IServiceRepository;
import com.grupo12.repositories.ITurnRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TurnService implements ITurnService {

	@Autowired
	private ITurnRepository turnRepository;
	@Autowired
	private IClientRepository clientRepository;
	@Autowired
	private IEmployeeRepository employeeRepository;
	@Autowired
	private IServiceRepository serviceRepository;
	@Autowired
	private TurnConverter turnConverter;
	
	@Override
	@Transactional
	public TurnDTO createTurn(TurnDTO turnDTO) {
		// TODO Auto-generated method stub
		//Este metodo se usa para que un Cliente pida un turno, o para que un Admin lo cree con un Cliente
		//Los turnos habilitados por empleados(CU009) No tendran ClienteID al inicio
		if(turnDTO.getIdService()==null||turnDTO.getStartTime()==null) {
			throw new IllegalArgumentException("Servicio y hora de inicio son obligatorios!!!");
		}
		Client client =null;
		if(turnDTO.getClientIdPerson() != null) {
			client=clientRepository.findById(turnDTO.getClientIdPerson()).orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: "+turnDTO.getClientIdPerson()));
		}
		com.grupo12.entities.Service service=serviceRepository.findById(turnDTO.getIdService()).orElseThrow(()-> new EntityNotFoundException("Servicio no encontrado con ID: "+turnDTO.getIdService()));
		
		Employee employee=null;
		if(turnDTO.getEmployeeIdPerson()!=null) {
			employee=employeeRepository.findById(turnDTO.getEmployeeIdPerson()).orElseThrow(()-> new EntityNotFoundException("Empleado no encontrado con ID: "+turnDTO.getEmployeeIdPerson()));
		}
		
		Turn turn=turnConverter.toEntity(turnDTO);
		turn.setClient(client); //Puede ser null si es un turno habilitado para ser tomado
		turn.setService(service);
		turn.setEmployee(employee);
		
		turn.setCreationTime(LocalDateTime.now());
		turn.setEndTime(turn.getStartTime().plusMinutes(service.getDurationMinutes()));
		
		//Para turno que se crean por solicitud de cliente, el estado inicial es PENDIENTE
		if(turn.getStatus()==null) {
			//Si el DTO no trae un estado, se asume PENDIENTE
			turn.setStatus(TurnStatus.PENDIENTE);
		}
		
		//Validacion de solapamiento para el empleado si esta asignado
		if(employee!=null) {
			List<Turn> conflictingTurns=turnRepository.findConflictingTurnForEmployee(employee.getIdPerson(), turn.getStartTime(), turn.getEndTime());
			if(!conflictingTurns.isEmpty())
			{
				throw new IllegalArgumentException("El empleado ya tiene un turno programado que se solapa en este horario!!");
			}
		}
		Turn savedTurn=turnRepository.save(turn);
		return turnConverter.toDTO(savedTurn);
	}
	
		@Override
		public Optional<TurnDTO> getTurnById(int id) {
			return turnRepository.findById(id).map(turnConverter::toDTO);
		}
		@Override
		public List<TurnDTO> getAllTurns(){
			return turnRepository.findAll().stream().map(turnConverter::toDTO).collect(Collectors.toList());
		}
		
		@Override
		public List<TurnDTO>getTurnsByEmployeeId(int employeeId){
			return turnRepository.findByEmployeeIdOrderByStartTimeAsc(employeeId).stream().map(turnConverter::toDTO).collect(Collectors.toList());
		}
		@Override
		public List<TurnDTO>getTurnsByClientId(int clientId){
			return turnRepository.findByClientIdOrderByStartTimeAsc(clientId).stream().map(turnConverter::toDTO).collect(Collectors.toList());
		}
		
		//CU010: Actualizar estado de un turno 
		@Override
		@Transactional
		public TurnDTO updateTurnStatus(int id,String newStatus) {
			Turn turn=turnRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Turno no encontrado con ID: "+id));
			try {
				TurnStatus status=TurnStatus.valueOf(newStatus.toUpperCase());
				//Validaciones de transicion de estado 
				if(turn.getStatus()==TurnStatus.ATENDIDO || turn.getStatus()==TurnStatus.AUSENTE || turn.getStatus()==TurnStatus.CANCELADO) {
					if(status!=TurnStatus.ATENDIDO&&status!=TurnStatus.AUSENTE&&status!=TurnStatus.CANCELADO) {
						throw new IllegalStateException("No se puede cambiar el estado de un turno ya finalizado!!");
					}
				}
				turn.setStatus(status);
				Turn updatedTurn=turnRepository.save(turn);
				return turnConverter.toDTO(updatedTurn);
			}catch(IllegalArgumentException e) {
				throw new IllegalArgumentException("Estado de turno invalido: "+newStatus);
			}
		}
	

	
	//CU 008: Llamar el proximo turno paara un empleado
	@Transactional
	@Override
	public TurnDTO callNextTurnForEmployee(int employeeId) {
		// TODO Auto-generated method stub
		//Validar que el empleado existe
		employeeRepository.findById(employeeId).orElseThrow(()->new EntityNotFoundException("Empleado no encontrado con ID: "+employeeId));
		
		//Buscar el proximo turno PENDIENTE para este empleado
		Optional<Turn>nextTurn=turnRepository.findNextPendingTurnByEmployeeId(employeeId);
		if(nextTurn.isPresent()) {
			Turn turnToCall=nextTurn.get();
			turnToCall.setStatus(TurnStatus.EN_ATENCION);//Cambiar estado a "en atencion"
			Turn updatedTurn=turnRepository.save(turnToCall);
			return turnConverter.toDTO(updatedTurn);
		}else {
			throw new IllegalStateException("No hay turnos pendientes para el empleado con ID: "+ employeeId);
		}
	}

	//CU009: Habilitar un solo turno 
	@Override
	public TurnDTO enableSingleTurn(TurnDTO turnDTO) {
		// TODO Auto-generated method stub
		//Validaciones para habilitar un turno
		if(turnDTO.getEmployeeIdPerson()==null || turnDTO.getIdService()==null || turnDTO.getStartTime()==null) {
			throw new IllegalArgumentException("Empleado, servicio y hora de inicio son obligatorios para habilitar un turno!!");
		}
		Employee employee=employeeRepository.findById(turnDTO.getEmployeeIdPerson()).orElseThrow(()->new EntityNotFoundException("Empleado no encontrado con ID: "+turnDTO.getEmployeeIdPerson()));
		com.grupo12.entities.Service service=serviceRepository.findById(turnDTO.getIdService()).orElseThrow(()-> new EntityNotFoundException("Servicio no encontrado con ID: "+turnDTO.getIdService()));
		Turn turn=new Turn();
		turn.setEmployee(employee);
		turn.setService(service);
		turn.setStartTime(turnDTO.getStartTime());
		turn.setEndTime(turnDTO.getStartTime().plusMinutes(service.getDurationMinutes()));
		turn.setStatus(TurnStatus.PENDIENTE); //Los turnos habilitados inician como PENDIENTE
		
		turn.setCreationTime(LocalDateTime.now());
		//No se asigna cliente en este punto. el Cliente lo toma despues
		
		//Validar solapamiento con otros turnos del empleado
		List<Turn> conflictingTurns=turnRepository.findConflictingTurnForEmployee(employee.getIdPerson(), turn.getStartTime(), turn.getEndTime());
		if(!conflictingTurns.isEmpty()) {
			throw new IllegalArgumentException("Ya existe un turno o disponibilidad para el empleado en este horario: "+conflictingTurns.get(0).getIdTurn());
		}
		
		Turn savedTurn=turnRepository.save(turn);
		
		return turnConverter.toDTO(savedTurn);
	}

	//CU009: Habilitar multiples turnos en una franja horaria
	@Override
	@Transactional
	public List<TurnDTO> enableMultipleTurns(int employeeId, int serviceId, LocalDateTime startDate,
			LocalDateTime endDate, int durationMinutes) {
		// TODO Auto-generated method stub
		Employee employee=employeeRepository.findById(employeeId).orElseThrow(()-> new EntityNotFoundException("Empleado no encontrado con ID: "+employeeId));
		com.grupo12.entities.Service service=serviceRepository.findById(serviceId).orElseThrow(()-> new EntityNotFoundException("Servicio no encontrado con ID: "+serviceId));
		
		if(durationMinutes<=0) {
			throw new IllegalArgumentException("La duracion del turno debe ser un numero positivo de minutos!");
		}
		if(startDate.isAfter(endDate)) {
			throw new IllegalArgumentException("La fecha de incio no puede ser posterior a la fecha de Fin!");
		}
		List<TurnDTO> createdTurns=new ArrayList<>();
		LocalDateTime currentTurnStart=startDate;
		
		while(currentTurnStart.plusMinutes(durationMinutes).isBefore(endDate)|| currentTurnStart.plusMinutes(durationMinutes).isEqual(endDate)) {
			LocalDateTime currentTurnEnd=currentTurnStart.plusMinutes(durationMinutes);
			//Validar solapamiento antes de crear
			List<Turn> conflictingTurns=turnRepository.findConflictingTurnForEmployee(employee.getIdPerson(), currentTurnStart, currentTurnEnd);
			if(!conflictingTurns.isEmpty()) {
				System.out.println("Saltando turno en "+currentTurnStart + " debido a conflicto");
			}else {
				Turn turn=new Turn();
				turn.setEmployee(employee);
				turn.setService(service);
				turn.setStartTime(currentTurnStart);
				turn.setEndTime(currentTurnEnd);
				turn.setStatus(TurnStatus.PENDIENTE);
				turn.setCreationTime(LocalDateTime.now());
				Turn savedTurn=turnRepository.save(turn);
				createdTurns.add(turnConverter.toDTO(savedTurn));
			}
			currentTurnStart=currentTurnEnd; //Avanzar al proximo inicio de turno
		}
		
		return createdTurns;
	}

	//CU011: Obtener turnos para recordatorios
	@Override
	public List<TurnDTO> getUpComingTurnsForReminders(LocalDateTime fromTime, LocalDateTime toTime) {
		// TODO Auto-generated method stub
		List<TurnStatus> statusForReminder=Arrays.asList(TurnStatus.PENDIENTE,TurnStatus.EN_ATENCION);
		return turnRepository.findByStartTimeBetweenAndStatusIn(fromTime,toTime,statusForReminder).stream().map(turnConverter::toDTO).collect(Collectors.toList());
	}

	@Override
	public void deleteTurn(int id) {
		// TODO Auto-generated method stub
		turnRepository.deleteById(id);

	}

}
