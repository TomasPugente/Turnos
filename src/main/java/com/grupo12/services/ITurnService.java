package com.grupo12.services;

import com.grupo12.entities.Client;
import com.grupo12.entities.Turn;
import com.grupo12.models.TurnDTO;
import com.grupo12.models.TurnMultipleDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface ITurnService {
	TurnDTO createTurn(TurnDTO turnDTO);
	Optional<TurnDTO> getTurnById(int id);
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
	List<TurnDTO> enableMultipleTurns(TurnMultipleDTO dto);
	
	//CU011: Obtener turnos para recordatorios
	//List<TurnDTO> getUpComingTurnsForReminders(LocalDateTime fromTime, LocalDateTime toTime);
	
	List<TurnDTO> sendRemindersTo(List<Integer> turnIds);
	
	void deleteTurn(int id);
	List<TurnDTO> findUpcomingTurns(LocalDateTime fromTime, LocalDateTime toTime);
	
	//public Turn requestAnAppointment(Long idClient, Long idService, LocalDate date);
	
	public List<Turn> GetAvailableAppointments(); 
	
	public void reserveTurn(Integer idTurno, String username);

	public List<Turn> getAvailableTurns();
	
	//public void reserveAppointment(Long idTurno, String username);

    
    void save(Turn turno);
	Optional<TurnDTO> getTurnById(Integer id);
	


	//public Turn requestAnAppointment(Long idClient, Long idService, LocalDate date);
	
	public void reserveTurn(Long idTurno, String username);
	
	//public void reserveAppointment(Long idTurno, String username);
	
	
	public void assignTurnToClient(Turn turn, Client client);
	
    List<Turn> getTurnsByClient(Client client);
    
    Map<String, List<Turn>> getTurnsGroupedByStatus(Client client);
    

	
	
}

