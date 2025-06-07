package com.grupo12.models;

import java.time.LocalDate;
import java.util.Set;

import com.grupo12.entities.JobFunction;
import com.grupo12.entities.Turn;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO extends PersonDTO {

	private String name;
	private LocalDate dateOfBirth;
	private String password;
	private ContactDTO contact;
    private LocalDate entryDate;
    private String cuit;

    private Set<JobFunction> functions;

    private Set<Turn> appointments;

	public EmployeeDTO(Integer idPerson, String name, String dni, LocalDate dateOfBirth, String password,
			     ContactDTO contact, LocalDate entryDate, String cuit) {
		super(idPerson, name, dni, dateOfBirth, password, contact);
		this.entryDate = entryDate;
		this.cuit = cuit;
	}

}