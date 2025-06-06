package com.grupo12.services;

import com.grupo12.models.TurnDTO;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class NotificationService {
	
	@Autowired
	private JavaMailSender mailSender; 
	
	public void sendTurnReminder(TurnDTO turn) {
		if(turn.getClientEmail()==null || turn.getClientEmail().isEmpty()) {
			System.out.println("No se pudo enviar recordatorio para turno " + turn.getClientIdPerson() + ": Cliente sin Email");
			return;
		}
		String recipient= turn.getClientEmail();
		String clientName=turn.getClientName();
		String serviceName=turn.getServiceName();
		String employeeName=turn.getEmployeeName()+ " el Profesional ";
		String startTimeFormatted=turn.getStartTime().toLocalDate().toString()+ " a las "+ turn.getStartTime().toLocalTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
		
		String subject= "Recordatorio: Tu proximo turno";
		String body= String.format("Estimado/a %s, \n\n"+ "Le recordamos que tiene un turno programado:\n"+" Servicio: %s\n"+" Con: %s\n"+
				"Fecha y Hora: %s\n\n"+" Por Favor sea Puntual!!\n"+" Lo Esperamos!"+" Gracias!\n"
					+" El equipo de Gestion de Turnos.",clientName,serviceName,employeeName,startTimeFormatted);
		//System.out.println("--- ENVIADO RECORDATORIO POR EMAIL --- ");
		
		
		//Para configurar el envio de email!
		try {
			SimpleMailMessage message=new SimpleMailMessage();
			message.setFrom("gestordeturnosunla@gmail.com");
			message.setTo(recipient);
			message.setSubject(subject);
			message.setText(body);
			mailSender.send(message);
			System.out.println("------ RECORDATORIO POR EMAIL ENVIADO CON EXITO ----- ");
			System.out.println("Para: "+recipient);
			System.out.println("Asunto: "+subject);
			System.out.println("Cuerpo: "+body);
			System.out.println("--------------------------------");
		}catch (Exception e) {
			System.err.println("Error al enviar recordatorio de email para turno "+ turn.getIdTurn()+": "+e.getMessage());
			e.printStackTrace();
		}
		
	}
}
