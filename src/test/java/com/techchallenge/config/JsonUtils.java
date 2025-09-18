package com.techchallenge.config;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	private ObjectMapper objectMapper;

	public JsonUtils(ObjectMapper objectMapper) {		
		this.objectMapper = objectMapper;
	}
	

	public Optional<String> toJson(Object obj) {
		try {
			return Optional.ofNullable(objectMapper.writeValueAsString(obj));
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
			return Optional.empty();
		}
	}
	
	public <T> Optional<T> parse(String json, Class<T> clazz) {
		try {
			return Optional.ofNullable(objectMapper.readValue(json, clazz));
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
			return Optional.empty();
		}
	}

	public <T> Optional<T> parseResponse(String json, Class<T> clazz) {		
		try {
			return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
		} catch (IOException e) {
			return Optional.empty();
		}
		
	}

	public <T> Optional<T> parse(String json,TypeReference<T> clazz) {
		try {
			return Optional.ofNullable(objectMapper.readValue(json, clazz));
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
			return Optional.empty();
		}
	}
	
	

}
