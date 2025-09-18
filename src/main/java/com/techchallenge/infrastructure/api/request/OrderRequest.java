package com.techchallenge.infrastructure.api.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public record OrderRequest(
		@Getter		
		String cpfCustumer, 
		
		@Getter	
		@NotNull(message = "Products is required!")		
		List<String> products
		) {

}
