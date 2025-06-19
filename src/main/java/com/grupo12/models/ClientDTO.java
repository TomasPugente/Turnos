package com.grupo12.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ClientDTO extends PersonDTO {

	private String code;
	private Set<TurnDTO> appointmentHistory = new HashSet<>();
	
	@Valid
	UserDTOForm user;	
  
	public ClientDTO(Integer idPerson, String name, String dni, LocalDate dateOfBirth,
			ContactDTO contact, UserDTOForm user, String code) {
		super(idPerson, name, dni, dateOfBirth, contact);
		this.user = user;
		this.code = code;
	}		


	
    public String getUsername() {
        return user != null ? user.getUsername() : null;
    }

}

