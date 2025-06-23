package com.grupo12.entities;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "client")
@DiscriminatorValue("CLIENT")
@PrimaryKeyJoinColumn(name = "idClient", referencedColumnName = "idPerson")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client extends Person {

	@Column(name = "code")
	private String code;

	
	@OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Turn> appointmentHistory = new HashSet<>();


	public Client(Integer idPerson, String name, String surname, String dni, LocalDate dateOfBirth,
            Contact contact, User user, String code) {
			  super(idPerson, name, surname, dni, dateOfBirth, contact, user);
			  this.code = code;
    }
	
    
    

}
