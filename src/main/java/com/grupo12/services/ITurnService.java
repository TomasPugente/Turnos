package com.grupo12.services;

import java.util.Map;
import java.util.Optional;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.grupo12.entities.Client;
import com.grupo12.entities.Turn;


import com.grupo12.models.TurnDTO;
import com.grupo12.models.TurnMultipleDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface ITurnService {
	
	//public Turn requestAnAppointment(Long idClient, Long idService, LocalDate date);
	
	public List<Turn> GetAvailableAppointments(); 
	
	public void reserveTurn(Integer idTurno, String username);
	
	//@Query("SELECT t FROM Turn t WHERE TYPE(t.employee) = Employee")
	public List<Turn> getAvailableTurns();
	
	//public void reserveAppointment(Long idTurno, String username);
	
	public void assignTurnToClient(Turn turn, Client client);
	
    List<Turn> getTurnsByClient(Client client);
    
    Map<String, List<Turn>> getTurnsGroupedByStatus(Client client);
    
    void save(Turn turno);
	
	
	TurnDTO createTurn(TurnDTO turnDTO);
	//Optional<Turn> getTurnById(Integer id);
	List<TurnDTO> getAllTurns();
	List<TurnDTO> getTurnsByEmployeeId(int employeeId);
	List<TurnDTO> getTurnsByClientId(int clientId);
	
	//CU0010: Actualizar estado de un turno 
	TurnDTO updateTurnStatus(int id,String newStatus);
	
	//CU008: Llamar el proximo turno para un empleado
	TurnDTO callNextTurnForEmployee(int employeeId);
	
	//CU009: Habilitar un solo turno(para crear disponibilidad)
	TurnDTO enableSingleTurn(TurnDTO turnDTO);
	//Metodo para habilitar multiples turnos/franjas
	List<TurnDTO> enableMultipleTurns(Integer employeeId, Long serviceId, LocalDateTime startDate, LocalDateTime endDate, int durationMinutes);
	
	//CU011: Obtener turnos para recordatorios
	List<TurnDTO> getUpComingTurnsForReminders(LocalDateTime fromTime, LocalDateTime toTime);
	
	List<TurnDTO> sendRemindersTo(List<Integer> turnIds);
	
	void deleteTurn(int id);

	void reserveAppointment(Integer idTurno, String username);

	Optional<Turn> getTurnById(Integer id);
	
	public Optional<TurnDTO> getTurnDTOById(int id);

	Turn requestAnAppointment(Long idClient, Integer idService, LocalDate date);

	public List<TurnDTO> enableMultipleTurns(int employeeId, int serviceId, LocalDateTime startDate,
			LocalDateTime endDate, int durationMinutes);

	List<TurnDTO> enableMultipleTurns(TurnMultipleDTO dto);

	List<TurnDTO> findUpcomingTurns(LocalDateTime fromTime, LocalDateTime toTime);

	void reserveTurn(Long idTurno, String username);

	TurnDTO reserveTurn(TurnDTO turnDTO, String username); 
    
}

