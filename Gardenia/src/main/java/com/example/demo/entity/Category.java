package com.example.demo.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="category")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, length = 20)
	private Long Id;

	@Column(name = "categoryName", length = 20)
    private String categoryName;
	
	@Column(name = "updatedDateTime", length = 20)
    private LocalDateTime updatedDateTime;
	 
	@ManyToOne
    @JoinColumn(name = "brandId")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	Brand brand;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
	public LocalDateTime getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public Category(Long id, String categoryName, Brand brand,LocalDateTime updatedDateTime) {
		super();
		Id = id;
		this.categoryName = categoryName;
		this.brand = brand;
		this.updatedDateTime = updatedDateTime;
	}

	public Category() {
		// TODO Auto-generated constructor stub
	}
	
	
}
