package com.example.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Export.CountryExportExcel;
import com.example.demo.Export.Service.ExportService;
import com.example.demo.Import.CountryImportHelper;
import com.example.demo.Import.StateHelper;
import com.example.demo.Import.Service.ImportResponseMessage;
import com.example.demo.Import.Service.ImportService;
import com.example.demo.entity.Country;
import com.example.demo.entity.State;
import com.example.demo.message.ErrorMessage;
import com.example.demo.repository.CountryRepository;
import com.example.demo.service.CountryService;

@RestController
@RequestMapping("/country")
public class CountryController {

	@Autowired
	private CountryService countryService;

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	ExportService exportService;

	public CountryController(CountryService countryService) {
		super();
		this.countryService = countryService;
	}

	@Autowired
	ImportService importService;

	@PreAuthorize("hasAuthority('ROLE_MIS')")
	@PostMapping("/upload/import")
	public ResponseEntity<ImportResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (CountryImportHelper.hasExcelFormat(file)) {
			try {
				importService.saveCountry(file);

				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ImportResponseMessage(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ImportResponseMessage(message));
			}
		}

		message = "Please upload an excel file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImportResponseMessage(message));
	}

	@PreAuthorize("hasAuthority('ROLE_MIS')")
	@GetMapping
	public ResponseEntity<Map<String, Object>> listStudents(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "25") Integer pageSize,
			@RequestParam(required = false) Optional<String> countryCode,
			@RequestParam(required = false) Optional<String> countryName,
			@RequestParam(defaultValue = "DESC") String DIR) {
		try {
			List<Country> countries = new ArrayList<Country>();
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

			Page<Country> pageCountry;
			pageCountry = countryRepository.findByFilterParam(countryCode,countryName,paging);
			countries = pageCountry.getContent();
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("currentPage", page);
			pageContent.put("pageSize", pageCountry.getSize());
			pageContent.put("totalPages", pageCountry.getTotalPages());
			pageContent.put("totalElements", pageCountry.getTotalElements());
			pageContent.put("sortDirection", DIR);
			Map<String, Object> response = new HashMap<>();
			response.put("data", countries);
			response.put("pagination", pageContent);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_USER','ROLE_RSM')")
	@GetMapping("/dropdown")
	public List<Map<String, Object>> dropDownValues(Model model) {
		// Create student object to hold student form data
		List<Country> countries = countryService.getAllCountry();
		List<Map<String, Object>> countryList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < countries.size(); i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", countries.get(i).getCountryName());
			pageContent.put("value", countries.get(i).getId());
			countryList.add(pageContent);
		}
		return countryList;
	}

	@PreAuthorize("hasAuthority('ROLE_MIS')")
	@GetMapping("/export/excel")
	public ResponseEntity<Resource> getFile() {
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDate downloadDate = localDateTime.toLocalDate();
		String filename = "Countries_" + downloadDate + ".xlsx";
		InputStreamResource file = new InputStreamResource(exportService.countryArrayInputStream());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

	@PreAuthorize("hasAuthority('ROLE_MIS')")
	@PostMapping
	ResponseEntity<?> saveCountry(@RequestBody Country country,HttpServletRequest request) {
		countryService.saveCountry(country);
		String message = "Data Added Successfully";
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, message, "OK", request.getRequestURI()));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	ResponseEntity<?> updateCountry(@PathVariable Long id, @RequestBody Country country, HttpServletRequest request) {

		// Get Existing Student
		Country existingCountry = countryService.getCountryById(id);
		existingCountry.setCountryCode(country.getCountryCode());
		existingCountry.setCountryName(country.getCountryName());

		// Save Student
		countryService.editCountry(existingCountry);
		String message = "Data Edited Successfully";
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, message, "OK", request.getRequestURI())) ;
	}
}
