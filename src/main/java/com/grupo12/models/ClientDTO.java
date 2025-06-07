package com.grupo12.models;

import java.time.LocalDate;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO extends PersonDTO {
	//private int idPerson;
	private String name;
	private String dni;
	private LocalDate dateOfBirth;
	private String password;
	private ContactDTO contact;
	private String code;
	
    private Set<TurnDTO> appointmentHistory;

	public ClientDTO(Integer idPerson, String name, String dni, LocalDate dateOfBirth, String password,
			ContactDTO contact, String code) {
		super(idPerson, name, dni, dateOfBirth, password, contact);
		this.code = code;
	}
    
    
	

}