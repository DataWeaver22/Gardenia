package com.example.demo.Import.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Import.BrandImportHelper;
import com.example.demo.Import.CategoryImportHelper;
import com.example.demo.Import.CountryImportHelper;
import com.example.demo.Import.FamilyImportHelper;
import com.example.demo.Import.StateHelper;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Category;
import com.example.demo.entity.Country;
import com.example.demo.entity.Family;
import com.example.demo.entity.State;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.FamilyRepository;
import com.example.demo.repository.StateRepository;

@Service
public class ImportService {
	@Autowired
	StateRepository stateRepository;

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	BrandRepository brandRepository;

	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	FamilyRepository familyRepository;

	public void saveState(MultipartFile file) {
		try {
			List<State> states = StateHelper.excelToStates(file.getInputStream());
			stateRepository.saveAll(states);
		} catch (IOException e) {
			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}

	public void saveCountry(MultipartFile file) {
		try {
			List<Country> countries = CountryImportHelper.excelToCountries(file.getInputStream());
			countryRepository.saveAll(countries);
		} catch (IOException e) {
			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}

	public void saveBrand(MultipartFile file) {
		try {
			List<Brand> brands = BrandImportHelper.excelToBrands(file.getInputStream());
			brandRepository.saveAll(brands);
		} catch (IOException e) {
			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}

	public void saveCategory(MultipartFile file) {
		try {
			List<Category> categories = CategoryImportHelper.excelToCategories(file.getInputStream());
			System.out.println(categories.size());
			categoryRepository.saveAll(categories);
		} catch (IOException e) {
			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}
	
	public void saveFamily(MultipartFile file) {
		try {
			List<Family> families = FamilyImportHelper.excelToFamilies(file.getInputStream());
			familyRepository.saveAll(families);
		} catch (IOException e) {
			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}
}
