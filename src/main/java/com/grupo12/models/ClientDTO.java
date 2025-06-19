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

	public ClientDTO(Integer idPerson, String name, String surname, String dni, LocalDate dateOfBirth,
			ContactDTO contact, UserDTO user, String code) {
		super(idPerson, name, surname, dni, dateOfBirth, user, contact);
		this.code = code;
	}

}