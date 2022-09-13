package com.example.demo.App.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.App.Entity.CmaTwo;
import com.example.demo.App.Service.CmaTwoService;

@Controller
public class CmaTwoController {
	private CmaTwoService cmaTwoService;

	public CmaTwoController(CmaTwoService cmaTwoService) {
		super();
		this.cmaTwoService = cmaTwoService;
	}
	
	@GetMapping("/cmatwo")
	public String listCma1(Model model) {
		model.addAttribute("cmatwo",cmaTwoService.getAllCmaTwo());
		return "cmatwo";
	}
	
	@GetMapping("/cmatwo/new")
	public String CreateNewForm(Model model){
		//Create cma1 object to hold cma1 form data
		CmaTwo cmaTwo = new CmaTwo();
		model.addAttribute("cmaTwo",cmaTwo);
		return "create_cmatwo";
	}
	
	@PostMapping("/cmatwo")
	public String saveCma1(@ModelAttribute("cmaTwo") CmaTwo cmaTwo) {
		cmaTwoService.saveCmaTwo(cmaTwo);
		return "redirect:/cmatwo";
	}
	
	@GetMapping("/cmatwo/edit/{id}")
	public String editCma1(@PathVariable Long id, Model model) {
		model.addAttribute("cmaTwo", cmaTwoService.getCmaTwoById(id));
		return "edit_cmatwo";
	}
	
	@PostMapping("/cmatwo/{id}")
	public String updateCma1(@PathVariable Long id,
			@ModelAttribute("country") CmaTwo cmaTwo,
			Model model) {
		
		//Get Existing Student
		CmaTwo existingCmaTwo = cmaTwoService.getCmaTwoById(id);
		existingCmaTwo.setCma_one_id(cmaTwo.getCma_one_id());
		existingCmaTwo.setDomestic_sales_commission_brokerage(cmaTwo.getDomestic_sales_commission_brokerage());
		existingCmaTwo.setExport_sales(cmaTwo.getExport_sales());
		existingCmaTwo.setOther_operating_income(cmaTwo.getOther_operating_income());
		existingCmaTwo.setExcise_duty(cmaTwo.getExcise_duty());
		existingCmaTwo.setRaw_materials_imported(cmaTwo.getRaw_materials_imported());
		existingCmaTwo.setRaw_materials_indigeneous(cmaTwo.getRaw_materials_indigeneous());
		existingCmaTwo.setOther_spares_imported(cmaTwo.getOther_spares_imported());
		existingCmaTwo.setOther_spares_indigeneous(cmaTwo.getOther_spares_indigeneous());
		existingCmaTwo.setPowerFuel(cmaTwo.getPowerFuel());
		existingCmaTwo.setDirectLabour(cmaTwo.getDirectLabour());
		existingCmaTwo.setOtherMfgExpenses(cmaTwo.getOtherMfgExpenses());
		existingCmaTwo.setRepairsAndMaintenance(cmaTwo.getRepairsAndMaintenance());
		existingCmaTwo.setDepreciation(cmaTwo.getDepreciation());
		existingCmaTwo.setOpeningStockInProcess(cmaTwo.getOpeningStockInProcess());
		existingCmaTwo.setClosingStockInProcess(cmaTwo.getClosingStockInProcess());
		existingCmaTwo.setOpeningStockOfFinishedGoods(cmaTwo.getClosingStockOfFinishedGoods());
		existingCmaTwo.setClosingStockOfFinishedGoods(cmaTwo.getClosingStockOfFinishedGoods());
		
		//Save Student
		cmaTwoService.editCmaTwo(existingCmaTwo);
		return "redirect:/cmatwo";
	}
	
	@GetMapping("/cmatwo{id}")
	public String deleteCmaTwo(@PathVariable Long id) {
		cmaTwoService.deleteCmaTwoById(id);
		return "redirect:/cmatwo";
	}
	
}
