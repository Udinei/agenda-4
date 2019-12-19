package com.jvs.gd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class Agenda4Application {

	public static void main(String[] args) {
		SpringApplication.run(Agenda4Application.class, args);
	}

}
