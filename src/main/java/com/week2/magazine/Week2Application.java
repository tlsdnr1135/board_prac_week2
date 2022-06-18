package com.week2.magazine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Week2Application {

	public static void main(String[] args) {
		SpringApplication.run(Week2Application.class, args);
	}

}
