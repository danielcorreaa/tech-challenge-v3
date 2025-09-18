package com.techchallenge.infrastructure.external.mapper;

import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.techchallenge.domain.entity.Order;
import com.techchallenge.domain.entity.Payment;
import com.techchallenge.domain.enums.StatusPayment;

@Component
public class PaymentExternalMapper {

	public Payment toPayment(InputStream in, Order order) {
		return new Payment(in, StatusPayment.AGUARDANDO.toString(), order, order.getId());
	}

	public Payment toPayment(String externalReference, Boolean approved) {
		StatusPayment status = approved ? StatusPayment.APROVADO : StatusPayment.REPROVADO;
		return new Payment(null, status.toString(), null, externalReference);
	}

}
