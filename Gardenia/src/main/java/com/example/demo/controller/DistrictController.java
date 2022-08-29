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

import com.example.demo.Export.DistrictExportExcel;
import com.example.demo.Import.DistrictImportHelper;
import com.example.demo.Import.StateHelper;
import com.example.demo.entity.Area;
import com.example.demo.entity.District;
import com.example.demo.entity.Region;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.service.DistrictImportService;
import com.example.demo.service.DistrictService;
import com.example.demo.service.RegionService;

@Controller
public class DistrictController {

	private DistrictService districtService;
	
	@Autowired
	private RegionService regionService;
	
	@Autowired
	private DistrictRepository districtRepository;
	
	@Autowired
	private DistrictImportService districtImportService;
	
	public DistrictController(DistrictService districtService) {
		super();
		this.districtService = districtService;
	}

	@GetMapping("/district/upload")
    public String index() {
        return "districtUploadPage";
    }
	
	@RequestMapping(value="/district/upload/import", method=RequestMethod.POST)
	public String upload(@RequestParam("file")MultipartFile file){
		if(DistrictImportHelper.checkExcelFormat(file)) {
			this.districtImportService.save(file);
			
			return "redirect:/district";
		}
		return "success";
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
	
	@GetMapping("/district/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
		/* DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss"); 
        String currentDateTime = dateFormatter.format(new Date());*/
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=district.xlsx";
        response.setHeader(headerKey, headerValue);
         
        List<District> listUsers = districtService.getAllDistrict();
         
        DistrictExportExcel excelExporter = new DistrictExportExcel(listUsers);
         
        excelExporter.export(response);    
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
	
	@GetMapping("/district/edit/{sid}")
	public String editDistrict(@PathVariable Long sid, Model model) {
		List<Region> did = regionService.getAllRegion();
		List<Region> region_name = regionService.getAllRegion();
		model.addAttribute("regionId",did);
		model.addAttribute("region_name",region_name);
		District district = districtService.getDistrictById(sid);
		String cIdString = district.getRegion_code();
		model.addAttribute("region_code_ID", cIdString);
		model.addAttribute("district", districtService.getDistrictById(sid));
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

