package com.grupo12.services.implementation;

import com.grupo12.converters.ClientConverter;
import com.grupo12.converters.PersonaConverter;
import com.grupo12.entities.Person;
import com.grupo12.entities.Turn;
import com.grupo12.exceptions.TurnNotFoundException;
import com.grupo12.models.PersonDTO;
import com.grupo12.models.TurnDTO;
import com.grupo12.models.TurnUpdateDTO;
import com.grupo12.repositories.IPersonRepository;
import com.grupo12.repositories.ITurnRepository;
import com.grupo12.services.IJobFunctionService;
import com.grupo12.services.IPersonService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("personService")
public class PersonService implements IPersonService {
    @Autowired
    @Qualifier("personRepository")
    private IPersonRepository personRepository;

    @Autowired
    @Qualifier("personConverter")
    private PersonaConverter personConverter;

    public PersonService(IPersonRepository personRepository) {
        this.personRepository = personRepository;
        this.personConverter = new PersonaConverter();
    }

//    @Override
//    public void updateTurn(Long id, TurnUpdateDTO turnDTO) {
//        Turn turn = turnRepository.findById(id)
//                .orElseThrow(() -> new TurnNotFoundException(id));
//
//        turn.setDate(turnDTO.getDate());
//        turn.setStatus(turnDTO.getStatus());
//        turnRepository.save(turn);
//    }
//
    @Override
    public Optional<Person> findById(Integer id){
        return personRepository.findById(id);
    }
//
    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @Override
	@Transactional
	public void insertOrUpdate(PersonDTO personDTO) {
		Person person;

		if (personDTO.getIdPerson() != null) {
			// Buscar y actualizar el cliente existente
            person = personRepository.findById(personDTO.getIdPerson()).orElseThrow();
			personConverter.updateEntityFromDTO(person, personDTO);
		} else {
			// Crear un nuevo cliente desde el DTO
			person = personConverter.DTOToEntity(personDTO);
		}
        personRepository.save(person);
    }

    @Override
    public boolean remove(int idPerson) {
        try {
            personRepository.deleteById(idPerson);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
