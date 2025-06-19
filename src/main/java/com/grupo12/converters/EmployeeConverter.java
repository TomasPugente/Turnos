package com.grupo12.converters;

import com.grupo12.entities.*;
import com.grupo12.models.ContactDTO;
import com.grupo12.models.EmployeeDTO;
import com.grupo12.models.LocalityDTO;

import com.grupo12.models.UserDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.grupo12.utils.Util.contactToDTO;
import static com.grupo12.utils.Util.contactToEntity;

@Component("employeeConverter")
public class EmployeeConverter {

    public EmployeeDTO entityToDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getIdPerson(),
                employee.getName(),
                employee.getSurname(),
                employee.getDni(),
                employee.getDateOfBirth(),
                contactToDTO(employee.getContact()),
                null,
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

}
