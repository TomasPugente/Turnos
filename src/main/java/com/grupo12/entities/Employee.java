package com.grupo12.entities;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="employee")
@DiscriminatorValue("EMPLOYEE")
@PrimaryKeyJoinColumn(name="idEmployee", referencedColumnName="idPerson")
@Getter
@Setter
@NoArgsConstructor
public class Employee extends Person {

    @Column(name="entryDate")
    private LocalDate entryDate;

    @Column(name="cuit")
    private String cuit;

    @ManyToMany
    @JoinTable(name = "employee_function",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "function_id"))
    private Set<JobFunction> functions;

    @OneToMany(mappedBy = "employee")
    private Set<Turn> appointments;

	public Employee(Integer idPerson, String name, String dni, LocalDate dateOfBirth, String password, Contact contact,
			     LocalDate entryDate, String cuit) {
		super(idPerson, name, dni, dateOfBirth, password, contact);
		this.entryDate = entryDate;
		this.cuit = cuit;
	}
    
    
}
