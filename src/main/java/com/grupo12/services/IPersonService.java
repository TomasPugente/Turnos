package com.grupo12.services;

import com.grupo12.entities.Person;
import com.grupo12.entities.Turn;
import com.grupo12.models.PersonDTO;
import com.grupo12.models.TurnDTO;

import java.util.List;
import java.util.Optional;

public interface IPersonService {
    List<Person> getAll();

    Optional<Person> findById(Integer id);
    void insertOrUpdate(PersonDTO personDTO);
    public boolean remove(int idPerson);

}
