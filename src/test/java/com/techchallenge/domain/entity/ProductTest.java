package com.techchallenge.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.techchallenge.domain.enums.Category;

class ProductTest {
	
	@Test
	void testProductWithSucess() {
		Product product = new Product(1L, "X Bacon", "LANCHE", "test", new BigDecimal("10.0"), "image");
		assertEquals(1L,product.getId(), "Must Be Equals");
		assertEquals("X Bacon",product.getTitle(), "Must Be Equals");
		assertEquals("test",product.getDescription(), "Must Be Equals");
		assertEquals("image",product.getImage(), "Must Be Equals");
		assertEquals(new BigDecimal("10.0"),product.getPrice(), "Must Be Equals");
		assertEquals(Category.LANCHE,product.getCategory(), "Must Be Equals");
		
	}

	@Test
	void testProductWithInvalidCategory() {
		
		IllegalArgumentException assertThrows = assertThrows(IllegalArgumentException.class,
				() -> new Product(1L, "X Bacon", "xx", "test", new BigDecimal("10.0"), ""));
		
		assertEquals("Invalid Category!", assertThrows.getMessage(), "Must Be Equals");
		
	}

}
