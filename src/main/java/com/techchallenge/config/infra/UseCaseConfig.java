package com.techchallenge.config.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.techchallenge.application.gateways.CustomerGateway;
import com.techchallenge.application.gateways.OrderGateway;
import com.techchallenge.application.gateways.PaymentExternalGateway;
import com.techchallenge.application.gateways.PaymentGateway;
import com.techchallenge.application.gateways.ProductGateway;
import com.techchallenge.application.usecases.CustomerUseCase;
import com.techchallenge.application.usecases.OrderUseCase;
import com.techchallenge.application.usecases.PaymentUseCase;
import com.techchallenge.application.usecases.ProductUseCase;
import com.techchallenge.application.usecases.interactor.CustomerUseCaseInteractor;
import com.techchallenge.application.usecases.interactor.OrderUseCaseInteractor;
import com.techchallenge.application.usecases.interactor.PaymentUseCaseInteractor;
import com.techchallenge.application.usecases.interactor.ProductUseCaseInteractor;

@Configuration
public class UseCaseConfig {
	
	@Bean
	public CustomerUseCase customerUseCase(CustomerGateway customerGateway) {
		return new CustomerUseCaseInteractor(customerGateway);
	}
	
	@Bean
	public ProductUseCase productUseCase(ProductGateway productGateway) {
		return new ProductUseCaseInteractor(productGateway);
	}
	
	@Bean
	public OrderUseCase orderUseCase(OrderGateway orderGateway, CustomerGateway customerGateway,
			ProductGateway productGateway) {
		return new OrderUseCaseInteractor(orderGateway, customerGateway, productGateway);
	}
	
	@Bean
	public PaymentUseCase paymentUseCase(PaymentExternalGateway paymentExternalGateway, OrderGateway orderGateway, 
			PaymentGateway paymentGateway) {
		return new PaymentUseCaseInteractor(paymentExternalGateway, orderGateway, paymentGateway);
	}

	
	
	
}
