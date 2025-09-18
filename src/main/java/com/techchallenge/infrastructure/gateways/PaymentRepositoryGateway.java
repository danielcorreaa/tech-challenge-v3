package com.techchallenge.infrastructure.gateways;

import com.techchallenge.infrastructure.persistence.repository.PaymentCustomRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import com.techchallenge.application.gateways.PaymentGateway;
import com.techchallenge.domain.entity.Payment;
import com.techchallenge.domain.errors.NotFoundException;
import com.techchallenge.infrastructure.persistence.entity.PaymentEntity;
import com.techchallenge.infrastructure.persistence.mapper.PaymentEntityMapper;
import com.techchallenge.infrastructure.persistence.repository.PaymentRepository;

@Component
public class PaymentRepositoryGateway implements PaymentGateway {

	private final PaymentRepository paymentRepository;
	private final PaymentEntityMapper mapper;
    private final PaymentCustomRepository paymentCustomRepository;

	public PaymentRepositoryGateway(PaymentRepository paymentRepository, PaymentEntityMapper mapper, PaymentCustomRepository paymentCustomRepository) {
		super();
		this.paymentRepository = paymentRepository;
		this.mapper = mapper;
        this.paymentCustomRepository = paymentCustomRepository;
    }

	@Override
	public Payment insert(Payment payment) {
		PaymentEntity paymentEntity = mapper.toPaymentEntity(payment);
        var test =  paymentRepository.findById(paymentEntity.getOrder().getId());
        System.out.printf("tste"+test.isPresent());
        if(paymentRepository.findById(paymentEntity.getOrder().getId()).isPresent()) {
			return mapper.toPayment(paymentEntity);
		}
		paymentEntity = paymentRepository.save(paymentEntity);
		return mapper.toPayment(paymentEntity);
	}

	@Override
	public Payment findByOrder(String id) {
		PaymentEntity paymentByOrder = paymentRepository.findById(id).orElseThrow(() -> new NotFoundException("Payment not found"));
		return mapper.toPayment(paymentByOrder);		
	}

	@Override
	public Payment update(Payment payment) {
		PaymentEntity paymentEntity = mapper.toPaymentEntity(payment);
		paymentEntity = paymentRepository.save(paymentEntity);
		return mapper.toPayment(paymentEntity);
	}

	@Override
	public int updateStatusPayment(String externalReferencelong) {
		return paymentCustomRepository.updateStatusById(new ObjectId(externalReferencelong), "APROVADO");
	}

}
