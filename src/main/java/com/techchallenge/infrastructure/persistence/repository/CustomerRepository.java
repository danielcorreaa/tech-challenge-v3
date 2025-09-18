package com.techchallenge.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.techchallenge.infrastructure.persistence.entity.CustomerEntity;

public interface CustomerRepository extends MongoRepository<CustomerEntity, String> {

	Optional<CustomerEntity> findByCpf(String cpf);

}
