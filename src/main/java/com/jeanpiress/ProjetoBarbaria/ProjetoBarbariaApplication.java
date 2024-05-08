package com.jeanpiress.ProjetoBarbaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProjetoBarbariaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoBarbariaApplication.class, args);
	}

}
