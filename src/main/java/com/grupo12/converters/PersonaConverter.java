package com.grupo12.converters;

import com.grupo12.entities.*;
import com.grupo12.models.ClientDTO;
import com.grupo12.models.EmployeeDTO;
import com.grupo12.models.PersonDTO;
import com.grupo12.services.ILocalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.grupo12.utils.Util.contactToDTO;
import static com.grupo12.utils.Util.contactToEntity;

@Component("personConverter")
public class PersonaConverter {
    @Autowired
    @Qualifier("localityService")
    private ILocalityService localityService;

    public PersonDTO entityToDTO(Person person) {
        return new PersonDTO(
                person.getIdPerson(),
                person.getName(),
                person.getSurname(),
                person.getDni(),
                person.getDateOfBirth(),
                null,
                contactToDTO(person.getContact())
        );
    }

    public Person DTOToEntity(PersonDTO personDTO) {
        Person person = new Person();
        if (personDTO.getIdPerson() != null) {
            person.setIdPerson(personDTO.getIdPerson());
        }

        person.setName(personDTO.getName());
        person.setSurname(personDTO.getSurname());
        person.setDni(personDTO.getDni());
        person.setDateOfBirth(personDTO.getDateOfBirth());

        if(personDTO.getContact() != null) {
            person.setContact(contactToEntity(personDTO.getContact()));
        }

        return person;
    }

    public void updateEntityFromDTO(Person existing, PersonDTO dto) {
        if (existing.getContact() == null) {
            existing.setContact(new Contact());
        }

        existing.setIdPerson(dto.getIdPerson());
        existing.setDni(dto.getDni());
        existing.setName(dto.getName());
        existing.setSurname(dto.getSurname());
        existing.setDateOfBirth(dto.getDateOfBirth());
        existing.getContact().setStreet(dto.getContact().getStreet());
        existing.getContact().setNumber(dto.getContact().getNumber());
        existing.getContact().setEmail(dto.getContact().getEmail());
        existing.getContact().setPhone(dto.getContact().getPhone());

        if (dto.getContact().getLocality() != null) {
            Locality locality = localityService.getById(dto.getContact().getLocality().getIdLocality()).orElse(null);
            existing.getContact().setLocality(locality);
        } else {
            existing.getContact().setLocality(null); // o dejarla como está
        }
    }

}
