package com.techchallenge.application.gateways;

import com.techchallenge.domain.entity.Order;
import com.techchallenge.domain.entity.Payment;

public interface PaymentExternalGateway {

	Payment pay(Order order, String webhook);

	Payment checkPayment(String resource);
	
}
