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
import org.springframework.validation.annotation.Validated;
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

import com.example.demo.Export.DistrictExportExcel;
import com.example.demo.Export.Service.ExportService;
import com.example.demo.Import.DistrictImportHelper;
import com.example.demo.Import.StateHelper;
import com.example.demo.Import.Service.ImportResponseMessage;
import com.example.demo.entity.Country;
import com.example.demo.entity.District;
import com.example.demo.entity.Region;
import com.example.demo.entity.State;
import com.example.demo.message.ErrorMessage;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.service.DistrictImportService;
import com.example.demo.service.DistrictService;
import com.example.demo.service.RegionService;

@RestController
@RequestMapping("/district")
public class DistrictController {
	@Autowired
	private DistrictService districtService;

	@Autowired
	private RegionService regionService;

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private DistrictImportService districtImportService;

	public DistrictController(DistrictService districtService) {
		super();
		this.districtService = districtService;
	}

	@PostMapping("/upload/import")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<ImportResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (DistrictImportHelper.hasExcelFormat(file)) {
			try {
				districtImportService.save(file);

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
	public ResponseEntity<Map<String, Object>> listRegion(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "districtName") String sortBy,
			@RequestParam(required = false) Optional<String> districtCode,
			@RequestParam(required = false) Optional<String> districtName,
			@RequestParam(required = false) Optional<String> regionName,
			@RequestParam(defaultValue = "25") Integer pageSize, @RequestParam(defaultValue = "DESC") String DIR) {

		try {
			List<District> districts = new ArrayList<District>();
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

			Page<District> pageDistrict;
			pageDistrict = districtRepository.findByFilterParam(districtCode,regionName,districtName,paging);
			districts = pageDistrict.getContent();
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("currentPage", page);
			pageContent.put("pageSize", pageDistrict.getSize());
			pageContent.put("totalPages", pageDistrict.getTotalPages());
			pageContent.put("totalElements", pageDistrict.getTotalElements());
			pageContent.put("sortDirection", DIR);
			Map<String, Object> response = new HashMap<>();
			response.put("data", districts);
			response.put("pagination", pageContent);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_USER','ROLE_RSM','ROLE_DISTAPPROVER')")
	public List<Map<String, Object>> dropDownValues(@RequestParam Optional<Long> regionId) {
		// Create student object to hold student form data
		List<District> districts;
		if (regionId.isPresent()) {
			districts = districtRepository.filterByRegion(regionId);
		} else {
			districts = districtService.getAllDistrict();
		}
		List<Map<String, Object>> districtList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < districts.size(); i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", districts.get(i).getDistrictName());
			pageContent.put("value", districts.get(i).getId());
			districtList.add(pageContent);
		}
		return districtList;
	}

	@Autowired
	ExportService exportService;

	@GetMapping("/export/excel")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Resource> getFile() {
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDate downloadDate = localDateTime.toLocalDate();
		String filename = "District_" + downloadDate + ".xlsx";
		InputStreamResource file = new InputStreamResource(exportService.districtArrayInputStream());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	ResponseEntity<?> saveDistrict(@RequestBody Map<String, Object> body,HttpServletRequest request) {
		District district = new District();
		Region region = regionRepository.getById(Long.parseLong(body.get("regionId").toString()));
		district.setRegion(region);
		district.setDistrictCode(body.get("districtCode").toString());
		district.setDistrictName(body.get("districtName").toString());

		districtService.saveDistrict(district);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "Data Added Successfully", "OK", request.getRequestURI()));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	ResponseEntity<?> updateDistrict(@PathVariable Long id, @RequestBody Map<String, Object> body,HttpServletRequest request) {

		// Get Existing District
		District existingDistrict = districtService.getDistrictById(id);

		// Save District

		Region region = regionRepository.getById(Long.parseLong(body.get("regionId").toString()));
		existingDistrict.setDistrictCode(body.get("districtCode").toString());
		existingDistrict.setDistrictName(body.get("districtName").toString());
		existingDistrict.setRegion(region);
		districtService.editDistrict(existingDistrict);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "Data Edited Successfully", "OK", request.getRequestURI()));
	}
}
