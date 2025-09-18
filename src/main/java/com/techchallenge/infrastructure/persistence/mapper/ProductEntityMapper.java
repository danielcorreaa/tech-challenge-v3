package com.techchallenge.infrastructure.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import com.techchallenge.domain.entity.Product;
import com.techchallenge.infrastructure.persistence.entity.ProductEntity;

@Component
public class ProductEntityMapper {

	public ProductEntity toProductEntity(Product product) {	
		return new ProductEntity(product.getId(), product.getTitle(), product.getCategory().toString(),
				product.getDescription(), product.getPrice(), product.getImage());
	}

	public Product toProduct(ProductEntity entity) {		
		return new Product(entity.getId().toString(),entity.getTitle(),entity.getCategory(),
				entity.getDescription(), entity.getPrice(), entity.getImage());
	}

	public List<Product> toProductList(List<ProductEntity> entity) {		
		return entity.stream().map( e -> toProduct(e)).collect(Collectors.toList());
	}

	public List<ProductEntity> toProductEntityList(List<Product> product) {		
		return product.stream().map(this::toProductEntity).collect(Collectors.toList());
	}

}
