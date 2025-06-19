package com.grupo12.entities;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id")
    private Contact contact;
    

    
    

}
