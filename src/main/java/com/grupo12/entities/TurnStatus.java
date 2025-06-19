package com.grupo12.entities;

public enum TurnStatus{
	PENDIENTE, //Turno disponible para ser tomado o esperando ser llamado
	EN_ATENCION, //El cliente esta siendo atendido
	ATENDIDO, //El cliente asistio y fue atendido
	AUSENTE, //El cliente no asistio
	CANCELADO //El turno fue cancelado (por cliente o empleado)
}


