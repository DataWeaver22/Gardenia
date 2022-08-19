package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.ProductNew;
import com.example.demo.repository.ProductNewRepository;
import com.example.demo.service.ProductNewService;

@Service
public class ProductNewServiceImpl implements ProductNewService {
private ProductNewRepository productNewRepository;
	
	public ProductNewServiceImpl(ProductNewRepository productNewRepository) {
		super();
		this.productNewRepository = productNewRepository;
	}

	@Override
	public List<ProductNew> getAllProductNew(){
		return productNewRepository.findAll();
	}
	
	@Override
	public ProductNew saveProductNew(ProductNew productNew) {
		return productNewRepository.save(productNew);
	}
	
	@Override
	public ProductNew getProductNew(Long id) {
		return productNewRepository.findById(id).get();
	}

	@Override
	public ProductNew editProductNew(ProductNew productNew) {
		return productNewRepository.save(productNew);
	}

	@Override
	public void deleteProductNewById(Long id) {
		// TODO Auto-generated method stub
		productNewRepository.deleteById(id);
	}
}
