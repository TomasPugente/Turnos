package com.grupo12.services;

import java.util.List;
import java.util.Optional;

import com.grupo12.entities.Turn;
import com.grupo12.models.TurnDTO;
import com.grupo12.models.TurnUpdateDTO;
import jakarta.validation.Valid;

public interface ITurnService {

    public void updateTurn(Long id, TurnUpdateDTO localityDTO);


    Turn findById(Long id);

    List<Turn> getAll();

    void insertOrUpdate(TurnDTO turnDTO);
}
