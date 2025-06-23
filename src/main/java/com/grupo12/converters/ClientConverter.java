package com.grupo12.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.grupo12.entities.Client;
import com.grupo12.entities.Contact;
import com.grupo12.entities.Locality;
import com.grupo12.entities.User;
import com.grupo12.models.ClientDTO;
import com.grupo12.models.ContactDTO;
import com.grupo12.models.LocalityDTO;
import com.grupo12.models.UserDTO;
import com.grupo12.models.UserDTOForm;
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
                client.getSurname(),
                client.getDni(),
                client.getDateOfBirth(),
                contactToDTO(client.getContact()),
                userToDTO(client.getUser()),
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
                userToEntity(clientDTO.getUser()),
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
        existing.setSurname(dto.getSurname());
        existing.setDateOfBirth(dto.getDateOfBirth());
        
        if (existing.getContact() == null) {
            existing.setContact(new Contact());
        }

        
        existing.getContact().setStreet(dto.getContact().getStreet());
        existing.getContact().setNumber(dto.getContact().getNumber());
        existing.getContact().setPhone(dto.getContact().getPhone());
        existing.setCode(dto.getCode());
        
        System.out.println("Street DTO: " + dto.getContact().getStreet());
        System.out.println("Number DTO: " + dto.getContact().getNumber());
        System.out.println("Existing Contact ID: " + (existing.getContact() != null ? existing.getContact().getIdContact() : "null"));
       // existing.setCode(dto.getCode());

        
        // ✅ Control de null para evitar NPE
        if (existing.getUser() != null && dto.getUser() != null) {
        	// Actualiza datos del User
            existing.getUser().setUsername(dto.getUser().getUsername());
            existing.getUser().setPassword(dto.getUser().getPassword());
            existing.getUser().setEmail(dto.getUser().getEmail());
        }

        if (dto.getContact().getLocality().getIdLocality() != null) {
            Locality locality = localityService.findById(dto.getContact().getLocality().getIdLocality()).orElse(null);
            existing.getContact().setLocality(locality);
        } else {
            existing.getContact().setLocality(null);
        }
    }

    public ContactDTO contactToDTO(Contact contact) {
        if (contact == null)
            return null;
        return new ContactDTO(
                contact.getIdContact(),
                contact.getStreet(),
                contact.getNumber(),
                contact.getPhone(),
                localityToDTO(contact.getLocality()));
    }

    public Contact contactToEntity(ContactDTO contactDTO) {
        if (contactDTO == null)
            return null;
        return new Contact(
                contactDTO.getIdContact(),
                contactDTO.getStreet(),
                contactDTO.getNumber(),
                contactDTO.getPhone(),
                localityToEntity(contactDTO.getLocality()));
    }
    
    public UserDTO userToDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        return dto;
    }
    
    public User userToEntity(UserDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setEnabled(true); // Por defecto en true, ajustalo si tenés lógica de activación
        return user;
    }

    private LocalityDTO localityToDTO(Locality locality) {
        if (locality == null)
            return null;
        return new LocalityDTO(locality.getIdLocality(), locality.getName());
    }

    private Locality localityToEntity(LocalityDTO localityDTO) {
        if (localityDTO == null)
            return null;
        return new Locality(localityDTO.getIdLocality(), localityDTO.getName());
    }


}

