package com.grupo12.services.implementation;

import java.util.List;
import java.util.Optional;

import com.grupo12.converters.TurnConverter;
import com.grupo12.entities.Client;
import com.grupo12.exceptions.TurnNotFoundException;
import com.grupo12.models.ClientDTO;
import com.grupo12.models.TurnDTO;
import com.grupo12.models.TurnUpdateDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.grupo12.entities.Turn;
import com.grupo12.repositories.ITurnRepository;
import com.grupo12.services.ITurnService;

@Service("turnService")
public class TurnService implements ITurnService {

    private final ITurnRepository turnRepository;

    @Autowired
    @Qualifier("turnConverter")
    private TurnConverter turnConverter;

    public TurnService(ITurnRepository turnRepository) {
        this.turnRepository = turnRepository;
    }

    @Override
    public void updateTurn(Long id, TurnUpdateDTO turnDTO) {
        Turn turn = turnRepository.findById(id)
                .orElseThrow(() -> new TurnNotFoundException(id));

        turn.setDate(turnDTO.getDate());
        turn.setStatus(turnDTO.getStatus());
        turnRepository.save(turn);
    }

    @Override
    public Turn findById(Long id){
        return turnRepository.findById(id)
                .orElseThrow(() -> new TurnNotFoundException(id));
    }

    @Override
    public List<Turn> getAll() {
        return turnRepository.findAll();
    }

    public Optional<Turn> getById(int idTurn) {
        return turnRepository.findById(idTurn);
    }

    @Override
    @Transactional
    public void insertOrUpdate(TurnDTO turnDTO) {
        Turn turn;

        if (turnDTO.getIdTurn() != null) {
            turn = turnRepository.findById(turnDTO.getIdTurn()).orElseThrow();
            turnConverter.updateEntityFromDTO(turn, turnDTO);
        } else {
            // Crear un nuevo turne desde el DTO
            turn = turnConverter.DTOToEntity(turnDTO);
        }

        turnRepository.save(turn);

    }

}
