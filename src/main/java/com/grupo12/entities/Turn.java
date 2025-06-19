package com.grupo12.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "turn")
@Getter
@Setter
@NoArgsConstructor
public class Turn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTurn;

    @Enumerated(EnumType.STRING)
    @Column (name="status",nullable=false)
    private TurnStatus status; //PENDIENTE, EN_ATENCION, ATENDIDO, AUSENTE, CANCELADO

	@Column(name = "createdAt")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "updateDate")
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@Column(name = "active")
	private boolean active;

	@Column(name = "observation")
	private String observation;
    
    @PrePersist
    protected void onCreate() {
    	LocalDateTime createdAt = LocalDateTime.now();
    	if(status==null) {
    		status=TurnStatus.PENDIENTE;//Estado predeterminado para los turnos recién creados
    	}
    }

	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@ManyToOne
	@JoinColumn(name = "date_id")
	private Date date;
	
	@ManyToOne
	@JoinColumn(name = "service_id")
	private Service service;

	public Turn(int idTurn, TurnStatus status, boolean active, String observation, Client client, Employee employee,
			Date date, Service service) {
		super();
		this.idTurn = idTurn;
		this.status = status;
		this.active = active;
		this.observation = observation;
		this.client = client;
		this.employee = employee;
		this.date = date;
	}



}
