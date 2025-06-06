package com.grupo12.entities;

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
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPerson;

    @Column(name="name")
    private String name;
    
    @Column(name="dni")
    private String dni;

    @Column(name="dateOfBirth")
    private LocalDate dateOfBirth;
    
    @Column(name="password")
    private String password;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id")
    private Contact contact;

	public Person(Integer idPerson, String name, String dni, LocalDate dateOfBirth, String password, Contact contact) {
		super();
		this.idPerson = idPerson;
		this.name = name;
		this.dni = dni;
		this.dateOfBirth = dateOfBirth;
		this.password = password;
		this.contact = contact;
	}
    
    

}
