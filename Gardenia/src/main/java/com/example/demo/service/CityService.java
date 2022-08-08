package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.City;

public interface CityService {

	List<City> getAllCity();

	City saveCity(City city);

	City getCityById(Long id);

	City editCity(City city);

	void deleteCityById(Long id);

}
