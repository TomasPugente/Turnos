package com.grupo12.models;
import java.time.LocalDate;
import java.util.Set;

import com.grupo12.entities.JobFunction;
import com.grupo12.entities.Turn;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO extends PersonDTO {

	private LocalDate entryDate;

	private String cuit;

	private Set<JobFunction> functions;

	private Set<Turn> appointments;
	
	public EmployeeDTO(Integer idPerson, String name, String dni, LocalDate dateOfBirth,
			ContactDTO contact, LocalDate entryDate, String cuit) {
		super(idPerson, name, dni, dateOfBirth, contact);
		this.entryDate = entryDate;
		this.cuit = cuit;
	}

	public EmployeeDTO(Integer idPerson, String name, String surname, String dni, LocalDate dateOfBirth, UserDTO user, ContactDTO contact,
            LocalDate entryDate, String cuit) {
		super(idPerson, name, surname, dni, dateOfBirth, contact, user);
		this.entryDate = entryDate;
		this.cuit = cuit;
   }


}