package com.techchallenge.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.techchallenge.domain.entity.Customer;
import com.techchallenge.infrastructure.persistence.entity.CustomerEntity;

@Component
public class CustomerEntityMapper {
	
	public CustomerEntity toCustomerEntity(Customer customer) {	
		return new CustomerEntity(customer.getCpfValue().orElse(null), customer.getName(), customer.getEmail());
	}

	public Customer toCustomer(CustomerEntity customer) {
		return new Customer(customer.getCpf(), customer.getNome(), customer.getEmail());
	}

}
