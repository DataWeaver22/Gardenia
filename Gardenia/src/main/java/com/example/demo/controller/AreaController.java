package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Export.AreaExportExcel;
import com.example.demo.Export.CityExportExcel;
import com.example.demo.Export.Service.ExportService;
import com.example.demo.Import.AreaImportHelper;
import com.example.demo.Import.StateHelper;
import com.example.demo.Import.Service.ImportResponseMessage;
import com.example.demo.entity.Area;
import com.example.demo.entity.City;
import com.example.demo.entity.District;
import com.example.demo.message.ErrorMessage;
import com.example.demo.repository.AreaRepository;
import com.example.demo.repository.CityRepository;
import com.example.demo.service.AreaImportService;
import com.example.demo.service.AreaService;
import com.example.demo.service.CityService;

@RestController
@RequestMapping("/area")
public class AreaController {

	private AreaService areaService;

	@Autowired
	private CityService cityService;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private AreaImportService areaImportService;

	public AreaController(AreaService areaService) {
		super();
		this.areaService = areaService;
	}

	@PostMapping("/upload/import")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<ImportResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (AreaImportHelper.hasExcelFormat(file)) {
			try {
				areaImportService.save(file);

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
	public ResponseEntity<Map<String, Object>> listArea(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "areaName") String sortBy, @RequestParam(defaultValue = "25") Integer pageSize,
			@RequestParam(required = false) Optional<String> areaCode,
			@RequestParam(required = false) Optional<String> areaName,
			@RequestParam(required = false) Optional<String> cityName,
			@RequestParam(defaultValue = "DESC") String DIR) {
		try {
			List<Area> areas = new ArrayList<Area>();
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

			Page<Area> pageArea;
			pageArea = areaRepository.findByFilterParam(areaCode,cityName,areaName,paging);
			areas = pageArea.getContent();
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("currentPage", page);
			pageContent.put("pageSize", pageArea.getSize());
			pageContent.put("totalPages", pageArea.getTotalPages());
			pageContent.put("totalElements", pageArea.getTotalElements());
			pageContent.put("sortDirection", DIR);
			Map<String, Object> response = new HashMap<>();
			response.put("data", areas);
			response.put("pagination", pageContent);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_USER','ROLE_RSM')")
	public List<Map<String, Object>> dropDownValues(@RequestParam Optional<Long> cityId) {
		// Create student object to hold student form data
		List<Area> areas;
		if (cityId.isPresent()) {
			areas = areaRepository.filterByCity(cityId);
		} else {
			areas = areaService.getAllArea();
		}
		List<Map<String, Object>> areasList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < areas.size(); i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", areas.get(i).getAreaName());
			pageContent.put("value", areas.get(i).getId());
			areasList.add(pageContent);
		}
		return areasList;
	}

	@Autowired
	ExportService exportService;

	@GetMapping("/export/excel")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Resource> getFile() {
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDate downloadDate = localDateTime.toLocalDate();
		String filename = "Area_" + downloadDate + ".xlsx";
		InputStreamResource file = new InputStreamResource(exportService.areaArrayInputStream());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	ResponseEntity<?> saveArea(@RequestBody Map<String, Object> body,HttpServletRequest request) {
		Area area = new Area();
		City city = cityRepository.getById(Long.parseLong(body.get("cityId").toString()));
		area.setCity(city);
		area.setAreaCode(body.get("areaCode").toString());
		area.setAreaName(body.get("areaName").toString());

		areaService.saveArea(area);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "Data Added Successfully", "OK", request.getRequestURI()));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	ResponseEntity<?> updateArea(@PathVariable Long id, @RequestBody Map<String, Object> body, HttpServletRequest request) {

		// Get Existing Region
		Area existingArea = areaService.getAreaById(id);

		// Save Region
		City city = cityRepository.getById(Long.parseLong(body.get("cityId").toString()));
		existingArea.setAreaCode(body.get("areaCode").toString());
		existingArea.setAreaName(body.get("areaName").toString());
		existingArea.setCity(city);

		areaService.editArea(existingArea);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "Data Edited Successfully", "OK", request.getRequestURI()));
	}

}
