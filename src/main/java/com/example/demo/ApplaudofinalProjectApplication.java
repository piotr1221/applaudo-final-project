package com.example.demo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.utility.KeycloakScopeVerifier;
import com.example.demo.utility.MyModelMapper;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@SpringBootApplication
@OpenAPIDefinition
public class ApplaudofinalProjectApplication {

	@Bean
	public MyModelMapper myModelMapper() {
		return new MyModelMapper(new ModelMapper());
	}
	
	@Bean
	public KeycloakScopeVerifier scopeVerifier() {
		return new KeycloakScopeVerifier();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ApplaudofinalProjectApplication.class, args);
	}

}
