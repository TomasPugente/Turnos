package com.grupo12.converters;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.grupo12.entities.Client;
import com.grupo12.entities.Contact;
import com.grupo12.entities.Locality;
import com.grupo12.models.ClientDTO;
import com.grupo12.models.ContactDTO;
import com.grupo12.models.LocalityDTO;
import com.grupo12.services.IClientService;
import com.grupo12.services.ILocalityService;


@Component("clientConverter")
public class ClientConverter {
	
    @Autowired
    @Qualifier("localityService")
    private ILocalityService localityService;

    public ClientDTO entityToDTO(Client client) {
        return new ClientDTO(
            client.getIdPerson(),
            client.getName(),
            client.getDni(),
            client.getDateOfBirth(),
            client.getPassword(),
            contactToDTO(client.getContact()),
            client.getCode()
        );
    }
    
    public Client DTOToEntity(ClientDTO clientDTO) {
       Client client = new Client(
            clientDTO.getIdPerson(),
            clientDTO.getName(),
            clientDTO.getDni(),
            clientDTO.getDateOfBirth(),
            clientDTO.getPassword(),
            contactToEntity(clientDTO.getContact()),
            clientDTO.getCode()
        );
        
        if (clientDTO.getIdPerson() != null) {
    	    client.setIdPerson(clientDTO.getIdPerson());
    	}
        
        return client;
    }
   
    public void updateEntityFromDTO(Client existing, ClientDTO dto) {
    	existing.setIdPerson(dto.getIdPerson());
        existing.setDni(dto.getDni());
        existing.setName(dto.getName());
        existing.setDateOfBirth(dto.getDateOfBirth());
        existing.setPassword(dto.getPassword());
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


    private ContactDTO contactToDTO(Contact contact) {
        if (contact == null) return null;
        return new ContactDTO(
            contact.getIdContact(),
            contact.getStreet(),
            contact.getNumber(),
            contact.getEmail(),
            contact.getPhone(),
            localityToDTO(contact.getLocality())
        );
    }

    private Contact contactToEntity(ContactDTO contactDTO) {
        if (contactDTO == null) return null;
        return new Contact(
            contactDTO.getIdContact(),
            contactDTO.getStreet(),
            contactDTO.getNumber(),
            contactDTO.getEmail(),
            contactDTO.getPhone(),
            localityToEntity(contactDTO.getLocality())
        );
    }

    private LocalityDTO localityToDTO(Locality locality) {
        if (locality == null) return null;
        return new LocalityDTO(locality.getIdLocality(), locality.getName());
    }

    private Locality localityToEntity(LocalityDTO localityDTO) {
        if (localityDTO == null) return null;
        return new Locality(localityDTO.getIdLocality(), localityDTO.getName());
    }

	public ILocalityService getLocalityService() {
		return localityService;
	}

	public void setLocalityService(ILocalityService localityService) {
		this.localityService = localityService;
	}
    
}

