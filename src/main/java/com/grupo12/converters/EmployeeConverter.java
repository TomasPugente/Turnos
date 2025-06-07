package com.grupo12.converters;

import com.grupo12.entities.Contact;
import com.grupo12.entities.Employee;
import com.grupo12.entities.Locality;
import com.grupo12.models.ContactDTO;
import com.grupo12.models.EmployeeDTO;
import com.grupo12.models.LocalityDTO;

public class EmployeeConverter {

    public EmployeeDTO entityToDTO(Employee employee) {
        return new EmployeeDTO(
        	employee.getIdPerson(),
        	employee.getName(),
        	employee.getDni(),
        	employee.getDateOfBirth(),
        	employee.getPassword(),
            contactToDTO(employee.getContact()),
            employee.getEntryDate(),
            employee.getCuit()
        );
    }

    public Employee DTOToEntity(EmployeeDTO employeeDTO) {
        return new Employee(
        	employeeDTO.getIdPerson(),
        	employeeDTO.getName(),
        	employeeDTO.getDni(),
        	employeeDTO.getDateOfBirth(),
        	employeeDTO.getPassword(),
            contactToEntity(employeeDTO.getContact()),
            employeeDTO.getEntryDate(),
            employeeDTO.getCuit()
        );
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
}
