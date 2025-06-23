package com.grupo12.entities;

import java.time.LocalDateTime;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.mail.Service;
import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Data;
import lombok.AllArgsConstructor;



@Entity
@Table(name="turn")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Turn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTurn;
    
	@Column(name="createdat")
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@Column(name="updatedat")
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	@Column(name="active")
    private boolean active;
    
	@Column(name="observation")
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

    @ManyToOne
    @JoinColumn(name = "idServicio")
    private ServiceEntity service;
    
    @Column (name="start_time",nullable=false)
    private LocalDateTime startTime;
    
    @Column (name="end_time",nullable=false)//calculado en base a start_time y service.durationMinutes
    private LocalDateTime endTime;
    
    @Enumerated(EnumType.STRING)
    @Column (name="status",nullable=false)
    private TurnStatus status; //PENDIENTE, EN_ATENCION, ATENDIDO, AUSENTE, CANCELADO
    
    @Column (name="creation_time", nullable=false)
    private LocalDateTime creationTime;
    
    @Column(name = "reminder_sent")
    private Boolean reminderSent;
    
    @PrePersist
    protected void onCreate() {
    	creationTime=LocalDateTime.now();
    	if(status==null) {
    		status=TurnStatus.PENDIENTE;//Estado predeterminado para los turnos recién creados
    	}
    }
    public Boolean isReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(Boolean reminderSent) {
        this.reminderSent = reminderSent;
    }
    
	/*public Turn(int idTurn, String state, boolean active, String observation, Client client, Employee employee,
			Date date,Service service,LocalDateTime starTime,LocalDateTime endTime,TurnStatus status,LocalDateTime creationTime) {
		super();
		this.idTurn = idTurn;
		this.state = state;
		this.active = active;
		this.observation = observation;
		this.client = client;
		this.employee = employee;
		this.date = date;
		this.service=service;
		this.starTime=starTime;
		this.endTime=endTime;
		this.status=status;
		this.creationTime=creationTime;
	}
*/
    
    
}




