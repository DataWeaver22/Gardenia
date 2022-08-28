package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Export.CountryExportExcel;
import com.example.demo.Export.StateExportExcel;
import com.example.demo.Import.StateHelper;
import com.example.demo.entity.Country;
import com.example.demo.entity.State;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.StateRepository;
import com.example.demo.service.CountryService;
import com.example.demo.service.IFileUploaderService;
import com.example.demo.service.StateImportService;
import com.example.demo.service.StateService;


@Controller
public class StateController {
	private StateService stateService;
	
	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private CountryService countryService;
	
	@Autowired
	private StateImportService stateImportService;
	
	public StateController(StateService stateService) {
		super();
		this.stateService = stateService;
	}
	
	@GetMapping("/state")
	public String listState(Model model) {
		model.addAttribute("state", stateService.getAllState());
		return "state";
	}

	@GetMapping("/state/upload")
    public String index() {
        return "uploadPage";
    }
	
	@RequestMapping(value="/state/upload/import", method=RequestMethod.POST)
	public String upload(@RequestParam("file")MultipartFile file){
		if(StateHelper.checkExcelFormat(file)) {
			this.stateImportService.save(file);
			
			return "redirect:/state";
		}
		return "success";
	}
	
	@GetMapping("/state/new")
	public String CreateNewForm(Model model) {
		// Create student object to hold student form data
		State state = new State();
		List<Country> id = countryService.getAllCountry();
		List<Country> country_name = countryService.getAllCountry();
		model.addAttribute("state", state);
		model.addAttribute("id", id);
		model.addAttribute("country_name", country_name);
		return "create_state";
	}
	
	@GetMapping("/state/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
		/* DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss"); 
        String currentDateTime = dateFormatter.format(new Date());*/
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=state.xlsx";
        response.setHeader(headerKey, headerValue);
         
        List<State> listUsers = stateService.getAllState();
         
        StateExportExcel excelExporter = new StateExportExcel(listUsers);
         
        excelExporter.export(response);    
    }
	
	
	@PostMapping("/state")
	public String saveState(@ModelAttribute("state") State state) {
		String cId = state.getCountry_code();
		System.out.println(cId);
		String name = stateRepository.findByCountryName(Long.parseLong(cId));
		System.out.println(name);
		state.setCountry_name(name);
		stateService.saveState(state);
		return "redirect:/state";
	}
	
	
	@GetMapping("/state/edit/{sId}")
	public String editState(@PathVariable Long sId, Model model) {
		List<Country> id = countryService.getAllCountry();
		List<Country> country_name = countryService.getAllCountry();
		model.addAttribute("countryId", id);
		model.addAttribute("country", country_name);
		State state = stateService.getStateById(sId);
		String cIdString = state.getCountry_code();
		model.addAttribute("country_code_ID", cIdString);
		model.addAttribute("state", stateService.getStateById(sId));
		System.out.println(stateService.getStateById(sId));
		
		return "edit_state";
	}

	@PostMapping("/state/{id}")
	public String updateState(@PathVariable Long id, @ModelAttribute("state") State state, Model model) {

		// Get Existing State
		State existingState = stateService.getStateById(id);
		String cId = state.getCountry_code();
		System.out.println(cId);
		String name = stateRepository.findByCountryName(Long.parseLong(cId));
		System.out.println(name);
		state.setCountry_name(name);

		// Save State
		stateService.editState(state);
		return "redirect:/state";
	}

	@GetMapping("/state/{id}")
	public String deleteState(@PathVariable Long id) {
		stateService.deleteStateById(id);
		return "redirect:/state";
	}
}
