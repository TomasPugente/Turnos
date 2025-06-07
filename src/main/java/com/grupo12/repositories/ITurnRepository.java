package com.grupo12.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.grupo12.entities.Turn;
import com.grupo12.entities.TurnStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ITurnRepository extends JpaRepository<Turn, Integer> {
	//PARA CU008: Buscar turnos pendientes por empleado, ordenados por hora de inicio
	List<Turn> findByEmployeddIdAndStatusOrderByStartTimeAsc(int employeeId, TurnStatus status);
	//PARA CU008: Buscar el proximo urno pendiente para un empleado
	@Query("SELECT t FROM Turn t WHERE t.employee.id = :employeeId AND t.status = 'PENDIENTE' ORDER BY t.startTime ASC LIMIT 1") 
	Optional<Turn> findNextPendingTurnByEmployeeId(int employeeId);
	
	//PARA CU009 (validacion de disponibilidad): Comprobar solapamientos 
	@Query("SELECT t FROM Turn t WHERE t.employeed.id = :employeeId AND t.startTime AND t.status NOT IN ('CANCELADO', 'AUSENTE', 'ATENDIDO')")
	List<Turn> findConflictingTurnForEmployee(int employeeId, LocalDateTime starTime, LocalDateTime endTime);
	
	//PARA CU011: Buscar turnos proximos para recordatorios
	//Filtra por estado PENDIENTE, EN_ATENCION, para recordar ambos
	List<Turn> findByStartTimeBetweenAndStatusIn(LocalDateTime start, LocalDateTime end, List<TurnStatus> statuses);
	
	//PARA CU010 y otras gestiones
	List<Turn> findByEmployeeIdOrderByStartTimeAsc(int employeeId);
	List<Turn> findByClientIdOrderByStartTimeAsc(int clientId);
	List<Turn> findByStatus(TurnStatus status);
	
}
