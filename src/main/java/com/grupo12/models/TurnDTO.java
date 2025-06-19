package com.grupo12.models;


import com.grupo12.entities.Date;
import com.grupo12.entities.TurnStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TurnDTO {

	private Integer idTurn;

	private TurnStatus status;

	private boolean active;

	private String observation;

	private ClientDTO client;

	private EmployeeDTO employee;

	private Date date;

	public TurnDTO(int idTurn, TurnStatus status, boolean active, String observation, ClientDTO client, EmployeeDTO employee,
			Date date) {
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
