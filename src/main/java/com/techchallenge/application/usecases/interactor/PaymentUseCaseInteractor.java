package com.techchallenge.application.usecases.interactor;

import com.techchallenge.application.gateways.OrderGateway;
import com.techchallenge.application.gateways.PaymentExternalGateway;
import com.techchallenge.application.gateways.PaymentGateway;
import com.techchallenge.application.usecases.PaymentUseCase;
import com.techchallenge.domain.entity.Order;
import com.techchallenge.domain.entity.Payment;
import com.techchallenge.domain.errors.NotFoundException;

public class PaymentUseCaseInteractor implements PaymentUseCase {

	private PaymentExternalGateway paymentExternalGateway;
	private OrderGateway orderGateway;
	private PaymentGateway paymentGateway;

	public PaymentUseCaseInteractor(PaymentExternalGateway paymentExternalGateway, OrderGateway orderGateway,
			PaymentGateway paymentGateway) {
		super();
		this.paymentExternalGateway = paymentExternalGateway;
		this.orderGateway = orderGateway;
		this.paymentGateway = paymentGateway;
	}

	public Payment validPayment(String idOrder, String webhook) {
		Order order = orderGateway.findbyId(idOrder).orElseThrow(() -> new NotFoundException("Order not found"));
		Payment pay = paymentExternalGateway.pay(order, webhook);
		paymentGateway.insert(pay);
		return pay;
	}

	@Override
	public Payment findByOrder(String idOrder) {		
		return paymentGateway.findByOrder(idOrder);
	}

	@Override
	public Payment webhook(String resource) {	
		Payment checkPayment = paymentExternalGateway.checkPayment(resource);
		if(checkPayment.IsAprooved()) {
			Order order = orderGateway.findbyId(checkPayment.getExternalReferencelong())
						.orElseThrow(() -> new NotFoundException("Order not found"));
			
			int result =  paymentGateway.updateStatusPayment(checkPayment.getExternalReferencelong());	
			if(result != 0) {
				order.sendToPreparation();
				orderGateway.insert(order);
			}
		
		}		
		return checkPayment;
	}

}
