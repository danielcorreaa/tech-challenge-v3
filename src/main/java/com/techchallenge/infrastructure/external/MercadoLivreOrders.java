package com.techchallenge.infrastructure.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.techchallenge.infrastructure.external.dtos.OrderResponseML;
import com.techchallenge.infrastructure.external.dtos.OrdersML;

@FeignClient(value = "orders", url = "${api.mercadolivre.orders}")
public interface MercadoLivreOrders {
	
	@PostMapping
	OrderResponseML sendOrderToMl(@RequestHeader("Authorization") String bearerToken, OrdersML orders);
	
}
