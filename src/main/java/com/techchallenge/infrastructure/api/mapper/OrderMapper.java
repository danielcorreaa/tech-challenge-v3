package com.techchallenge.infrastructure.api.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.techchallenge.domain.entity.Order;
import com.techchallenge.infrastructure.api.request.CustomerResponse;
import com.techchallenge.infrastructure.api.request.OrderResponse;
import com.techchallenge.infrastructure.api.request.ProductResponse;

@Component
public class OrderMapper {

	private CustomerMapper customerMapper;
	private ProductMapper productMapper;

	public OrderMapper(CustomerMapper customerMapper, ProductMapper productMapper) {
		super();
		this.customerMapper = customerMapper;
		this.productMapper = productMapper;
	}
	
	public OrderResponse toOrderResponse(Order order) {
		CustomerResponse customer = null;
		if(Optional.ofNullable(order.getCustomer()).isPresent()) {
			 customer = customerMapper.toCustomerResponse(order.getCustomer());
		}
		List<ProductResponse> products = productMapper.toProductResponseList(order.getProcuts());	
		return new OrderResponse(order.getId(), customer, products, order.getInitOrder(), order.getFinishOrder(), order.getMinutesDurationOrder(),
				order.getStatusOrderString());
	
	}

	public List<OrderResponse> toOrderListResponse(List<Order> orders) {
		return orders.stream().map(order -> toOrderResponse(order)).collect(Collectors.toList());
	}

}
