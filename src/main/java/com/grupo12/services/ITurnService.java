package com.grupo12.services;

import com.grupo12.models.TurnDTO;
import java.time.LocalDateTime;
import java.util.List;
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
	List<TurnDTO> enableMultipleTurns(int employeeId, int serviceId, LocalDateTime startDate, LocalDateTime endDate, int durationMinutes);
	
	//CU011: Obtener turnos para recordatorios
	List<TurnDTO> getUpComingTurnsForReminders(LocalDateTime fromTime, LocalDateTime toTime);
	
	List<TurnDTO> sendRemindersTo(List<Integer> turnIds);
	
	void deleteTurn(int id);
}
