package com.grupo12.converters;


import com.grupo12.models.ContactDTO;
import com.grupo12.models.EmployeeDTO;
import com.grupo12.models.LocalityDTO;


import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;


import com.grupo12.entities.*;
import com.grupo12.models.ContactDTO;
import com.grupo12.models.LocalityDTO;

import com.grupo12.models.UserDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component("employeeConverter")
public class EmployeeConverter {

    public EmployeeDTO entityToDTO(Employee employee) {
    	return new EmployeeDTO(
    		    employee.getIdPerson(),
    		    employee.getName(),
    		    employee.getSurname(),
    		    employee.getDni(),
    		    employee.getDateOfBirth(),
    		    employee.getUser() != null ? userToDTO(employee.getUser()) : null,// primero UserDTO
    		    contactToDTO(employee.getContact()),// después ContactDTO
    		    employee.getEntryDate(),
    		    employee.getCuit()
    		);

    }

    public Employee DTOToEntity(EmployeeDTO employeeDTO) {

        return new Employee(
                employeeDTO.getIdPerson(),
                employeeDTO.getName(),
                employeeDTO.getSurname(),
                employeeDTO.getDni(),
                employeeDTO.getDateOfBirth(),
                contactToEntity(employeeDTO.getContact()),
                employeeDTO.getEntryDate(),
                employeeDTO.getCuit(),
                null,
                employeeDTO.getFunctions()
        );
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

