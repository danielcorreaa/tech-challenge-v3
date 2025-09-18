package com.techchallenge.infrastructure.persistence.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.techchallenge.domain.entity.Customer;
import com.techchallenge.domain.entity.Order;
import com.techchallenge.domain.entity.Product;
import com.techchallenge.infrastructure.persistence.entity.CustomerEntity;
import com.techchallenge.infrastructure.persistence.entity.OrderEntity;
import com.techchallenge.infrastructure.persistence.entity.ProductEntity;

@Component
public class OrderEntityMapper {

	private CustomerEntityMapper custumerEntityMapper;
	private ProductEntityMapper productEntityMapper;

	public OrderEntityMapper(CustomerEntityMapper custumerEntityMapper, ProductEntityMapper productEntityMapper) {
		super();
		this.custumerEntityMapper = custumerEntityMapper;
		this.productEntityMapper = productEntityMapper;
	}

	public OrderEntity toOrderEntity(Order order) {
		CustomerEntity customerEntity = null;
		if(Optional.ofNullable(order.getCustomer()).isPresent()) {
			 customerEntity = custumerEntityMapper.toCustomerEntity(order.getCustomer());
		}
		List<ProductEntity> productEntityList = productEntityMapper.toProductEntityList(order.getProcuts());
		return new OrderEntity(order.getId(), customerEntity, productEntityList, order.getInitOrder(),
				order.getFinishOrder(), order.getStatusOrderString());
	}

	public Order toOrder(OrderEntity entity) {
		Customer customer = null;
		if(Optional.ofNullable(entity.getCustomer()).isPresent()) {
			 customer = custumerEntityMapper.toCustomer(entity.getCustomer());
		}
		List<Product> products = productEntityMapper.toProductList(entity.getProducts());	
		
		return Order.convert(entity.getId().toString(), customer, products, entity.getDateOrderInit(), entity.getDateOrdernFinish(),
				entity.getStatusOrder());
	}

	public List<Order> toOrderList(List<OrderEntity> all) {
		return all.stream().map(order -> toOrder(order)).collect(Collectors.toList());
	}

}
