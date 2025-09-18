package com.techchallenge.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CustomerTest {
	
	@Test
	void testCustumerWithValidCpf() {
		Customer customer = new Customer("37465505569", "Doug Funny","doug@email");
		assertEquals("37465505569", customer.getCpf().getValue(), "Must Be Equals");		
		assertEquals("Doug Funny", customer.getName(), "Must Be Equals");
		assertEquals("doug@email", customer.getEmail(), "Must Be Equals");
		assertEquals("374.655.055-69", customer.getFormatCpf(), "Must Be Equals");
	}
	
	@Test
	void testCustumerWithValidCpfRemoveEspecialCaracter() {
		Customer customer = new Customer("374.655.055-69", "Doug Funny", "doug@email");
		assertEquals("37465505569", customer.getCpf().getValue(), "Must Be Equals");		
		assertEquals("Doug Funny", customer.getName(), "Must Be Equals");
		assertEquals("doug@email", customer.getEmail(), "Must Be Equals");
		assertEquals("374.655.055-69", customer.getFormatCpf(), "Must Be Equals");
	}

	@Test
	void testCustumerWithInvalidCpf() {
		
		IllegalArgumentException assertThrows = assertThrows(IllegalArgumentException.class, 
				() -> new Customer("999999999", "Zé Comeia", "email@email")
		);
	
		assertEquals("Invalid Cpf!", assertThrows.getMessage(), "Must Be Equals");
	}
	
	@Test
	void testCustumerWithNullCpf() {
		
		IllegalArgumentException assertThrows = assertThrows(IllegalArgumentException.class, 
				() -> new Customer(null, "Zé Comeia", "email@email")
		);
	
		assertEquals("Invalid Cpf!", assertThrows.getMessage(), "Must Be Equals");
	}

}
