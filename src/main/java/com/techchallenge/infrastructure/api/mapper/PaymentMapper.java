package com.techchallenge.infrastructure.api.mapper;

import org.springframework.stereotype.Component;

import com.techchallenge.domain.entity.Payment;
import com.techchallenge.infrastructure.api.request.PaymentResponse;

@Component
public class PaymentMapper {

	
	public PaymentResponse toPaymentResponse(Payment payment) {		
		return new PaymentResponse(payment.getStatusPaymentString());
	}

}
