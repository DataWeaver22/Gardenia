package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Enum.Roles;
import com.example.demo.entity.HqMaster;
import com.example.demo.service.HqService;

@Controller
public class HqMasterController {
	
	@Autowired
	private HqService hqService;

	public HqMasterController(HqService hqService) {
		super();
		this.hqService = hqService;
	}
	
	@GetMapping("/hqmaster")
	public String listHq(Model model){
		Roles roles;
		model.addAttribute("hqmaster",hqService.getAllHq());
		return "hqmaster";
	}
	
	@GetMapping("/hqmaster/new")
	public String CreateNewForm(Model model){
		//Create student object to hold student form data
		Roles roles;
		HqMaster hqmaster = new HqMaster();
		model.addAttribute("hqmaster",hqmaster);
		return "create_hqmaster";
	}
	
	@PostMapping("/hqmaster")
	public String saveHqMaster(@ModelAttribute("hqmaster") HqMaster hqmaster) {
		String hq = hqmaster.getHq_designation();
		System.out.println(hq);
		hqService.saveHqMaster(hqmaster);
		return "redirect:/hqmaster";
	}

	 @RequestMapping("/hqmaster")
	    public String viewHqPage(Model model, @Param("keyword") String keyword) {
	        List<HqMaster> listProducts = hqService.listAll(keyword);
	        model.addAttribute("listProducts", listProducts);
	        model.addAttribute("keyword", keyword);
	        System.out.println(keyword);
	        return "hqmaster";
	    }
	
	
	@GetMapping("/hqmaster/edit/{id}")
	public String editHqMaster(@PathVariable Long id, Model model) {
		model.addAttribute("hqmaster", hqService.getHqMaster(id));
		return "edit_hqmaster";
	}
	
	@ModelAttribute("/hqmaster") 
	public String populateList(Model model) {
	    List<String> options = new ArrayList<String>();
	    options.add("Territory Sales Officer");
	    options.add("Area Sales Executive");
	    options.add("Area Sales Manager");
	    options.add("Regional Sales Manager");
	    model.addAttribute("options", options);
	    return "hqmaster";
	}
	
	@PostMapping("/hqmaster/{id}")
	public String updateHqMaster(@PathVariable Long id,
			@ModelAttribute("hqmaster") HqMaster hqmaster,
			Model model) {
		
		//Get Existing HqMaster
		HqMaster existingHqMaster = hqService.getHqMaster(id);
		existingHqMaster.setHq_code(hqmaster.getHq_code());
		existingHqMaster.setHq_name(hqmaster.getHq_name());
		existingHqMaster.setHq_designation(hqmaster.getHq_designation());
		
		//Save HqMaster
		hqService.editHqMaster(existingHqMaster);
		return "redirect:/hqmaster";
	}
	
	@GetMapping("/hqmaster/{id}")
	public String deleteHqMaster(@PathVariable Long id) {
		hqService.deleteHqMasterById(id);
		return "redirect:/hqmaster";
	}
}
