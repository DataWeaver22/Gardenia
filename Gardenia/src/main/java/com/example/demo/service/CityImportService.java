package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Import.CityImportHelper;
import com.example.demo.entity.City;
import com.example.demo.repository.CityRepository;


@Service
public class CityImportService {
	
	@Autowired
	private CityRepository cityRepository;
	
	public void save(MultipartFile file) {
		try {
			List<City> cities = CityImportHelper.convertToCities(file.getInputStream());
			this.cityRepository.saveAll(cities);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<City> getAllCities(){
		return this.cityRepository.findAll();
	}
}
