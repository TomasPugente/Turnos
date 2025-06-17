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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "client")
@DiscriminatorValue("CLIENT")
@PrimaryKeyJoinColumn(name = "idClient", referencedColumnName = "idPerson")
@Getter
@Setter
@NoArgsConstructor
public class Client extends Person {

	@Column(name = "code")
	private String code;

	@OneToMany(mappedBy = "client")
	private Set<Turn> appointmentHistory;

	public Client(Integer idPerson, String name, String surname, String dni, LocalDate dateOfBirth,
			Contact contact,
			String code, User user) {
		super(idPerson, name, dni, dateOfBirth, contact, user);
		this.code = code;
	}

}
