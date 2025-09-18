package com.techchallenge.infrastructure.persistence.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import com.techchallenge.infrastructure.persistence.entity.ProductEntity;

public interface ProductRepository extends MongoRepository<ProductEntity, String> {
	
	List<ProductEntity> findByCategory(String category);
	
	List<ProductEntity> findByIdIn(@Param("ids") List<String> ids);

}
