package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Id;
import javax.servlet.http.HttpServletResponse;

import org.attoparser.config.ParseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Export.CountryExportExcel;
import com.example.demo.Export.StateExportExcel;
import com.example.demo.Export.Service.ExportService;
import com.example.demo.Import.StateHelper;
import com.example.demo.Import.Service.ImportResponseMessage;
import com.example.demo.Import.Service.ImportService;
import com.example.demo.entity.Country;
import com.example.demo.entity.State;
import com.example.demo.jwt.JwtAuthenticationEntryPoint;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.StateRepository;
import com.example.demo.service.StateService;

@RestController
public class StateController {
	@Autowired
	private StateService stateService;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	ExportService exportService;

	public StateController(StateService stateService) {
		super();
		this.stateService = stateService;
	}

	@RequestMapping(value = "/state", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Map<String, Object>> listState(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "stateName") String sortBy,
			@RequestParam(defaultValue = "25") Integer pageSize, @RequestParam(defaultValue = "DESC") String DIR) {

		try {
			List<State> states = new ArrayList<State>();
			PageRequest pageRequest;
			Pageable paging;
			System.out.println(DIR.equals("DESC"));
			if (DIR.equals("DESC")) {
				System.out.println("DESC");
				pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, sortBy);
				paging = pageRequest;

			} else {
				System.out.println("ASC");
				pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.ASC, sortBy);
				paging = pageRequest;
			}

			Page<State> pageState;
			pageState = stateRepository.findAll(paging);
			states = pageState.getContent();
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("currentPage", page);
			pageContent.put("pageSize", pageState.getSize());
			pageContent.put("totalPages", pageState.getTotalPages());
			pageContent.put("totalElements", pageState.getTotalElements());
			pageContent.put("sortDirection", DIR);
			Map<String, Object> response = new HashMap<>();
			response.put("data", states);
			response.put("pagination", pageContent);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			if (HttpStatus.UNAUTHORIZED.isError()) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

			} else {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}
	}

	@Autowired
	ImportService importService;

	@PostMapping("/state/upload/import")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<ImportResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (StateHelper.hasExcelFormat(file)) {
			try {
				importService.saveState(file);

				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ImportResponseMessage(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				//message = e.fillInStackTrace().toString();
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ImportResponseMessage(message));
			}
		}

		message = "Please upload an excel file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImportResponseMessage(message));
	}

	@RequestMapping(value = "/state/dropdown", method = RequestMethod.GET)
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_USER','ROLE_RSM')")
	public List<Map<String, Object>> dropDownValues(@RequestParam Optional<Long> countryId) {
		// Create student object to hold student form data
		List<State> states;
		if (countryId.isPresent()) {
			states = stateRepository.filterByCountry(countryId);
		} else {
			states = stateService.getAllState();
		}
		List<Map<String, Object>> stateList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < states.size(); i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", states.get(i).getStateName());
			pageContent.put("value", states.get(i).getId());
			stateList.add(pageContent);
		}
		return stateList;
	}

	@RequestMapping(value = "/state/export/excel", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Resource> getFile() {
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDate downloadDate = localDateTime.toLocalDate();
		String filename = "States_" + downloadDate + ".xlsx";
		InputStreamResource file = new InputStreamResource(exportService.stateArrayInputStream());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

	@RequestMapping(value = "/state", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	State saveState(@RequestBody Map<String, Object> body) {
		State state = new State();
		Country country = countryRepository.getById(Long.parseLong(body.get("countryId").toString()));
		state.setCountry(country);
		state.setStateCode(body.get("stateCode").toString());
		state.setStateName(body.get("stateName").toString());

		return stateService.saveState(state);
	}

	@RequestMapping(value = "/state/{id}", method = RequestMethod.PUT)
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	State updateState(@PathVariable Long id, @RequestBody Map<String, Object> body) {

		// Get Existing State
		State existingState = stateService.getStateById(id);

		// Save State

		Country country = countryRepository.getById(Long.parseLong(body.get("countryId").toString()));
		existingState.setStateCode(body.get("stateCode").toString());
		existingState.setStateName(body.get("stateName").toString());
		existingState.setCountry(country);
		return stateService.editState(existingState);
	}

}
