package com.grupo12.services.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//import com.grupo12.models.TurnDTO;
import com.grupo12.services.ITurnService;
import com.grupo12.services.MailService;
import com.grupo12.converters.TurnConverter;
//import com.grupo12.converters.TurnConverter;
import com.grupo12.entities.Client;
import com.grupo12.entities.Date;
import com.grupo12.entities.Employee;
import com.grupo12.entities.ServiceEntity;
//import com.grupo12.entities.Service;
import com.grupo12.entities.Turn;
import com.grupo12.entities.TurnStatus;
import com.grupo12.exceptions.MissingTurnDataException;
import com.grupo12.models.TurnDTO;
import com.grupo12.models.TurnMultipleDTO;
import com.grupo12.repositories.IClientRepository;
import com.grupo12.repositories.IDateRepository;
import com.grupo12.repositories.IEmployeeRepository;
import com.grupo12.repositories.IServiceRepository;
import com.grupo12.repositories.ITurnRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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
	private IDateRepository dateRepository;
	
	@Autowired
	private TurnConverter turnConverter;
	
	@Autowired
	private MailService mailService;
	
	@Override
	public List<Turn> GetAvailableAppointments() {
		 return turnRepository.findByStatus(TurnStatus.PENDIENTE);
	}

	/*@Override
=======
	@Override
>>>>>>> b23e90e (Agrego casos de uso)
	public void reserveTurn(Integer idTurno, String username) {
        Turn turn = turnRepository.findById(idTurno).orElseThrow();
        Client client = clientRepository.findByUserUsername(username).orElseThrow(); // según cómo manejes login

        turn.setClient(client);
        turn.setStatus(TurnStatus.PENDIENTE);
        turnRepository.save(turn);
		
	}*/
    
	/*@Override
	public Turn requestAnAppointment(Long idClient, Long idService, LocalDate date) {
=======
	@Override
	public Turn requestAnAppointment(Long idClient, Integer idService, LocalDate date) {
>>>>>>> b23e90e (Agrego casos de uso)
        Client client = clientRepository.findById(idClient)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        ServiceEntity service = serviceRepository.findById(idService)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        
        Turn turn = new Turn();
        turn.setClient(client);
        turn.setService(service);
        turn.setDate(turn.getDate());
        turn.setStatus(TurnStatus.PENDIENTE);

        return turnRepository.save(turn);
	}*/
	
	@Override
	public List<Turn> getAvailableTurns() {
		return turnRepository.findAvailableTurns();
	}

	//Sirve para un cliente logueado
	/*@Override
	public void reserveAppointment(Long idTurno, String username) {
=======
	@Override
	public void reserveAppointment(Integer idTurno, String username) {
>>>>>>> b23e90e (Agrego casos de uso)
        Turn turn = turnRepository.findById(idTurno).orElseThrow();
        Client client = clientRepository.findByUserUsername(username).orElseThrow(); // según cómo manejes login

        turn.setClient(client);
        turn.setStatus(TurnStatus.PENDIENTE);
        turnRepository.save(turn);
		
	}*/
	
    /*public Optional<Turn> getTurnById(int id) {
        return turnRepository.findById(id);
    }*/

    public void assignTurnToClient(Turn turn, Client client) {
        turn.setClient(client);
        turn.setStatus(TurnStatus.PENDIENTE); // asegurás el estado
        turnRepository.save(turn);
    }

	@Override
	public List<Turn> getTurnsByClient(Client client) {
		return turnRepository.findByClient(client);
	}

	@Override
	public Map<String, List<Turn>> getTurnsGroupedByStatus(Client client) {
        List<Turn> allTurns = turnRepository.findByClient(client);
        return allTurns.stream().collect(Collectors.groupingBy(t -> {
            switch (t.getStatus()) {
                case PENDIENTE, EN_ATENCION: return "Activos";
                case ATENDIDO: return "Pasados";
                case CANCELADO, AUSENTE: return "Cancelados";
                default: return "Otros";
            }
        }));
	}
	
	/*@Override
	public Map<String, List<Turn>> getTurnsGroupedByStatus(Client client) {
	    List<Turn> allTurns = turnRepository.findByClient(client);

	    Map<String, List<Turn>> grouped = new LinkedHashMap<>();
	    grouped.put("Activos", new ArrayList<>());
	    grouped.put("Pasados", new ArrayList<>());
	    grouped.put("Cancelados", new ArrayList<>());
	    grouped.put("Otros", new ArrayList<>());

	    for (Turn t : allTurns) {
	        switch (t.getStatus()) {
	            case PENDIENTE, EN_ATENCION -> grouped.get("Activos").add(t);
	            case ATENDIDO -> grouped.get("Pasados").add(t);
	            case CANCELADO, AUSENTE -> grouped.get("Cancelados").add(t);
	            default -> grouped.get("Otros").add(t);
	        }
	    }

	    return grouped;
	}*/

	@Override
	public void save(Turn turno) {
		turnRepository.save(turno);
	}



	
		@Override
		public Optional<TurnDTO> getTurnDTOById(int id) {
			return turnRepository.findById(id).map(turnConverter::toDTO);
		}
		@Override
		public List<TurnDTO> getAllTurns(){
			return turnRepository.findAll().stream().map(turnConverter::toDTO).collect(Collectors.toList());
		}
		
		@Override
		public List<TurnDTO>getTurnsByEmployeeId(int employeeId){
			return turnRepository.findByEmployeeIdPersonOrderByStartTimeAsc(employeeId).stream().map(turnConverter::toDTO).collect(Collectors.toList());
		}
		@Override
		public List<TurnDTO>getTurnsByClientId(int clientId){
			return turnRepository.findByClientIdPersonOrderByStartTimeAsc(clientId).stream().map(turnConverter::toDTO).collect(Collectors.toList());
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
		Optional<Turn>nextTurn=turnRepository.findNextPendingTurnByEmployeeId(employeeId,PageRequest.of(0, 1)).stream().findFirst();
		if(nextTurn.isPresent()) {
			Turn turnToCall=nextTurn.get();
			turnToCall.setStatus(TurnStatus.EN_ATENCION);//Cambiar estado a "en atencion"
			Turn updatedTurn=turnRepository.save(turnToCall);
			return turnConverter.toDTO(updatedTurn);
		}else {
			throw new IllegalStateException("No hay turnos pendientes para el empleado con ID: "+ employeeId);
		}
	}


	@Override
	public void reserveTurn(Integer idTurno, String username) {
		// TODO Auto-generated method stub
		
	}

	
	// Solo para pruebas unitarias
	public void setTurnRepository(ITurnRepository turnRepository) {
	    this.turnRepository = turnRepository;
	}

	public void setEmployeeRepository(IEmployeeRepository employeeRepository) {
	    this.employeeRepository = employeeRepository;
	}

	public void setTurnConverter(TurnConverter turnConverter) {
	    this.turnConverter = turnConverter;
	}
	public void setServiceRepository(IServiceRepository serviceRepository) {
	    this.serviceRepository = serviceRepository;
	}
	
	@Override
	@Transactional
	public TurnDTO createTurn(TurnDTO turnDTO) {
		// TODO Auto-generated method stub
		//Este metodo se usa para que un Cliente pida un turno, o para que un Admin lo cree con un Cliente
		//Los turnos habilitados por empleados(CU009) No tendran ClienteID al inicio
		if(turnDTO.getServiceId()==null||turnDTO.getStartTime()==null) {
			throw new IllegalArgumentException("Servicio y hora de inicio son obligatorios!!!");
		}
		Client client =null;
		if(turnDTO.getClientIdPerson() != null) {
			client=clientRepository.findById(turnDTO.getClientIdPerson()).orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: "+turnDTO.getClientIdPerson()));
		}
		com.grupo12.entities.ServiceEntity service=serviceRepository.findById(turnDTO.getServiceId().longValue()).orElseThrow(()-> new EntityNotFoundException("Servicio no encontrado con ID: "+turnDTO.getServiceId()));
		
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
	
		/*@Override
		public Optional<TurnDTO> getTurnById(Integer id) {
			return turnRepository.findById(id).map(turnConverter::toDTO);
		}*/
	
		
		//CU010: Actualizar estado de un turno 
		@Override
		@Transactional
		public TurnDTO updateTurnStatus(Integer id,String newStatus) {
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
	


    //CU009: Habilitar un turno en una franja horaria
	public TurnDTO enableSingleTurn(TurnDTO turnDTO) {
	    
	    if (turnDTO.getEmployeeIdPerson() <= 0 || turnDTO.getServiceId() == null || turnDTO.getStartTime() == null) {
	        throw new MissingTurnDataException("Empleado, servicio y hora de inicio son obligatorios para habilitar un turno!!");
	    }

	    Employee employee = employeeRepository.findById(turnDTO.getEmployeeIdPerson())
	            .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + turnDTO.getEmployeeIdPerson()));
	    ServiceEntity service = serviceRepository.findById(turnDTO.getServiceId().longValue())
	            .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado con ID: " + turnDTO.getServiceId()));

	    LocalDate date = turnDTO.getStartTime().toLocalDate();
	    LocalTime hour = turnDTO.getStartTime().toLocalTime();

	    Date findDate = dateRepository.findByDateAndHour(date, hour)
	            .orElseGet(() -> {
	                Date newDate = new Date();
	                newDate.setDate(date);
	                newDate.setHour(hour);
	                return dateRepository.save(newDate);
	            });

	    Turn turn = new Turn();
	    turn.setEmployee(employee);
	    turn.setService(service);
	    turn.setStartTime(turnDTO.getStartTime());
	    turn.setEndTime(turnDTO.getStartTime().plusMinutes(service.getDurationMinutes()));
	    turn.setDate(findDate);
	    turn.setObservation("Turno disponible");
	    turn.setActive(true);
	    turn.setReminderSent(false);
	    turn.setStatus(TurnStatus.PENDIENTE);
	    turn.setCreationTime(LocalDateTime.now());

	    // Validar solapamiento con otros turnos
	    List<Turn> conflictingTurns = turnRepository.findConflictingTurnForEmployee(employee.getIdPerson(), turn.getStartTime(), turn.getEndTime());
	    if (!conflictingTurns.isEmpty()) {
	        throw new IllegalArgumentException("Ya existe un turno o disponibilidad para el empleado en este horario: " + conflictingTurns.get(0).getIdTurn());
	    }

	    Turn savedTurn = turnRepository.save(turn);
	    TurnDTO dto = turnConverter.toDTO(savedTurn);
	    dto.setServiceName(service.getName());
	    return dto;
	}


	//CU009: Habilitar multiples turnos en una franja horaria
	@Override
	@Transactional
	public List<TurnDTO> enableMultipleTurns(TurnDTO dto) {
		// TODO Auto-generated method stub
		int employeeId = dto.getEmployeeIdPerson();
	    Integer serviceId = dto.getServiceId();
	    LocalDateTime startDate = dto.getStartTime();
	    LocalDateTime endDate = dto.getEndTime();
	    int durationMinutes = dto.getDurationMinutes();

		    Employee employee = employeeRepository.findById(employeeId)
		        .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + employeeId));

		    com.grupo12.entities.ServiceEntity service = serviceRepository.findByIdService(serviceId.longValue())
		        .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado con ID: " + serviceId));

		    if (durationMinutes <= 0) {
		        throw new IllegalArgumentException("La duración debe ser positiva.");
		    }

		    if (startDate.isAfter(endDate)) {
		        throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la de fin.");
		    }

		    List<TurnDTO> createdTurns = new ArrayList<>();
		    LocalDateTime currentTurnStart = startDate;

		    while (!currentTurnStart.plusMinutes(durationMinutes).isAfter(endDate)) {
		        LocalDateTime currentTurnEnd = currentTurnStart.plusMinutes(durationMinutes);

		        List<Turn> conflictingTurns = turnRepository
		            .findConflictingTurnForEmployee(employee.getIdPerson(), currentTurnStart, currentTurnEnd);

		        if (conflictingTurns.isEmpty()) {
		            Turn turn = new Turn();
		            turn.setEmployee(employee);
		            turn.setService(service);
		            turn.setStartTime(currentTurnStart);
		            turn.setEndTime(currentTurnEnd);
		    	    turn.setObservation("Turno disponible");
		    	    turn.setActive(true);
		    	    turn.setReminderSent(false);
		            turn.setStatus(TurnStatus.PENDIENTE);
		            turn.setCreationTime(LocalDateTime.now());
		            
		            // 👉 AÑADIR DATE
		            LocalDate date = currentTurnStart.toLocalDate();
		            LocalTime hour = currentTurnStart.toLocalTime();

		            Date dateEntity = dateRepository.findByDateAndHour(date, hour)
		                .orElseGet(() -> {
		                    Date newDate = new Date();
		                    newDate.setDate(date);
		                    newDate.setHour(hour);
		                    return dateRepository.save(newDate);
		                });

		            turn.setDate(dateEntity);
		            

		            Turn savedTurn = turnRepository.save(turn);
		            TurnDTO dtoTurn = turnConverter.toDTO(savedTurn);
		            dtoTurn.setServiceName(service.getName());

		            createdTurns.add(dtoTurn);
		        } else {
		            System.out.println("Saltando turno en " + currentTurnStart + " por conflicto.");
		        }

		        currentTurnStart = currentTurnEnd;
		    }

		    return createdTurns;
	}
	

	//CU011: Obtener turnos para recordatorios
	//getUpComingTurnsForReminders
	@Override
	public List<TurnDTO> findUpcomingTurns(LocalDateTime fromTime, LocalDateTime toTime) {
	    List<TurnStatus> statusForReminder = Arrays.asList(TurnStatus.PENDIENTE, TurnStatus.EN_ATENCION);
	    List<Turn> upcomingTurns = turnRepository.findByStartTimeBetweenAndStatusIn(fromTime, toTime, statusForReminder).stream()
	    	.filter(t -> t.getReminderSent() == null || !t.getReminderSent())
	    	    .collect(Collectors.toList());;

	    return upcomingTurns.stream()
	            .map(turnConverter::toDTO)
	            .collect(Collectors.toList());
	}
	/*	@Override
	public List<TurnDTO> findUpcomingTurns(LocalDateTime fromTime, LocalDateTime toTime) {
		// TODO Auto-generated method stub
		List<TurnStatus> statusForReminder = Arrays.asList(TurnStatus.PENDIENTE, TurnStatus.EN_ATENCION);
	    List<Turn> upcomingTurns = turnRepository.findByStartTimeBetweenAndStatusIn(fromTime, toTime, statusForReminder);

	    List<TurnDTO> dtos = new ArrayList<>();

	    for (Turn turn : upcomingTurns) {
	        if (turn.getClient() != null && turn.getClient().getContact().getEmail() != null) {
	            String to = turn.getClient().getContact().getEmail();
	            String subject = "Recordatorio de turno";
	            String body = "Estimado/a " + turn.getClient().getName() +
	                    ",\n\nLe recordamos que tiene un turno el día " + turn.getStartTime() +
	                    ".\n\nGracias.";

	            // Enviar el mail
	            mailService.sendSimpleMail(to, subject, body);

	            // Marcar como enviado
	            turn.setReminderSent(true);
	            turnRepository.save(turn); // ✅ guardar el cambio
	        }

	        dtos.add(turnConverter.toDTO(turn));
	    }

	    return dtos;
		
		/* List<TurnStatus> statusForReminder = Arrays.asList(TurnStatus.PENDIENTE, TurnStatus.EN_ATENCION);
		 List<Turn> upcomingTurns = turnRepository.findByStartTimeBetweenAndStatusIn(fromTime, toTime, statusForReminder);

		    List<TurnDTO> dtos = new ArrayList<>();

		    for (Turn turn : upcomingTurns) {
		        TurnDTO dto = turnConverter.toDTO(turn);

		        // Verificamos que tenga cliente y mail
		        if (turn.getClient() != null && turn.getClient().getContact().getEmail() != null) {
		            String to = turn.getClient().getContact().getEmail();
		            String subject = "Recordatorio de turno";
		            String body = "Estimado/a " + turn.getClient().getName() +
		                    ",\n\nLe recordamos que tiene un turno el día " + turn.getStartTime() +
		                    ".\n\nGracias.";

		            // Envía el mail
		            mailService.sendSimpleMail(to, subject, body);
		        }

		        dtos.add(dto);
		    }

		    return dtos;*/
		 //  }*/

	/*@Override
	public List<TurnDTO> sendRemindersTo(List<Integer> turnIds) {
		List<TurnDTO> enviados = new ArrayList<>();
	    for (int id : turnIds) {
	        Turn turn = turnRepository.findById(id).orElse(null);
	        if (turn != null && turn.getClient() != null && turn.getClient().getContact().getEmail() != null) {
	            String to = turn.getClient().getContact().getEmail();
	            String subject = "Recordatorio de turno";
	            String body = "Hola " + turn.getClient().getName() + ", tenés un turno el " + turn.getStartTime();

	            // Enviar el mail
	            mailService.sendSimpleMail(to, subject, body);

	            // Marcar como recordatorio enviado
	            turn.setReminderSent(true);
	            turnRepository.save(turn); // ✅ persistir el cambio

	            // Agregar a la lista de enviados
	            enviados.add(turnConverter.toDTO(turn));
	        }
	    }
	    return enviados;
		List<TurnDTO> enviados = new ArrayList<>();
	    for (int id : turnIds) {
	        Turn turn = turnRepository.findById(id).orElse(null);
	        if (turn != null && turn.getClient() != null && turn.getClient().getContact().getEmail() != null) {
	            String to = turn.getClient().getContact().getEmail();
	            String subject = "Recordatorio de turno";
	            String body = "Hola " + turn.getClient().getName() + ", tenés un turno el " + turn.getStartTime();
	            mailService.sendSimpleMail(to, subject, body);
	            enviados.add(turnConverter.toDTO(turn));
	        }
	    }
	    return enviados;
	}*/
	@Override
	public void deleteTurn(int id) {
		// TODO Auto-generated method stub
		turnRepository.deleteById(id);

	}

	@Override
	@Transactional
	public TurnDTO reserveTurn(TurnDTO turnDTO, String username) {

	    // Validaciones para habilitar un turno
	    if (turnDTO.getEmployeeIdPerson() == null || turnDTO.getServiceId() == null || turnDTO.getStartTime() == null) {
	        throw new IllegalArgumentException("Empleado, servicio y hora de inicio son obligatorios para habilitar un turno!!");
	    }

	    Employee employee = employeeRepository.findById(turnDTO.getEmployeeIdPerson())
	        .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + turnDTO.getEmployeeIdPerson()));

	    ServiceEntity service = serviceRepository.findById(turnDTO.getServiceId().longValue())
	        .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado con ID: " + turnDTO.getServiceId()));

	    Turn turn = new Turn();
	    turn.setEmployee(employee);
	    turn.setService(service);
	    turn.setStartTime(turnDTO.getStartTime());
	    turn.setEndTime(turnDTO.getStartTime().plusMinutes(service.getDurationMinutes()));
	    turn.setStatus(TurnStatus.PENDIENTE);
	    turn.setCreationTime(LocalDateTime.now());
	    // Cliente aún no se asigna

	    // Validar solapamiento
	    List<Turn> conflictingTurns = turnRepository.findConflictingTurnForEmployee(
	        employee.getIdPerson(), turn.getStartTime(), turn.getEndTime());

	    if (!conflictingTurns.isEmpty()) {
	        throw new IllegalArgumentException("Ya existe un turno o disponibilidad para el empleado en este horario: "
	            + conflictingTurns.get(0).getIdTurn());
	    }

	    Turn savedTurn = turnRepository.save(turn);
	    TurnDTO dto = turnConverter.toDTO(savedTurn);
	    dto.setServiceName(service.getName());

	    return dto;
	}


	//CU009: Habilitar multiples turnos en una franja horaria
	@Override
	@Transactional
	public List<TurnDTO> enableMultipleTurns(Integer employeeId, Long serviceId, LocalDateTime startDate,
			LocalDateTime endDate, int durationMinutes) {
		// TODO Auto-generated method stub
		Employee employee=employeeRepository.findById(employeeId).orElseThrow(()-> new EntityNotFoundException("Empleado no encontrado con ID: "+employeeId));
		com.grupo12.entities.ServiceEntity service=serviceRepository.findByIdService(serviceId).orElseThrow(()-> new EntityNotFoundException("Servicio no encontrado con ID: "+serviceId));
		
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
				turn.setCreatedAt(LocalDateTime.now());
				Turn savedTurn=turnRepository.save(turn);
				TurnDTO dto = turnConverter.toDTO(savedTurn);
				dto.setServiceName(service.getName());
				createdTurns.add(dto);
			}
			currentTurnStart=currentTurnEnd; //Avanzar al proximo inicio de turno
		}
		
		return createdTurns;
	}

	//CU011: Obtener turnos para recordatorios
	@Override
	public List<TurnDTO> getUpComingTurnsForReminders(LocalDateTime fromTime, LocalDateTime toTime) {
		// TODO Auto-generated method stub
		
		
		 List<TurnStatus> statusForReminder = Arrays.asList(TurnStatus.PENDIENTE, TurnStatus.EN_ATENCION);
		 List<Turn> upcomingTurns = turnRepository.findByStartTimeBetweenAndStatusIn(fromTime, toTime, statusForReminder);

		    List<TurnDTO> dtos = new ArrayList<>();

		    for (Turn turn : upcomingTurns) {
		        TurnDTO dto = turnConverter.toDTO(turn);

		        // Verificamos que tenga cliente y mail
		        if (turn.getClient() != null && turn.getClient().getUser().getEmail() != null) {
		            String to = turn.getClient().getUser().getEmail();
		            String subject = "Recordatorio de turno";
		            String body = "Estimado/a " + turn.getClient().getName() +
		                    ",\n\nLe recordamos que tiene un turno el día " + turn.getStartTime() +
		                    ".\n\nGracias.";

		            // Envía el mail
		            mailService.sendSimpleMail(to, subject, body);
		        }

		        dtos.add(dto);
		    }

		    return dtos;
		   }

	@Override
	public List<TurnDTO> sendRemindersTo(List<Integer> turnIds) {
	    List<TurnDTO> enviados = new ArrayList<>();
	    for (int id : turnIds) {
	        Turn turn = turnRepository.findById(id).orElse(null);
	        if (turn != null && turn.getClient() != null && turn.getClient().getUser().getEmail() != null) {
	            String to = turn.getClient().getUser().getEmail();
	            String subject = "Recordatorio de turno";
	            String body = "Hola " + turn.getClient().getName() + ", tenés un turno el " + turn.getStartTime();
	            mailService.sendSimpleMail(to, subject, body);
	            enviados.add(turnConverter.toDTO(turn));
	        }
	    }
	    return enviados;
	}


	@Override
	public Optional<Turn> getTurnById(Integer id) {
		return turnRepository.findById(id);
	}

	@Override
	public List<TurnDTO> enableMultipleTurns(int employeeId, int serviceId, LocalDateTime startDate,
			LocalDateTime endDate, int durationMinutes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reserveAppointment(Integer idTurno, String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Turn requestAnAppointment(Long idClient, Integer idService, LocalDate date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reserveTurn(Long idTurno, String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ServiceEntity> getAllServicios() {
		return serviceRepository.findAll();
	}


}

