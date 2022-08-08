package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.City;
import com.example.demo.entity.District;
import com.example.demo.entity.Region;
import com.example.demo.entity.State;
import com.example.demo.repository.CityRepository;
import com.example.demo.service.CityService;
import com.example.demo.service.DistrictService;

@Controller
public class CityController {

	private CityService cityService;
	
	@Autowired
	private DistrictService districtService;
	
	@Autowired
	private CityRepository cityRepository;
	
	public CityController(CityService cityService) {
		super();
		this.cityService = cityService;
	}

	@GetMapping("/city")
	public String listCity(Model model){
		model.addAttribute("city",cityService.getAllCity());
		return "city";
	}
	
	@GetMapping("/city/new")
	public String CreateNewForm(Model model){
		//Create student object to hold student form data
		City city = new City();
		List<District> district_name = districtService.getAllDistrict();
		List<District> district_id = districtService.getAllDistrict();
		model.addAttribute("city",city);
		model.addAttribute("district_name",district_name);
		model.addAttribute("district_id",district_id);
		return "create_city";
	}
	
	@PostMapping("/city")
	public String saveCity(@ModelAttribute("city")City city) {
		String dId = city.getDistrict_code();
		System.out.println(dId);
		String name = cityRepository.findByDistrictName(Long.parseLong(dId));
		System.out.println(name);
		city.setDistrict_name(name);
		cityService.saveCity(city);
		return "redirect:/city";
	}
	
	@GetMapping("/city/edit/{id}")
	public String editCity(@PathVariable Long id, Model model) {
		List<District> district_code = districtService.getAllDistrict();
		List<District> district_name = districtService.getAllDistrict();
		model.addAttribute("district_code",district_code);
		model.addAttribute("district_name",district_name);
		model.addAttribute("city", cityService.getCityById(id));
		return "edit_city";
	}
	
	@PostMapping("/city/{id}")
	public String updateCity(@PathVariable Long id,
			@ModelAttribute("city") City city,
			Model model) {
		
		//Get Existing Region
		City existingCity = cityService.getCityById(id);
		String ctyId = city.getDistrict_code();
		System.out.println(ctyId);
		String name = cityRepository.findByDistrictName(Long.parseLong(ctyId));
		System.out.println(name);
		city.setDistrict_name(name);
		
		//Save Region
		cityService.editCity(city);
		return "redirect:/city";
	}
	
	@GetMapping("/city/{id}")
	public String deleteCity(@PathVariable Long id) {
		cityService.deleteCityById(id);
		return "redirect:/city";
	}
}


