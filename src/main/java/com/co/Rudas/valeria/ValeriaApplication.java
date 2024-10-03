package com.co.Rudas.valeria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ValeriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValeriaApplication.class, args);
	}

}
