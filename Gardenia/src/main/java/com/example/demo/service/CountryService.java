package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Country;

public interface CountryService {

	List<Country> getAllCountry();

	Country saveCountry(Country country);

	Country getCountryById(Long id);

	Country editCountry(Country country);
	
	void deleteCountryById(Long id);
	
//	Country createCountry(Country country);
//	
//	Country getAllCountries(int pageNo, int pageSize, String sortBy, String sortDir);

}
