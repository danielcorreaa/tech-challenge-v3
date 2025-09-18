package com.techchallenge.infrastructure.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public record CustomerRequest(
		@NotBlank(message = "Cpf is required!")
		@Size(max = 11, min = 11,  message = "Cpf without correct number of digits!")
		@Getter
		String cpf, 
		@NotBlank(message = "Name is required!") 
		@Getter
		String name, 
		@Getter		
		String email
		) {
		

}
