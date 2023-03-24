package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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

import com.example.demo.Export.Service.ExportService;
import com.example.demo.Import.CategoryImportHelper;
import com.example.demo.Import.FamilyImportHelper;
import com.example.demo.Import.Service.ImportResponseMessage;
import com.example.demo.Import.Service.ImportService;
import com.example.demo.entity.Category;
import com.example.demo.entity.Family;
import com.example.demo.entity.UserTargetDetails;
import com.example.demo.entity.UserTeam;
import com.example.demo.message.ErrorMessage;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.FamilyRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.FamilyService;

@RestController
@RequestMapping("/family")
public class FamilyController {

	@Autowired
	private FamilyService familyService;

	@Autowired
	private FamilyRepository familyRepository;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CategoryRepository categoryRepository;

	public FamilyController(FamilyService familyService) {
		super();
		this.familyService = familyService;
		// TODO Auto-generated constructor stub
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Map<String, Object>> listBrand(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "updatedDateTime") String sortBy,
			@RequestParam(required = false) Optional<String> familyName,
			@RequestParam(required = false) Optional<String> categoryName,
			@RequestParam(defaultValue = "25") Integer pageSize, @RequestParam(defaultValue = "DESC") String DIR) {

		try {
			List<Family> families = new ArrayList<Family>();
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

			Page<Family> pageFamily;
			pageFamily = familyRepository.findByFilterParam(familyName,categoryName,paging);
			families = pageFamily.getContent();
			
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("currentPage", page);
			pageContent.put("pageSize", pageFamily.getSize());
			pageContent.put("totalPages", pageFamily.getTotalPages());
			pageContent.put("totalElements", pageFamily.getTotalElements());
			pageContent.put("sortDirection", DIR);
			Map<String, Object> response = new HashMap<>();
			response.put("data", families);
			response.put("pagination", pageContent);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/dropdown")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public List<Map<String, Object>> dropDownValues(@RequestParam Optional<Long> categoryId) {
		// Create student object to hold student form data
		List<Family> families;
		if (categoryId.isPresent()) {
			families = familyRepository.filterByCategories(categoryId);
		} else {
			families = familyService.getAllFamilies();
		}

		List<Map<String, Object>> familiesList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < families.size(); i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", families.get(i).getFamilyName());
			pageContent.put("value", families.get(i).getId());
			familiesList.add(pageContent);
		}
		return familiesList;
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	ResponseEntity<?> saveFamily(@RequestBody Map<String, Object> body,HttpServletRequest request) {
		Family family = new Family();
		Category category = new Category();
		family.setFamilyName(body.get("familyName").toString());
		category = categoryRepository.getById(Long.parseLong(body.get("categoryId").toString()));
		family.setCategory(category);
		LocalDateTime updatedDateTime = LocalDateTime.now();
		family.setUpdatedDateTime(updatedDateTime);
		familyService.saveFamily(family);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "Data Added Successfully", "OK", request.getRequestURI()));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	ResponseEntity<?> updateFamily(@PathVariable Long id, @RequestBody Map<String, Object> body,HttpServletRequest request) {

		// Get Existing State
		Family existingFamily = familyService.getFamilyById(id);

		// Save State
		Category category = categoryRepository.getById(Long.parseLong(body.get("categoryId").toString()));
		LocalDateTime updatedDateTime = LocalDateTime.now();
		existingFamily.setFamilyName(body.get("familyName").toString());
		existingFamily.setUpdatedDateTime(updatedDateTime);
		existingFamily.setCategory(category);
		familyService.editFamily(existingFamily);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "Data Edited Successfully", "OK", request.getRequestURI()));
	}

	@Autowired
	ExportService exportService;

	@GetMapping("/export/excel")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Resource> getFile() {
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDate downloadDate = localDateTime.toLocalDate();
		String filename = "Family_" + downloadDate + ".xlsx";
		InputStreamResource file = new InputStreamResource(exportService.familyArrayInputStream());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

	@Autowired
	ImportService importService;

	@PostMapping("/upload/import")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<ImportResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (FamilyImportHelper.hasExcelFormat(file)) {
			try {
				importService.saveFamily(file);

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
}
