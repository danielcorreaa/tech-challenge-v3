package com.techchallenge.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.techchallenge.domain.enums.StatusOrder;
import com.techchallenge.domain.errors.BusinessException;

class OrderTest {

	@Test
	void testCreateOrderWithStatusInOrder() {
		Customer customer = new Customer("37465505569", "Doug Funny", "doug@email");
		Product product = new Product(1L, "X Bacon", "LANCHE", "test", new BigDecimal("10.0"), "image");
		Order order = new Order();
		Order startOrder = order.startOrder(customer, List.of(product));
		
		assertEquals("37465505569", startOrder.getCustomer().getCpf().getValue(), "Must Be Equals");
		assertEquals("X Bacon", startOrder.getProcuts().get(0).getTitle(), "Must Be Equals");
		assertEquals(StatusOrder.RECEBIDO, startOrder.getStatusOrder().get(), "Must Be Equals");
		assertNotNull(startOrder.getInitOrder(), "Must Be Not null");
		assertNull(startOrder.getFinishOrder(), "Must Be null");
		assertNotNull(startOrder.getMinutesDurationOrder(), "Must Be Not null");		
		
		assertEquals(StatusOrder.EM_PREPARACAO, startOrder.sendToPreparation().getStatusOrder().get(), "Must Be Equals");
		
		startOrder = startOrder.ready();
		assertEquals(StatusOrder.PRONTO, startOrder.getStatusOrder().get(), "Must Be Equals");
		
		startOrder = startOrder.finishOrder(1L);
		assertEquals(StatusOrder.FINALIZADO, startOrder.getStatusOrder().get(), "Must Be Equals");
		
		assertNotNull(startOrder.getFinishOrder(), "Must Be Not null");		
		
	}
	
	@Test
	void testCreateOrderTryChangeStatusFinalizadoToRecebido() {		
		Order order = new Order();
		Customer customer = new Customer("37465505569", "Doug Funny", "doug@email");
		Product product = new Product(1L, "X Bacon", "LANCHE", "test", new BigDecimal("10.0"), "image");
		order.startOrder(customer,  List.of(product));
		try {
			order.finishOrder(1L);
		}catch (Exception e) {
			System.out.println(e);
		}
		
		BusinessException exception = assertThrows(BusinessException.class, () -> order.finishOrder(1L));		
		assertEquals("Invalid status to order", exception.getMessage(), "Is impossible Start Order with StatusEM_PREPARACAO ");
		
	}
	
	@Test
	void testCreateOrderWithStatusDisorder() {		
		Order order = new Order();
		BusinessException exception = assertThrows(BusinessException.class, () -> order.sendToPreparation());		
		assertEquals("Invalid status to order", exception.getMessage(), "Is impossible Start Order with StatusEM_PREPARACAO ");
		
	}

}
