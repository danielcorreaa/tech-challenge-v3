package com.techchallenge.application.usecases;

import java.util.List;

import com.techchallenge.domain.entity.Product;

public interface ProductUseCase {
	
	Product insert(Product product);
	Product update(Product product);
	List<Product> findByCategory(String category);
	void delete(String id);
	Product findById(String id);

}
