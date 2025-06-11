package com.grupo12.models;

import java.util.Date;

//import com.grupo12.entities.Contact;

import java.time.LocalDateTime;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Data;


@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor //Ya genera el constructor de todos los argumentos
public class TurnDTO {

    private int idTurn;
 
    private boolean active;
    private String observation;	
    private ClientDTO client;
    private EmployeeDTO employee;
    private Date date;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; //Representa el enum como String
    private LocalDateTime creationTime;
	
    
    //Campos para el cliente
    private Integer clientIdPerson;
    private String clientName;
    private String clientDni;
    private LocalDate clientDateOfBirth;
    private String clientPassword;
    private String clientEmail;
    
    //Campos para el empleado
    private Integer employeeIdPerson;
    private String employeeName;
    private String employeeDni;
    private LocalDate employeeDateOfBirth;
    private String employeePassword;
    private String employeeEmail;
    private String employeeCuit;
    private LocalDate employeeEntryDate;
    
    //Campos para el servicio
    private String serviceName;
    private Integer idServicio;
    
    //Campo para los recordatorios
    private Boolean reminderSent;
    
    public int getEmployeeId() {
        return employeeIdPerson;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeIdPerson = employeeId;
    }

    public int getServiceId() {
        return idServicio;
    }

    public void setServiceId(int serviceId) {
        this.idServicio = serviceId;
    }

	public void setReminderSent(Boolean reminderSent2) {
		// TODO Auto-generated method stub
		this.reminderSent=reminderSent2;
	}
	
	public TurnDTO(int idTurn, String status, boolean active, String observation, ClientDTO client, EmployeeDTO employee,
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

	public Boolean getReminderSent() {
		// TODO Auto-generated method stub
		return reminderSent;
	}
	
}
