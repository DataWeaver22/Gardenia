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

import com.example.demo.entity.State;
import com.example.demo.repository.DistributorCodeRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.StateRepository;
import com.example.demo.Export.RegionExportExcel;
import com.example.demo.Export.StateExportExcel;
import com.example.demo.entity.Area;
import com.example.demo.entity.City;
import com.example.demo.entity.DistributorCode;
import com.example.demo.entity.Region;
import com.example.demo.service.DistributorCodeService;
import com.example.demo.service.RegionService;
import com.example.demo.service.StateService;

@Controller
public class RegionController {
	String regionCodeString;
	
	
	@Autowired
	private RegionService regionService;
	
	@Autowired
	private RegionRepository regionRepository;
	
	@Autowired
	private StateService stateService;
	
	@Autowired
	private DistributorCodeRepository distributorCodeRepository;
	
	@Autowired
	private DistributorCodeService distributorCodeService;

	public RegionController(RegionService regionService) {
		super();
		this.regionService = regionService;
	}
	
	@GetMapping("/region")
	public String listRegion(Model model){
		model.addAttribute("region",regionService.getAllRegion());
		return "region";
	}
	
	@GetMapping("/region/new")
	public String CreateNewForm(Model model){
		//Create student object to hold student form data
		Region region = new Region();
		List<State> state_code = stateService.getAllState();
		List<State> state_name = stateService.getAllState();
		model.addAttribute("region",region);
		model.addAttribute("state_code",state_code);
		model.addAttribute("state_name",state_name);
		return "create_region";
	}  
	
	@PostMapping("/region")
	public String saveRegion(@ModelAttribute("region") Region region, DistributorCode distributorCode ) {
		
		String sId = region.getState_code();
		System.out.println(sId);
		String name = regionRepository.findByStateName(Long.parseLong(sId));
		System.out.println(name);
		region.setState_name(name);
		regionCodeString = region.getRegion_code();
		String codeNumberInteger = "000";
		distributorCode.setCodeNumber(codeNumberInteger);
		distributorCode.setRegion_code(regionCodeString);
		System.out.println(distributorCode);
		distributorCodeService.saveDistributorCode(distributorCode);
		regionService.saveRegion(region);
		return "redirect:/region";
	}
	
	@GetMapping("/region/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
		/* DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss"); 
        String currentDateTime = dateFormatter.format(new Date());*/
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=region.xlsx";
        response.setHeader(headerKey, headerValue);
         
        List<Region> listUsers = regionService.getAllRegion();
         
        RegionExportExcel excelExporter = new RegionExportExcel(listUsers);
         
        excelExporter.export(response);    
    }  
	
	@GetMapping("/region/edit/{sid}")
	public String editRegion(@PathVariable Long sid, Model model) {
		List<State> did = stateService.getAllState();
		List<State> state_name = stateService.getAllState();
		model.addAttribute("stateId", did);
		model.addAttribute("state_name",state_name);
		Region region = regionService.getRegionById(sid);
		String cIdString = region.getState_code();
		model.addAttribute("state_code_ID", cIdString);
		System.out.println(cIdString);
		model.addAttribute("region", regionService.getRegionById(sid));
		return "edit_region";
	}
	
	@PostMapping("/region/{id}")
	public String updateRegion(@PathVariable Long id,
			@ModelAttribute("region") Region region,
			Model model) {
		
		//Get Existing Region
		Region existingRegion = regionService.getRegionById(id);
		String sId = region.getState_code();
		System.out.println(sId);
		String name = regionRepository.findByStateName(Long.parseLong(sId));
		System.out.println(name);
		region.setState_name(name);
		
		//Save Region
		regionService.editRegion(region);
		return "redirect:/region";
	}
	
	@GetMapping("/region/{id}")
	public String deleteRegion(@PathVariable Long id) {
		regionService.deleteRegionById(id);
		return "redirect:/region";
	}
}

