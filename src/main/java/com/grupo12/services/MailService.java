package com.grupo12.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	 @Autowired
	    private JavaMailSender mailSender;

	    public void sendSimpleMail(String to, String subject, String text) {
	    	try {
		    	SimpleMailMessage message = new SimpleMailMessage();
		        message.setFrom("gestordeturnosunla@gmail.com"); // Cambiá por uno real
		        message.setTo(to);
		        message.setSubject(subject);
		        message.setText(text);
		        mailSender.send(message);
		        System.out.println("Enviando mail a: " + to);
		        System.out.println("✅ Mail enviado a: " + to);
	        } catch (Exception e) {
	            System.err.println("❌ Error al enviar mail a: " + to + " - " + e.getMessage());
	        }
	    }
}
