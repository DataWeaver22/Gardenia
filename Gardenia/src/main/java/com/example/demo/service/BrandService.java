package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Brand;

public interface BrandService {

	Brand saveBrand(Brand brand);

	Brand getBrandById(Long id);

	Brand editBrand(Brand brand);

	void deleteBrandById(Long id);

	List<Brand> getAllBrands();
}
