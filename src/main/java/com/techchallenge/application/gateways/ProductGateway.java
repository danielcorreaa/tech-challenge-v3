package com.techchallenge.application.gateways;

import java.util.List;
import java.util.Optional;

import com.techchallenge.domain.entity.Product;

public interface ProductGateway {
	
	Product insert(Product product);
	Product update(Product product);
	Optional<List<Product>> findByCategory(String category);
	void delete(String id);
	Optional<Product> findById(String id);
	Optional<List<Product>> findByIds(List<String> productsId);


}
