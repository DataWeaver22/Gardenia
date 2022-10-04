package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.Export.CountryExportExcel;
import com.example.demo.Export.DistributorExportExcel;
import com.example.demo.entity.Country;
import com.example.demo.entity.Distributor;
import com.example.demo.service.CountryService;


@Controller
public class CountryController {
	private CountryService countryService;

	public CountryController(CountryService countryService) {
		super();
		this.countryService = countryService;
	}
	
	@GetMapping("/country")
	public String listStudents(Model model){
		model.addAttribute("country",countryService.getAllCountry());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.print(auth.getAuthorities()); 
		return "country";
	}
	
	@GetMapping("/country/new")
	public String CreateNewForm(Model model){
		//Create student object to hold student form data
		Country country = new Country();
		model.addAttribute("country",country);
		return "create_country";
	}
	
	@GetMapping("/country/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
		/* DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss"); 
        String currentDateTime = dateFormatter.format(new Date());*/
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=country.xlsx";
        response.setHeader(headerKey, headerValue);
         
        List<Country> listUsers = countryService.getAllCountry();
         
        CountryExportExcel excelExporter = new CountryExportExcel(listUsers);
         
        excelExporter.export(response);    
    }  
	
	@PostMapping("/country")
	public String saveCountry(@ModelAttribute("country") Country country) {
		countryService.saveCountry(country);
		return "redirect:/country";
	}
	
	@GetMapping("/country/edit/{id}")
	public String editCountry(@PathVariable Long id, Model model) {
		model.addAttribute("country", countryService.getCountryById(id));
		return "edit_country";
	}
	
	@PostMapping("/country/{id}")
	public String updateCountry(@PathVariable Long id,
			@ModelAttribute("country") Country country,
			Model model) {
		
		//Get Existing Student
		Country existingCountry = countryService.getCountryById(id);
		existingCountry.setCountry_code(country.getCountry_code());
		existingCountry.setCountry_name(country.getCountry_name());
		
		//Save Student
		countryService.editCountry(existingCountry);
		return "redirect:/country";
	}
	
	@GetMapping("/country/{id}")
	public String deleteCountry(@PathVariable Long id) {
		countryService.deleteCountryById(id);
		return "redirect:/country";
	}
}
