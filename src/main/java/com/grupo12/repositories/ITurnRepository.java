package com.grupo12.repositories;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.grupo12.entities.Date;
import com.grupo12.entities.Turn;
import org.springframework.data.jpa.repository.Query;

import com.grupo12.entities.Client;
import com.grupo12.entities.Turn;
import com.grupo12.entities.TurnStatus;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import com.grupo12.entities.Turn;
import com.grupo12.entities.TurnStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ITurnRepository extends JpaRepository<Turn, Integer> {
	@Query("SELECT t FROM Turn t WHERE t.status = 'PENDIENTE'")
	List<Turn> findAvailableTurns(); // ✅ Funciona
	

    
    List<Turn> findByClient(Client client);
    List<Turn> findByClientIsNullAndStatus(TurnStatus status);
	
	
	//PARA CU008: Buscar turnos pendientes por empleado, ordenados por hora de inicio
	List<Turn> findByEmployee_IdPersonAndStatusOrderByStartTimeAsc(int employeeId, TurnStatus status);
	
	//PARA CU008: Buscar el proximo turno pendiente para un empleado
	/*@Query("SELECT t FROM Turn t WHERE t.employee.idPerson = :employeeId AND t.status = 'PENDIENTE' ORDER BY t.startTime ASC") 
	List<Turn> findAvailableTurns();*/
	
	/*@Query("SELECT t FROM Turn t WHERE t.client IS NULL AND t.status = com.grupo12.entities.TurnStatus.PENDIENTE AND t.active = true")
	List<Turn> findAvailableTurns();*/
	
	//Optional<Turn> findNextPendingTurnByEmployeedId(int employeeId);
	@Query("SELECT t FROM Turn t WHERE t.employee.idPerson = :employeeId AND t.status = 'PENDIENTE'")
	List<Turn>findNextPendingTurnByEmployeeId(int employeeId,Pageable pageable);
	
	//PARA CU009 (validacion de disponibilidad): Comprobar solapamientos 
	@Query("SELECT t FROM Turn t WHERE t.employee.idPerson = :employeeId AND ((t.startTime < :endTime AND t.endTime > :startTime) OR (t.startTime =:startTime AND t.endTime =:endTime)) AND t.status NOT IN ('CANCELADO', 'AUSENTE', 'ATENDIDO')")
	List<Turn> findConflictingTurnForEmployee(@Param("employeeId")int employeeId, @Param("startTime")LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);
	
	//PARA CU011: Buscar turnos proximos para recordatorios
	//Filtra por estado PENDIENTE, EN_ATENCION, para recordar ambos
	List<Turn> findByStartTimeBetweenAndStatusIn(LocalDateTime start, LocalDateTime end, List<TurnStatus> statuses);
	
	//PARA CU010 y otras gestiones
	List<Turn> findByEmployee_IdPersonOrderByStartTimeAsc(int employeeId);
	List<Turn> findByClientIdPersonOrderByStartTimeAsc(int clientId);
	List<Turn> findByStatus(TurnStatus status);




	Collection<Turn> findByEmployeeIdPersonOrderByStartTimeAsc(int employeeId);



	Optional<Turn> findByIdTurn(Integer id);



	void deleteByEmployeeIdPersonAndClientIsNull(Integer id);
	
}

