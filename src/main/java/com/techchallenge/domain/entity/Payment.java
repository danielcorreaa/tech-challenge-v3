package com.techchallenge.domain.entity;

import java.io.InputStream;
import java.util.Optional;

import com.techchallenge.domain.enums.StatusPayment;

public class Payment {

	private InputStream qrCode;
	private StatusPayment statusPayment;
	private Order order;
	private String externalReference; 

	public Payment(InputStream qrCode, String statusPayment, Order order, String externalReference) {
		this.qrCode = qrCode;
		this.order = order;
		this.externalReference = externalReference;
		try {
			this.statusPayment = StatusPayment.valueOf(statusPayment);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid Status Payment!");
		}
	}

	public InputStream getQrCode() {
		return qrCode;
	}

	public Optional<StatusPayment> getStatusPayment() {
		return Optional.ofNullable(statusPayment);
	}

	public String getStatusPaymentString() {
		return getStatusPayment().map( status -> status.toString()).orElse("");
	}	
	
	public Order getOrder() {
		return order;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public String getExternalReferencelong() {		
		return externalReference;
	}

	public boolean IsAprooved() {	
		return this.statusPayment.equals(StatusPayment.APROVADO);
	}

	public void aprooved() {
		this.statusPayment = StatusPayment.APROVADO;
	}
}
