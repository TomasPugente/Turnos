package com.grupo12.entities;


import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="client")
@DiscriminatorValue("CLIENT")
@PrimaryKeyJoinColumn(name="idClient", referencedColumnName="idPerson")
@Getter
@Setter
public class Client extends Person {
	
	@Column(name="name")
	private String name;
	
    @OneToMany(mappedBy = "client")
    private Set<Turn> appointmentHistory;

	public Client(int idPerson, String name, int dni, LocalDate dateOfBirth, String password, Contact contact,
			int idClient, String name2) {
		super(idPerson, name, dni, dateOfBirth, password, contact);
		name = name2;
	}
    
    
	

}
