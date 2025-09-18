package com.techchallenge.infrastructure.gateways;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.techchallenge.application.gateways.CustomerGateway;
import com.techchallenge.domain.entity.Customer;
import com.techchallenge.infrastructure.persistence.entity.CustomerEntity;
import com.techchallenge.infrastructure.persistence.mapper.CustomerEntityMapper;
import com.techchallenge.infrastructure.persistence.repository.CustomerRepository;

@Component
public class CustomerRepositoryGateway implements CustomerGateway {

	private CustomerRepository repository;

	private CustomerEntityMapper mapper;

	public CustomerRepositoryGateway(CustomerRepository repository, CustomerEntityMapper mapper) {
		super();
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public Customer insert(Customer customer) {
		CustomerEntity entity = repository.save(mapper.toCustomerEntity(customer));
		return mapper.toCustomer(entity);
	}

	@Override
	public Customer update(Customer customer) {
		CustomerEntity entity = repository.save(mapper.toCustomerEntity(customer));
		return mapper.toCustomer(entity);
	}

	@Override
	public Optional<Customer> findByCpf(String cpf) {
		var customerEntity = repository.findByCpf(cpf);
		return customerEntity.map( customer -> mapper.toCustomer(customer));
	}

	@Override
	public void delete(Customer customer) {
		repository.delete(mapper.toCustomerEntity(customer));
	}

	

}
