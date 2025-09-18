package com.techchallenge.infrastructure.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Document(collection = "orderdb" )
public class OrderEntity {
	
	@Id
	private String id;
	
	private CustomerEntity customer;
	
	private List<ProductEntity> products;
	
	private LocalDateTime dateOrderInit;
	private LocalDateTime dateOrdernFinish;
	private String statusOrder;


}
