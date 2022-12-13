package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Brand;
import com.example.demo.repository.BrandRepository;
import com.example.demo.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService{
	
	@Autowired
	private BrandRepository brandRepository;

	@Override
	public Brand saveBrand(Brand brand) {
		// TODO Auto-generated method stub
		return brandRepository.save(brand);
	}

	@Override
	public Brand getBrandById(Long id) {
		// TODO Auto-generated method stub
		return brandRepository.findById(id).get();
	}

	@Override
	public Brand editBrand(Brand brand) {
		// TODO Auto-generated method stub
		return brandRepository.save(brand);
	}

	@Override
	public void deleteBrandById(Long id) {
		// TODO Auto-generated method stub
		brandRepository.deleteById(id);
	}

	@Override
	public List<Brand> getAllBrands() {
		// TODO Auto-generated method stub
		return brandRepository.findAll();
	}

}
