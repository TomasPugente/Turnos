package com.grupo12.services.implementation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.grupo12.entities.Client;
import com.grupo12.entities.Service;
import com.grupo12.entities.Turn;
import com.grupo12.repositories.IClientRepository;
import com.grupo12.repositories.ITurnRepository;
import com.grupo12.services.ITurnService;

public class TurnService implements ITurnService {


    @Autowired
    private ITurnRepository turnRepository;

    @Autowired
    private IClientRepository clientRepository;

	@Override
	public List<Turn> GetAvailableAppointments() {
		 return turnRepository.findByState("disponible");
	}

	@Override
	public void reserveTurn(Long idTurno, String username) {
        Turn turn = turnRepository.findById(idTurno).orElseThrow();
        Client client = clientRepository.findById(username).orElseThrow(); // según cómo manejes login

        turn.setClient(client);
        turn.setState("pendiente");
        turnRepository.save(turn);
		
	}
    
	/*@Override
	public Turn requestAnAppointment(Long idClient, Long idService, LocalDate date) {
        Client client = clientRepository.findById(idClient)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Service service = servicioRepository.findById(idService)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        Turn turn = new Turn();
        turn.setClient(client);
        //turn.setService(service);
        turn.setDate(date);
        turn.setState("pendiente");

        return turnRepository.save(turn);
	}*/

}
