package com.techchallenge.application.usecases.interactor;

import java.util.List;

import org.bson.types.ObjectId;

import com.techchallenge.application.gateways.ProductGateway;
import com.techchallenge.application.usecases.ProductUseCase;
import com.techchallenge.domain.entity.Product;
import com.techchallenge.domain.errors.NotFoundException;

public class ProductUseCaseInteractor implements ProductUseCase{
	
	private final ProductGateway productGateway;

	public ProductUseCaseInteractor(ProductGateway productGateway) {
		super();
		this.productGateway = productGateway;
	}
	
	public Product insert(Product product) {
		return productGateway.insert(product);
	}
	
	public Product update(Product product) {
		findById(product.getId());
		return productGateway.update(product);
	}

	public List<Product> findByCategory(String category) {
		return productGateway.findByCategory(category)
				.orElseThrow(()-> new NotFoundException("Products not found!"));		
	}

	public void delete(String id) {
		productGateway.delete(id);						
	}

	public Product findById(String id) {		
		return productGateway.findById(id).orElseThrow(() -> new NotFoundException("Product not found!"));
	}
	
	

}
