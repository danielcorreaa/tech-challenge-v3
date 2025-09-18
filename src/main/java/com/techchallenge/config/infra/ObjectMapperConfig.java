package com.techchallenge.config.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ObjectMapperConfig {
	
	@Bean
	@Primary
	public ObjectMapper objectMapper() {
	    return new ObjectMapper()	     
	        .setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
	}
	
	

}
