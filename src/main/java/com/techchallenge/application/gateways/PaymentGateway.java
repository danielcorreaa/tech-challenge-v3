package com.techchallenge.application.gateways;

import com.techchallenge.domain.entity.Payment;

public interface PaymentGateway {
	
	Payment insert(Payment payment);
	Payment update(Payment payment);
	
	Payment findByOrder(String id);
	int updateStatusPayment(String externalReferencelong);

}
