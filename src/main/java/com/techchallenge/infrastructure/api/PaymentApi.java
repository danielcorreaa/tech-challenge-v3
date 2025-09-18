package com.techchallenge.infrastructure.api;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.techchallenge.application.usecases.PaymentUseCase;
import com.techchallenge.config.infra.Result;
import com.techchallenge.domain.entity.Payment;
import com.techchallenge.infrastructure.api.mapper.PaymentMapper;
import com.techchallenge.infrastructure.api.request.PaymentWebhookRequest;
import com.techchallenge.infrastructure.api.request.PaymentRequest;
import com.techchallenge.infrastructure.api.request.PaymentResponse;

@RestController
@RequestMapping("api/v1/payment")
@CrossOrigin
public class PaymentApi {

	private PaymentUseCase paymentUseCase;
	private PaymentMapper mapper;

    @Value("${webhook.pipedream.net}")
    private String notificationUrl;

	public PaymentApi(PaymentUseCase paymentUseCase, PaymentMapper mapper) {
		super();
		this.paymentUseCase = paymentUseCase;
		this.mapper = mapper;
	}

	@PostMapping("/pay")
	public ResponseEntity<InputStreamResource> checkout(@RequestBody PaymentRequest request, UriComponentsBuilder uri ) throws IOException {
        UriComponents uriComponents = uri.path("/api/v1/payment/webhook").build();
        String notification = uriComponents.toUriString();
        if(StringUtils.isNotBlank(notificationUrl)){
            notification = notificationUrl;
        }
        System.out.printf("notification: %s\n", notification);

        Payment payment = paymentUseCase.validPayment(request.orderId(), notification);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new InputStreamResource(payment.getQrCode()));
	}

	@GetMapping("/find/order/{sku}")
	public ResponseEntity<Result<PaymentResponse>> findbyOrder(@PathVariable String sku) throws IOException {
		Payment payment = paymentUseCase.findByOrder(sku);
		return ResponseEntity.ok(Result.ok(mapper.toPaymentResponse(payment)));

	}
	
	@PostMapping("/webhook")
	public ResponseEntity<Result<PaymentResponse>> webhook(@RequestBody PaymentWebhookRequest request) throws IOException {
		Payment payment = paymentUseCase.webhook(request.resource());
		return ResponseEntity.ok(Result.ok(mapper.toPaymentResponse(payment)));

	}

}
