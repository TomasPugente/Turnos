package com.grupo12.SistemaTurnos.entities;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="person")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="idPerson_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPerson;

    @Column(name="name")
    private String name;
    
    @Column(name="dni")
    private int dni;

    @Column(name="dateOfBirth")
    private LocalDate dateOfBirth;
    
    @Column(name="password")
    private String password;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id")
    private Contact contact;

	public Person(int idPerson, String name, int dni, LocalDate dateOfBirth, String password, Contact contact) {
		super();
		this.idPerson = idPerson;
		this.name = name;
		this.dni = dni;
		this.dateOfBirth = dateOfBirth;
		this.password = password;
		this.contact = contact;
	}
    
    

}
