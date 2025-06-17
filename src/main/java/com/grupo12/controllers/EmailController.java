package com.grupo12.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo12.services.implementation.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/test")
    public String sendTestEmail() {
        emailService.sendSimpleMail("tumipugente@gmail.com", "Correo de prueba",
                "¡Hola! Este es un correo de prueba desde Spring Boot.");
        System.out.println("Correo enviado correctamente");
        return "Correo enviado correctamente";
    }
}
