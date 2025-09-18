package com.techchallenge.infrastructure.api.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.techchallenge.domain.entity.Product;
import com.techchallenge.infrastructure.api.request.InsertProductRequest;
import com.techchallenge.infrastructure.api.request.ProductResponse;
import com.techchallenge.infrastructure.api.request.UpdateProductRequest;

@Component
public class ProductMapper {

	public Product toProduct(UpdateProductRequest request) {
		return new Product(request.getId(),request.getTitle(), request.getCategory(), 
				request.getDescription(), request.getPrice(), request.getImage());
	}
	public Product toProduct(InsertProductRequest request) {
		return new Product(request.sku(),request.getTitle(), request.getCategory(),
				request.getDescription(), request.getPrice(), request.getImage());
	}
	
	public ProductResponse toProductResponse(Product product) {
		return new ProductResponse(product.getId(), product.getTitle(), product.getCategory().toString(), 
				product.getDescription(), product.getPrice(), product.getImage());
	}

	public List<ProductResponse> toProductResponseList(List<Product> product) {
		return product.stream().map(this::toProductResponse).toList();
	}
}
