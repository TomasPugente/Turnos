package com.grupo12.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TurnDTO {

    // Turno
    private Integer idTurn;
    private boolean active;
    private String observation;
    private String status; // Representa el enum como String
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime creationTime;
    //private Date date;
    private LocalDate date;
    private LocalTime hour;
    private Boolean reminderSent;
    private int durationMinutes;


    // Cliente asociado
    private Integer clientIdPerson;
    private String clientName;
    private String clientDni;
    private LocalDate clientDateOfBirth;
    private String clientPassword;
    private String clientEmail;

    // Empleado asociado
    private Integer employeeIdPerson;
    private String employeeName;
    private String employeeDni;
    private LocalDate employeeDateOfBirth;
    private String employeePassword;
    private String employeeEmail;
    private String employeeCuit;
    private LocalDate employeeEntryDate;

    // Servicio
    private Integer serviceId;
    private String serviceName;

    // Métodos utilitarios si los necesitás

    public int getEmployeeId() {
        return employeeIdPerson != null ? employeeIdPerson : 0;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeIdPerson = employeeId;
    }
}
