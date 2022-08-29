package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
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

import com.example.demo.Enum.Roles;
import com.example.demo.Export.DistributorExportExcel;
import com.example.demo.Export.HqExportExcel;
import com.example.demo.Import.HqMasterImportHelper;
import com.example.demo.Import.StateHelper;
import com.example.demo.entity.Distributor;
import com.example.demo.entity.HqMaster;
import com.example.demo.repository.HqRepository;
import com.example.demo.service.HqMasterImportService;
import com.example.demo.service.HqService;

@Controller
public class HqMasterController {
	
	@Autowired
	private HqService hqService;
	
	@Autowired
	private HqMasterImportService hqMasterImportService;

	public HqMasterController(HqService hqService) {
		super();
		this.hqService = hqService;
	}
	
	@GetMapping("/hqmaster/upload")
    public String index() {
        return "hqMasterUploadPage";
    }
	
	@RequestMapping(value="/hqmaster/upload/import", method=RequestMethod.POST)
	public String upload(@RequestParam("file")MultipartFile file){
		if(HqMasterImportHelper.checkExcelFormat(file)) {
			this.hqMasterImportService.save(file);
			
			return "redirect:/hqmaster";
		}
		return "success";
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
	
	 @GetMapping("/hqmaster/export/excel")
	    public void exportToExcel(HttpServletResponse response) throws IOException {
	        response.setContentType("application/octet-stream");
			/* DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss"); 
	        String currentDateTime = dateFormatter.format(new Date());*/
	         
	        String headerKey = "Content-Disposition";
	        String headerValue = "attachment; filename=hqmaster.xlsx";
	        response.setHeader(headerKey, headerValue);
	         
	        List<HqMaster> listUsers = hqService.getAllHq();
	         
	        HqExportExcel excelExporter = new HqExportExcel(listUsers);
	         
	        excelExporter.export(response);    
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
