package com.sara.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSecurityAuth0Application {

	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SpringSecurityAuth0Application.class);
		app.run(args);

		
	}

	
}
