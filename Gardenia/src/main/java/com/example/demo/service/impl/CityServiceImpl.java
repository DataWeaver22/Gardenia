package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.City;
import com.example.demo.repository.CityRepository;
import com.example.demo.service.CityService;



@Service
public class CityServiceImpl implements CityService{
	
	private CityRepository cityRepository;

	public CityServiceImpl(CityRepository cityRepository) {
		super();
		this.cityRepository = cityRepository;
	}

	@Override
	public List<City> getAllCity() {
		return cityRepository.findAll();
	}
	
	@Override
	public City saveCity(City city) {
		return cityRepository.save(city);
	}
	
	@Override
	public City getCityById(Long id) {
		return cityRepository.findById(id).get();
	}
	
	@Override
	public City editCity(City city) {
		return cityRepository.save(city);
	}

	@Override
	public void deleteCityById(Long id) {
		// TODO Auto-generated method stub
		cityRepository.deleteById(id);
	}

}
