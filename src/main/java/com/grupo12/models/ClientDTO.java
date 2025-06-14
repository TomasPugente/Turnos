package com.grupo12.models;

import java.time.LocalDate;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ClientDTO extends PersonDTO {

	private String code;

	private Set<TurnDTO> appointmentHistory;

	public ClientDTO(Integer idPerson, String name, String dni, LocalDate dateOfBirth, String password,
			ContactDTO contact, String code) {
		super(idPerson, name, dni, dateOfBirth, password, contact);
		this.code = code;
	}

}