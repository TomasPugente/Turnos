package com.grupo12.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="service")
@Getter
@Setter
@NoArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idService;

    @Column(name="name")
    private String name;
    
    @Column(name="description")
    private String description;

    @ManyToMany(mappedBy = "services")
    private Set<Date> dates;
    
    @Column(name="duration_minutes", nullable=false)
    private int durationMinutes;
    
    public Service(int idService,String name,String description, int durationMinutes) {
    	this.idService=idService;
    	this.name=name;
    	this.description=description;
    	this.durationMinutes=durationMinutes;
    }
}
