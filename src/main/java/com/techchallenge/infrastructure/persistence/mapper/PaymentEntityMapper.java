package com.techchallenge.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.techchallenge.domain.entity.Order;
import com.techchallenge.domain.entity.Payment;
import com.techchallenge.infrastructure.persistence.entity.OrderEntity;
import com.techchallenge.infrastructure.persistence.entity.PaymentEntity;

@Component
public class PaymentEntityMapper {

	private OrderEntityMapper orderEntityMapper;

	public PaymentEntityMapper(OrderEntityMapper orderEntityMapper) {
		super();
		this.orderEntityMapper = orderEntityMapper;
	}

	public PaymentEntity toPaymentEntity(Payment payment) {
		OrderEntity orderEntity = orderEntityMapper.toOrderEntity(payment.getOrder());
		return new PaymentEntity(payment.getExternalReference(), payment.getStatusPaymentString(), orderEntity);
	}

	public Payment toPayment(PaymentEntity paymentEntity) {
		Order order = orderEntityMapper.toOrder(paymentEntity.getOrder());
		return new Payment(null, paymentEntity.getStatus(), order, null);
	}

}
