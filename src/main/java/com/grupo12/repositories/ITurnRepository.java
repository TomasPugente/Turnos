package com.grupo12.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.grupo12.entities.Date;
import com.grupo12.entities.Turn;
import org.springframework.data.jpa.repository.Query;

import com.grupo12.entities.Client;
import com.grupo12.entities.Turn;
import com.grupo12.entities.TurnStatus;

public interface ITurnRepository extends JpaRepository<Turn, Serializable> {

	List<Turn> findByStatus(TurnStatus pendiente);
	
    @Query("SELECT t FROM Turn t WHERE t.client IS NULL AND t.status = 'PENDIENTE' AND t.active = true")
    List<Turn> findAvailableTurns();
	
	/*@Query("SELECT t FROM Turn t WHERE t.client IS NULL AND t.status = com.grupo12.entities.TurnStatus.PENDIENTE AND t.active = true")
	List<Turn> findAvailableTurns();*/
    
    List<Turn> findByClient(Client client);
    List<Turn> findByClientIsNullAndStatus(TurnStatus status);
}


