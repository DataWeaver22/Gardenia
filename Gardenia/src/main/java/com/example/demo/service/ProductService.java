package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Product;

public interface ProductService {

	List<Product> getAllProduct();

	Product saveProduct(Product product);

	Product getProduct(Long id);

	Product editProduct(Product product);

	void deleteProductById(Long id);

}
