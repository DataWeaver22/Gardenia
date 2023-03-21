package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.exception.ConstraintViolationException;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Export.CityExportExcel;
import com.example.demo.Export.Service.ExportService;
import com.example.demo.Import.CityImportHelper;
import com.example.demo.Import.StateHelper;
import com.example.demo.Import.Service.ImportResponseMessage;
import com.example.demo.entity.City;
import com.example.demo.entity.District;
import com.example.demo.entity.Region;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.DistRepository;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.service.CityImportService;
import com.example.demo.service.CityService;
import com.example.demo.service.DistrictService;

@RestController
@RequestMapping("/city")
public class CityController {

	private CityService cityService;

	@Autowired
	private DistrictService districtService;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private CityImportService cityImportService;

	public CityController(CityService cityService) {
		super();
		this.cityService = cityService;
	}

	@PostMapping("/upload/import")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<ImportResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (CityImportHelper.hasExcelFormat(file)) {
			try {
				cityImportService.save(file);

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

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Map<String, Object>> listCity(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "cityName") String sortBy, @RequestParam(defaultValue = "25") Integer pageSize,
			@RequestParam(required = false) Optional<String> cityCode,
			@RequestParam(required = false) Optional<String> districtName,
			@RequestParam(required = false) Optional<String> cityName,
			@RequestParam(defaultValue = "DESC") String DIR) {
		try {
			List<City> cities = new ArrayList<City>();
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

			Page<City> pageCity;
			pageCity = cityRepository.findByFilterParam(cityCode,cityName,districtName,paging);
			cities = pageCity.getContent();
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("currentPage", page);
			pageContent.put("pageSize", pageCity.getSize());
			pageContent.put("totalPages", pageCity.getTotalPages());
			pageContent.put("totalElements", pageCity.getTotalElements());
			pageContent.put("sortDirection", DIR);
			Map<String, Object> response = new HashMap<>();
			response.put("data", cities);
			response.put("pagination", pageContent);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_USER','ROLE_RSM')")
	public List<Map<String, Object>> dropDownValues(@RequestParam Optional<Long> districtId) {
		// Create student object to hold student form data
		List<City> cities;
		if (districtId.isPresent()) {
			cities = cityRepository.filterByDistrict(districtId);
		} else {
			cities = cityService.getAllCity();
		}
		List<Map<String, Object>> citiesList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < cities.size(); i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", cities.get(i).getCityName());
			pageContent.put("value", cities.get(i).getId());
			citiesList.add(pageContent);
		}
		return citiesList;
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	City saveCity(@RequestBody Map<String, Object> body) {
		City city = new City();
		District district = districtRepository.getById(Long.parseLong(body.get("districtId").toString()));
		city.setDistrict(district);
		city.setCityCode(body.get("cityCode").toString());
		city.setCityName(body.get("cityName").toString());

		return cityService.saveCity(city);
	}

	@Autowired
	ExportService exportService;

	@GetMapping("/export/excel")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Resource> getFile() {
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDate downloadDate = localDateTime.toLocalDate();
		String filename = "City_" + downloadDate + ".xlsx";
		InputStreamResource file = new InputStreamResource(exportService.cityArrayInputStream());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	City updateCity(@PathVariable Long id, @RequestBody Map<String, Object> body) {

		// Get Existing Region
		City existingCity = cityService.getCityById(id);

		// Save Region
		District district = districtRepository.getById(Long.parseLong(body.get("districtId").toString()));
		existingCity.setCityCode(body.get("cityCode").toString());
		existingCity.setCityName(body.get("cityName").toString());
		existingCity.setDistrict(district);

		return cityService.editCity(existingCity);
	}

	@GetMapping("/{id}")
	public String deleteCity(@PathVariable Long id) {
		cityService.deleteCityById(id);
		return "redirect:/city";
	}
}
