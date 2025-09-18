package com.techchallenge.domain.entity;

import java.math.BigDecimal;

import com.techchallenge.domain.enums.Category;

public class Product {

	private String id;
	private String title;
	private Category category;
	private String description;
	private BigDecimal price;
	private String image;

	public Product(String id, String title, String categoryName, String description, BigDecimal price, String image) {
		
		try{
			this.category = Category.valueOf(categoryName);
		}catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid Category!");
		}
		
		this.id = id;
		this.title = title;		
		this.description = description;
		this.price = price;
		this.image = image;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
