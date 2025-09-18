package com.techchallenge.infrastructure.external.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.techchallenge.domain.entity.Order;
import com.techchallenge.domain.entity.Product;
import com.techchallenge.infrastructure.external.dtos.ItemsML;
import com.techchallenge.infrastructure.external.dtos.OrdersML;

@Component
public class OrdersMLMapper {

	public OrdersML toOrdersML(Order order, String webhook) {
		
		BigDecimal totalAmount = order.getProcuts().stream().map(Product::getPrice)
			.reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
		List<ItemsML> items = new ArrayList<>();
		for (Product product : order.getProcuts()) {
			ItemsML item = new ItemsML(String.valueOf(product.getId()), 
					product.getCategory().toString(), product.getTitle(), product.getDescription(), 
					product.getPrice(), 1, "unit", product.getPrice());
			items.add(item);
		}
		
		return new OrdersML(order.getId().toString(), "Lanchonete Checkout", "Checkout", 
				webhook, totalAmount, items);
	}


	
}
