package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ProductDetails;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Long>{

	List<ProductDetails> findByParam(long id);

	
}
