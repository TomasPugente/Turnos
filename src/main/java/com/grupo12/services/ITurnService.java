package com.grupo12.services;

import java.util.List;

import com.grupo12.entities.Turn;

public interface ITurnService {

	//public Turn requestAnAppointment(Long idClient, Long idService, LocalDate date);
	
	public List<Turn> GetAvailableAppointments(); 
	
	public void reserveTurn(Long idTurno, String username);
}
