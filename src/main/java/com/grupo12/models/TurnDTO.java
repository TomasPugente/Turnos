package com.grupo12.models;




import java.util.Date;

import com.grupo12.entities.Contact;

import java.time.LocalDateTime;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor //Ya genera el constructor de todos los argumentos
public class TurnDTO {

    private int idTurn;
    private String state;
    private boolean active;
    private String observation;
    private ClientDTO client;
    private EmployeeDTO employee;
    private Date date;
    private Integer idServicio;
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
    /*public Employee(Integer idPerson, String name, String dni, LocalDate dateOfBirth, String password, Contact contact,
			     LocalDate entryDate, String cuit) {
		super(idPerson, name, dni, dateOfBirth, password, contact);
		this.entryDate = entryDate;
		this.cuit = cuit;
	}
    */
    
    

	/*public TurnDTO(int idTurn, String state, boolean active, String observation, ClientDTO client, EmployeeDTO employee, 
			Date date, int idServicio, LocalDateTime startTime, LocalDateTime endTime, String status, LocalDateTime creationTime) {
		super();
		this.idTurn = idTurn;
		this.state = state;
		this.active = active;
		this.observation = observation;
		this.client = client;
		this.employee = employee;
		this.date = date;
		this.idServicio = idServicio;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		this.creationTime = creationTime;
	}*/
    
    
    
    
}
