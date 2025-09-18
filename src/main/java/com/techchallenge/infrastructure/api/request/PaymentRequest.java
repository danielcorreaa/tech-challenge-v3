package com.techchallenge.infrastructure.api.request;

import jakarta.validation.constraints.NotBlank;

public record PaymentRequest(
		@NotBlank(message = "OrderId is required!")
		String orderId) {

}
