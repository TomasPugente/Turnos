package com.grupo12.services.implementation;

<<<<<<< Updated upstream
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.grupo12.entities.Client;
import com.grupo12.entities.Service;
import com.grupo12.entities.Turn;
=======
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo12.entities.Client;
import com.grupo12.entities.Turn;
import com.grupo12.entities.TurnStatus;
>>>>>>> Stashed changes
import com.grupo12.repositories.IClientRepository;
import com.grupo12.repositories.ITurnRepository;
import com.grupo12.services.ITurnService;

<<<<<<< Updated upstream
public class TurnService implements ITurnService {


=======
@Service("turnService")
public class TurnService implements ITurnService {

>>>>>>> Stashed changes
    @Autowired
    private ITurnRepository turnRepository;

    @Autowired
    private IClientRepository clientRepository;
<<<<<<< Updated upstream

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
=======
	
	@Override
	public List<Turn> getAvailableTurns() {
		return turnRepository.findAvailableTurns();
	}

	//Sirve para un cliente logueado
	/*@Override
	public void reserveAppointment(Long idTurno, String username) {
        Turn turn = turnRepository.findById(idTurno).orElseThrow();
        Client client = clientRepository.findByUsername(username).orElseThrow(); // según cómo manejes login

        turn.setClient(client);
        turn.setStatus(TurnStatus.PENDIENTE);
        turnRepository.save(turn);
		
	}*/
	
    public Optional<Turn> getTurnById(int id) {
        return turnRepository.findById(id);
    }

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

	@Override
	public void save(Turn turno) {
		turnRepository.save(turno);
	}
>>>>>>> Stashed changes

}
