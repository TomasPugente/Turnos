package com.grupo12.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
	private int idTurn;

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

	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@ManyToOne
	@JoinColumn(name = "date_id")
	private Date date;

	public Turn(int idTurn, TurnStatus state, boolean active, String observation, Client client, Employee employee,
				Date date) {
		super();
		this.idTurn = idTurn;
		this.status = state;
		this.active = active;
		this.observation = observation;
		this.client = client;
		this.employee = employee;
		this.date = date;
	}

}
