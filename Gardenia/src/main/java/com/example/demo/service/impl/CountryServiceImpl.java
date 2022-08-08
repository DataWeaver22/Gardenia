package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Country;
import com.example.demo.repository.CountryRepository;
import com.example.demo.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService{
	
	private CountryRepository countryRepository;

	public CountryServiceImpl(CountryRepository countryRepository) {
		super();
		this.countryRepository = countryRepository;
	}
	
	@Override
	public List<Country> getAllCountry() {
		return countryRepository.findAll();
	}

	@Override
	public Country saveCountry(Country country) {
		return countryRepository.save(country);
	}

	@Override
	public Country getCountryById(Long id) {
		return countryRepository.findById(id).get();
	}

	@Override
	public Country editCountry(Country country) {
		return countryRepository.save(country);
	}

	@Override
	public void deleteCountryById(Long id) {
		// TODO Auto-generated method stub
		countryRepository.deleteById(id);
	}

	
}
