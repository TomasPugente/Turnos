package com.grupo12;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling //Habilita la programacion de tareas
public class SistemaTurnosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaTurnosApplication.class, args);
	}

}
