package com.techchallenge.application.usecases;

import com.techchallenge.domain.entity.Payment;

public interface PaymentUseCase {

	Payment validPayment(String idOrder, String webhook);
	
	Payment findByOrder(String idOrder);

	Payment webhook(String resource);
	
}
