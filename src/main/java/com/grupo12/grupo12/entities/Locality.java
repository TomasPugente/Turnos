package com.grupo12.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="locality")
@Getter
@Setter
@NoArgsConstructor
public class Locality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLocality;

    @Column(name="name")
    private String name;

	public Locality(int idLocality, String name) {
		super();
		this.idLocality = idLocality;
		this.name = name;
	}

    
}