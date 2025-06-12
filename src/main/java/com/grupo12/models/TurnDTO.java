package com.grupo12.models;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TurnDTO {

	private int idTurn;

	private String state;

	private boolean active;

	private String observation;

	private ClientDTO client;

	private EmployeeDTO employee;

	private Date date;

	public TurnDTO(int idTurn, String state, boolean active, String observation, ClientDTO client, EmployeeDTO employee,
			Date date) {
		super();
		this.idTurn = idTurn;
		this.state = state;
		this.active = active;
		this.observation = observation;
		this.client = client;
		this.employee = employee;
		this.date = date;
	}

}
