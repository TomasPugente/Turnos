package com.grupo12.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="date")
@Getter
@Setter
@NoArgsConstructor
public class Date {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDate;

    @Column(name="date")
    private LocalDate date;
    
    @Column(name="hour")
    private LocalTime hour;

    @OneToMany(mappedBy = "date")
    private Set<Turn> appointments;

    @ManyToMany
    @JoinTable(name = "date_service",
        joinColumns = @JoinColumn(name = "date_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<Service> services;

	public Date(int idDate, LocalDate date, LocalTime hour) {
		super();
		this.idDate = idDate;
		this.date = date;
		this.hour = hour;
	}
    
    
}

