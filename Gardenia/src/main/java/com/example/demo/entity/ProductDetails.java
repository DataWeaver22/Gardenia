package com.example.demo.entity;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@Entity
@Table(name = "productDetails")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProductDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, length = 20)
	private Long Id;
	
	
	
	@ManyToOne(fetch =FetchType.LAZY)
	@JoinColumn(name = "productId")
	Product product;
	
	@Column(name = "quantity")
	private int quantity;
	
	@Column(name = "value"  )
	private BigDecimal value;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updateStockId")
	UpdateStock updateStock;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	
	

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public UpdateStock getUpdateStock() {
		return updateStock;
	}

	public void setUpdateStock(UpdateStock updateStock) {
		this.updateStock = updateStock;
	}

	/**
	 * @param id
	 * @param distributor
	 * @param product
	 * @param quantity
	 * @param value
	 * @param updateStock
	 */
	public ProductDetails(Long id, Product product, int quantity, BigDecimal value,
			UpdateStock updateStock) {
		Id = id;
		
		this.product = product;
		this.quantity = quantity;
		this.value = value;
		this.updateStock = updateStock;
	}

	/**
	 * 
	 */
	public ProductDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

	