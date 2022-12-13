package com.example.demo.entity;

import javax.persistence.CascadeType;
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
@Table(name = "brand_associated_to_distributor")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BrandAssociatedToDist {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "brandId")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	Brand brand;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "distributorId")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	Distributor distributor;

	public BrandAssociatedToDist() {
		
	}
	
	public BrandAssociatedToDist(Brand brand,Distributor distributor,Long id) {
		super();
		this.id = id;
		this.brand = brand;
		this.distributor = distributor;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Distributor getDistributor() {
		return distributor;
	}

	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

		
}
