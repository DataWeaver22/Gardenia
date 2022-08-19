package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.ProductNew;

public interface ProductNewService {

	List<ProductNew> getAllProductNew();

	ProductNew saveProductNew(ProductNew productNew);

	ProductNew editProductNew(ProductNew productNew);

	ProductNew getProductNew(Long id);

	void deleteProductNewById(Long id);

}
