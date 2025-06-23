package com.grupo12.entities;

import java.time.LocalDateTime;
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
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idService;

    @Column(name="name")
    private String name;
    
    @Column(name="detail")
    private String detail;
    
    @Column(name="description")
    private String description;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;
    
    

    @ManyToMany(mappedBy = "services")
    private Set<Date> dates;

	public ServiceEntity(Integer idService, String name, String detail, Integer durationMinutes) {
		super();
		this.idService = idService;
		this.name = name;
		this.detail = detail;
		this.durationMinutes = durationMinutes;
	}


    
    
}
