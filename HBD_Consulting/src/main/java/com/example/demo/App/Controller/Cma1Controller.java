package com.example.demo.App.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.App.Entity.Cma1;
import com.example.demo.App.Service.Cma1Service;

@Controller
public class Cma1Controller {
	private Cma1Service cma1Service;

	public Cma1Controller(Cma1Service cma1Service) {
		super();
		this.cma1Service = cma1Service;
	}
	
	@GetMapping("/cma_one")
	public String listCma1(Model model) {
		model.addAttribute("cma1",cma1Service.getAllCma1());
		return "cma_one";
	}
	
	@GetMapping("/cma_one/new")
	public String CreateNewForm(Model model){
		//Create cma1 object to hold cma1 form data
		Cma1 cma1 = new Cma1();
		model.addAttribute("cma1",cma1);
		return "create_cma_one";
	}
	
	@PostMapping("/cma_one")
	public String saveCma1(@ModelAttribute("cma1") Cma1 cma1) {
		cma1Service.saveCma1(cma1);
		return "redirect:/cma_one";
	}
	
	@GetMapping("/cma_one/edit/{id}")
	public String editCma1(@PathVariable Long id, Model model) {
		model.addAttribute("cma1", cma1Service.getCma1ById(id));
		return "edit_cma_one";
	}
	
	@PostMapping("/cma_one/{id}")
	public String updateCma1(@PathVariable Long id,
			@ModelAttribute("country") Cma1 cma1,
			Model model) {
		
		//Get Existing Student
		Cma1 existingCma1 = cma1Service.getCma1ById(id);
		existingCma1.setName_of_bank(cma1.getName_of_bank());
		existingCma1.setName_of_facility(cma1.getName_of_facility());
		existingCma1.setExisting_limits(cma1.getExisting_limits());
		existingCma1.setExtent_limits(cma1.getExtent_limits());
		existingCma1.setBalance(cma1.getBalance());
		existingCma1.setLimits(cma1.getLimits());
		
		//Save Student
		cma1Service.editCma1(existingCma1);
		return "redirect:/cma_one";
	}
	
	@GetMapping("/cma_one/{id}")
	public String deleteCma1(@PathVariable Long id) {
		cma1Service.deleteCma1ById(id);
		return "redirect:/cma_one";
	}
	
}
