package com.grupo12.converters;

import com.grupo12.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.grupo12.entities.Client;
import com.grupo12.entities.Contact;
import com.grupo12.entities.Locality;
import com.grupo12.models.ClientDTO;
import com.grupo12.models.ContactDTO;
import com.grupo12.models.LocalityDTO;
import com.grupo12.services.ILocalityService;

import static com.grupo12.utils.Util.contactToDTO;
import static com.grupo12.utils.Util.contactToEntity;

@Component("clientConverter")
public class ClientConverter {

    @Autowired
    @Qualifier("localityService")
    private ILocalityService localityService;

    public ClientDTO entityToDTO(Client client) {
        return new ClientDTO(
                client.getIdPerson(),
                client.getName(),
                client.getSurname(),
                client.getDni(),
                client.getDateOfBirth(),
                contactToDTO(client.getContact()), null,
                client.getCode());
    }

    public Client DTOToEntity(ClientDTO clientDTO) {
        Client client = new Client(
                clientDTO.getIdPerson(),
                clientDTO.getName(),
                clientDTO.getSurname(),
                clientDTO.getDni(),
                clientDTO.getDateOfBirth(),
                contactToEntity(clientDTO.getContact()),
                clientDTO.getCode(), null);

        if (clientDTO.getIdPerson() != null) {
            client.setIdPerson(clientDTO.getIdPerson());
        }

        return client;
    }

    public void updateEntityFromDTO(Client existing, ClientDTO dto) {
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
        existing.setCode(dto.getCode());

        if (dto.getContact().getLocality().getIdLocality() != null) {
            Locality locality = localityService.getById(dto.getContact().getLocality().getIdLocality()).orElse(null);
            existing.getContact().setLocality(locality);
        } else {
            existing.getContact().setLocality(null); // o dejarla como está
        }
    }

}
