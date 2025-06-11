package com.grupo12.services;

import java.util.List;
<<<<<<< Updated upstream

=======
import java.util.Map;
import java.util.Optional;

import com.grupo12.entities.Client;
>>>>>>> Stashed changes
import com.grupo12.entities.Turn;

public interface ITurnService {

<<<<<<< Updated upstream
	//public Turn requestAnAppointment(Long idClient, Long idService, LocalDate date);
	
	public List<Turn> GetAvailableAppointments(); 
	
	public void reserveTurn(Long idTurno, String username);
=======
	public List<Turn> getAvailableTurns();
	
	//public void reserveAppointment(Long idTurno, String username);
	
	public Optional<Turn> getTurnById(int id);
	
	public void assignTurnToClient(Turn turn, Client client);
	
    List<Turn> getTurnsByClient(Client client);
    
    Map<String, List<Turn>> getTurnsGroupedByStatus(Client client);
    
    void save(Turn turno);

	
>>>>>>> Stashed changes
}
