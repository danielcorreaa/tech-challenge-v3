package com.techchallenge.application.gateways;

import java.util.Optional;

import com.techchallenge.domain.entity.Customer;

public interface CustomerGateway {
	
	Customer insert(Customer custumer);
	Customer update(Customer custumer);
	Optional<Customer> findByCpf(String cpf);
	void delete(Customer customer);	

}
