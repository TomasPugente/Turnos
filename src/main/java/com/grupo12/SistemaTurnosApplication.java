package com.grupo12;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.grupo12")
//@RestController
@EnableJpaRepositories(basePackages = "com.grupo12.repositories")
//@ComponentScan(basePackages = "com.grupo12")
@EntityScan(basePackages = "com.grupo12.entities")
public class SistemaTurnosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaTurnosApplication.class, args);
	}


}
