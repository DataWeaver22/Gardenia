package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ProductNew;

public interface ProductNewRepository extends JpaRepository<ProductNew, Long>{
	
}
