package com.example.demo.controller;

import java.util.List;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Country;
import com.example.demo.entity.State;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.StateRepository;
import com.example.demo.service.CountryService;
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

	public StateController(StateService stateService) {
		super();
		this.stateService = stateService;
	}

	@GetMapping("/state")
	public String listState(Model model) {
		model.addAttribute("state", stateService.getAllState());
		return "state";
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

	
	/*
	 * @PostMapping("/state/{id}") public ResponseEntity<State>
	 * createState(@PathVariable(value = "id") Long id,
	 * 
	 * @RequestBody State stateRequest) { State state =
	 * countryRepository.findById(id).map(country -> {
	 * stateRequest.setCountry(country); return stateRepository.save(stateRequest);
	 * }).orElseThrow(() -> new
	 * ResourceNotFoundException("Not found Tutorial with id = " + id)); return new
	 * ResponseEntity<>(state, HttpStatus.CREATED); }
	 */
	 	
	/*
	 * @GetMapping("/{id}/state") public State createState(@RequestBody State state)
	 * { return stateRepository.save(state); }
	 */

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
