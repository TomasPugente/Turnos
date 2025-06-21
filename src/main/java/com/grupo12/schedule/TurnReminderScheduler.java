package com.grupo12.schedule;

import com.grupo12.models.TurnDTO;
import com.grupo12.services.ITurnService;
import com.grupo12.services.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class TurnReminderScheduler {
	//scheduler = planificador
	@Autowired
	private ITurnService turnService;
	@Autowired
	private NotificationService notificationService;
	
	//CU011: Enviar Recordatorio de Turno
	//Se ejecutara cada 30 min
	public void sendUpcomingTurnReminders() {
		LocalDateTime now= LocalDateTime.now();
		//Definir el rango para los recordatorios. Esta para turnos en las proximas 24hs
		LocalDateTime reminderWindowEnd=now.plus(24, ChronoUnit.HOURS);
		
		//empezar a buscar desde 1 hora en el futuro para no recordar turnos que ya pasaron o estan muy cerca
		LocalDateTime reminderWindowStartDateTime=now.plus(1, ChronoUnit.HOURS); 
		
		System.out.println("Ejecutando scheduler de recordatorios de turnos entre "+reminderWindowStartDateTime + " y "+reminderWindowEnd);
		
	//	List<TurnDTO> upcomingTurns=turnService.getUpComingTurnsForReminders(reminderWindowStartDateTime, reminderWindowEnd);
		List<TurnDTO> upcomingTurns=turnService.findUpcomingTurns(reminderWindowStartDateTime, reminderWindowEnd );
		if(upcomingTurns.isEmpty()) {
			System.out.println("No hay turnos proximos en el rango definido para enviar recordatorios");
			return;
		}
		for(TurnDTO turn : upcomingTurns) {
			if(turn.getClientEmail()!=null && !turn.getClientEmail().isEmpty()) {
				notificationService.sendTurnReminder(turn);
			}else {
				System.out.println("Advertencia: No se pudo enviar recordatorio para turno "+ turn.getIdTurn() + " debido a que el cliente no tiene email registrado");
			}
		}
		System.out.println("Recordatorios de turnos procesados para " + upcomingTurns.size()+" turnos"); 
	}
}
