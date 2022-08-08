package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Area;
import com.example.demo.entity.City;
import com.example.demo.entity.District;
import com.example.demo.repository.AreaRepository;
import com.example.demo.repository.CityRepository;
import com.example.demo.service.AreaService;
import com.example.demo.service.CityService;


@Controller
public class AreaController {

	private AreaService areaService;
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private AreaRepository areaRepository;
	
	public AreaController(AreaService areaService) {
		super();
		this.areaService = areaService;
	}

	@GetMapping("/area")
	public String listArea(Model model){
		model.addAttribute("area",areaService.getAllArea());
		return "area";
	}
	
	@GetMapping("/area/new")
	public String CreateNewForm(Model model){
		//Create student object to hold student form data
		Area area = new Area();
		List<City> city_name = cityService.getAllCity();
		List<City> city_code = cityService.getAllCity();
		model.addAttribute("area",area);
		model.addAttribute("city_name",city_name);
		model.addAttribute("city_code",city_code);
		return "create_area";
	}
	
	@PostMapping("/area")
	public String saveArea(@ModelAttribute("area")Area area) {
		String cyId = area.getCity_code();
		System.out.println(cyId);
		String name = areaRepository.findByCityName(Long.parseLong(cyId));
		System.out.println(name);
		area.setCity_name(name);
		areaService.saveArea(area);
		return "redirect:/area";
	}
	
	@GetMapping("/area/edit/{id}")
	public String editArea(@PathVariable Long id, Model model) {
		List<City> city_code = cityService.getAllCity();
		List<City> city_name = cityService.getAllCity();
		model.addAttribute("city_code",city_code);
		model.addAttribute("city_name",city_name);
		model.addAttribute("area", areaService.getAreaById(id));
		return "edit_area";
	}
	
	@PostMapping("/area/{id}")
	public String updateArea(@PathVariable Long id,
			@ModelAttribute("area") Area area,
			Model model) {
		
		//Get Existing Region
		Area existingArea = areaService.getAreaById(id);
		String aId = area.getCity_code();
		System.out.println(aId);
		String name = areaRepository.findByCityName(Long.parseLong(aId));
		System.out.println(name);
		area.setCity_name(name);
		
		//Save Region
		areaService.editArea(area);
		return "redirect:/area";
	}
	
	@GetMapping("/area/{id}")
	public String deleteArea(@PathVariable Long id) {
		areaService.deleteAreaById(id);
		return "redirect:/area";
	}
}


