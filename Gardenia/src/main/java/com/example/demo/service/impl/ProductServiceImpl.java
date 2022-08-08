package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
	private ProductRepository productRepository;
	
	public ProductServiceImpl(ProductRepository productRepository) {
		super();
		this.productRepository = productRepository;
	}

	@Override
	public List<Product> getAllProduct(){
		return productRepository.findAll();
	}
	
	@Override
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}
	
	@Override
	public Product getProduct(Long id) {
		return productRepository.findById(id).get();
	}

	@Override
	public Product editProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public void deleteProductById(Long id) {
		// TODO Auto-generated method stub
		productRepository.deleteById(id);
	}
}
