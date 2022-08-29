package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Export.CityExportExcel;
import com.example.demo.Export.ProductExportExcel;
import com.example.demo.Import.CityImportHelper;
import com.example.demo.Import.StateHelper;
import com.example.demo.entity.City;
import com.example.demo.entity.Country;
import com.example.demo.entity.District;
import com.example.demo.entity.Product;
import com.example.demo.entity.Region;
import com.example.demo.entity.State;
import com.example.demo.repository.CityRepository;
import com.example.demo.service.CityImportService;
import com.example.demo.service.CityService;
import com.example.demo.service.DistrictService;

@Controller
public class CityController {

	private CityService cityService;
	
	@Autowired
	private DistrictService districtService;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private CityImportService cityImportService;
	
	public CityController(CityService cityService) {
		super();
		this.cityService = cityService;
	}

	@GetMapping("/city/upload")
    public String index() {
        return "cityUploadPage";
    }
	
	@RequestMapping(value="/city/upload/import", method=RequestMethod.POST)
	public String upload(@RequestParam("file")MultipartFile file){
		if(CityImportHelper.checkExcelFormat(file)) {
			this.cityImportService.save(file);
			
			return "redirect:/city";
		}
		return "success";
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
	
	@GetMapping("/city/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
		/* DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss"); 
        String currentDateTime = dateFormatter.format(new Date());*/
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=city.xlsx";
        response.setHeader(headerKey, headerValue);
         
        List<City> listUsers = cityService.getAllCity();
         
        CityExportExcel excelExporter = new CityExportExcel(listUsers);
         
        excelExporter.export(response);    
    }  
	
	@GetMapping("/city/edit/{sid}")
	public String editCity(@PathVariable Long sid, Model model) {
		List<District> did = districtService.getAllDistrict();
		List<District> district_name = districtService.getAllDistrict();
		model.addAttribute("districtId", did);
		model.addAttribute("district_name",district_name);
		City city = cityService.getCityById(sid);
		String cIdString = city.getDistrict_code();
		model.addAttribute("district_code_ID", cIdString);
		model.addAttribute("city", cityService.getCityById(sid));
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


