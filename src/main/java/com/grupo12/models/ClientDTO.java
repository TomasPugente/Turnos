package com.grupo12.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ClientDTO extends PersonDTO {

	private String code;
	
	private Set<TurnDTO> appointmentHistory = new HashSet<>();	
	
  
	public ClientDTO(Integer idPerson, String name, String surname, String dni, LocalDate dateOfBirth,
			ContactDTO contact, UserDTO user, String code) {
		super(idPerson, name, surname, dni, dateOfBirth, contact, user);
		this.code = code;
	}
 
	
    /*public String getUsername() {
        return user != null ? user.getUsername() : null;
    }*/

}

