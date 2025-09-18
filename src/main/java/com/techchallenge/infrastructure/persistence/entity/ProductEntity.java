package com.techchallenge.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Document(collection = "product")
public class ProductEntity {

	@Id
	private String id;
	private String title;
	private String category;
	private String description;
	private BigDecimal price;
	private String image;


	public ProductEntity(String id, String title, String category, String description, BigDecimal price, String image) {

        this.id = id;
        this.title = title;
		this.category = category;
		this.description = description;
		this.price = price;
		this.image = image;
	}

}
