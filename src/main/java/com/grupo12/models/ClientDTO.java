package com.grupo12.models;

import java.time.LocalDate;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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