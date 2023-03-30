package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ProductDetails;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Long>{

	
}
