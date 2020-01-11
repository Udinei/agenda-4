package com.jvs.gd;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Agenda4Application {

	public static void main(String[] args) {
		SpringApplication.run(Agenda4Application.class, args);
	}
	
   // cria um singeton de modelMapper
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
}
