package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.District;
import com.example.demo.entity.Region;
import com.example.demo.entity.State;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.service.DistrictService;
import com.example.demo.service.RegionService;

@Controller
public class DistrictController {

	private DistrictService districtService;
	
	@Autowired
	private RegionService regionService;
	
	@Autowired
	private DistrictRepository districtRepository;
	
	public DistrictController(DistrictService districtService) {
		super();
		this.districtService = districtService;
	}

	@GetMapping("/district")
	public String listRegion(Model model){
		model.addAttribute("district",districtService.getAllDistrict());
		return "district";
	}
	
	@GetMapping("/district/new")
	public String CreateNewForm(Model model){
		//Create student object to hold student form data
		District district = new District();
		List<Region> region_code = regionService.getAllRegion();
		List<Region> region_name = regionService.getAllRegion();
		model.addAttribute("district",district);
		model.addAttribute("region_code",region_code);
		model.addAttribute("region_name",region_name);
		return "create_district";
	}
	
	@PostMapping("/district")
	public String saveDistrict(@ModelAttribute("district") District district) {
		String rId = district.getRegion_code();
		System.out.println(rId);
		String name = districtRepository.findByRegionName(Long.parseLong(rId));
		System.out.println(name);
		district.setRegion_name(name);
		districtService.saveDistrict(district);
		return "redirect:/district";
	}
	
	@GetMapping("/district/edit/{id}")
	public String editDistrict(@PathVariable Long id, Model model) {
		List<Region> region_code = regionService.getAllRegion();
		List<Region> region_name = regionService.getAllRegion();
		model.addAttribute("region_code",region_code);
		model.addAttribute("region_name",region_name);
		model.addAttribute("district", districtService.getDistrictById(id));
		return "edit_district";
	}
	
	@PostMapping("/district/{id}")
	public String updateDistrict(@PathVariable Long id,
			@ModelAttribute("district") District district,
			Model model) {
		
		//Get Existing District
		District existingDistrict = districtService.getDistrictById(id);
		String dId = district.getRegion_code();
		System.out.println(dId);
		String name = districtRepository.findByRegionName(Long.parseLong(dId));
		System.out.println(name);
		district.setRegion_name(name);
		
		//Save Region
		districtService.editDistrict(district);
		return "redirect:/district";
	}
	
	@GetMapping("/district/{id}")
	public String deleteDistrict(@PathVariable Long id) {
		districtService.deleteDistrictById(id);
		return "redirect:/district";
	}
}

