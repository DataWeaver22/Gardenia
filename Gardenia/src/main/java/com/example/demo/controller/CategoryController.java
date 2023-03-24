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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Export.Service.ExportService;
import com.example.demo.Import.BrandImportHelper;
import com.example.demo.Import.CategoryImportHelper;
import com.example.demo.Import.Service.ImportResponseMessage;
import com.example.demo.Import.Service.ImportService;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Category;
import com.example.demo.message.ErrorMessage;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.BrandService;
import com.example.demo.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	public CategoryController(CategoryService categoryService) {
		super();
		this.categoryService = categoryService;
		// TODO Auto-generated constructor stub
	}
	
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	@GetMapping
	public ResponseEntity<Map<String, Object>> listBrand(
			@RequestParam(defaultValue = "1")Integer page,
			@RequestParam(defaultValue = "updatedDateTime")String sortBy, 
			@RequestParam(defaultValue = "25")Integer pageSize,
			@RequestParam(required = false) Optional<String> brandName,
			@RequestParam(required = false) Optional<String> categoryName,
			@RequestParam(defaultValue = "DESC") String DIR){
		
		try {
			List<Category> categories = new ArrayList<Category>();
			PageRequest pageRequest;
			Pageable paging;
			System.out.println(DIR.equals("DESC"));
			if(DIR.equals("DESC"))
			{
				System.out.println("DESC");
				pageRequest = PageRequest.of(page -1, pageSize,Sort.Direction.DESC,sortBy);
				paging = pageRequest;
				
			}else {
				System.out.println("ASC");
				pageRequest = PageRequest.of(page -1, pageSize,Sort.Direction.ASC,sortBy);
				paging = pageRequest;
			}
		      
		      
		      Page<Category> pageCategories;
		      pageCategories = categoryRepository.findByFilterParam(brandName,categoryName,paging);
		      categories = pageCategories.getContent();
		      Map<String, Object> pageContent = new HashMap<>();
		      pageContent.put("currentPage", page);
		      pageContent.put("pageSize", pageCategories.getSize());
		      pageContent.put("totalPages", pageCategories.getTotalPages());
		      pageContent.put("totalElements", pageCategories.getTotalElements());
		      pageContent.put("sortDirection", DIR);
		      Map<String, Object> response = new HashMap<>();
		      response.put("data", categories);
		      response.put("pagination", pageContent);
		      
			return new ResponseEntity<>(response, HttpStatus.OK);
			
		} catch (Exception e) {
		      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@GetMapping("/dropdown")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public List<Map<String, Object>> dropDownValues(@RequestParam Optional<Long> brandId) {
		List<Category> categories;
		if(brandId.isPresent()) {
			categories = categoryRepository.filterByBrands(brandId);
		}else {
			categories= categoryService.getAllCategories();
		}
		
		List<Map<String, Object>> categoriesList = new ArrayList<Map<String, Object>>();
		for(int i=0;i<categories.size();i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", categories.get(i).getCategoryName());
			pageContent.put("value", categories.get(i).getId());
			categoriesList.add(pageContent);
		}
		return categoriesList;
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	ResponseEntity<?> saveCategory(@RequestBody Map<String,Object> body, HttpServletRequest request) {
		Brand brand = new Brand();
		Category category = new Category();
		category.setCategoryName(body.get("categoryName").toString());
		brand = brandRepository.getById(Long.parseLong(body.get("brandId").toString()));
		category.setBrand(brand);
		LocalDateTime updatedDateTime = LocalDateTime.now();
		category.setUpdatedDateTime(updatedDateTime);
		categoryService.saveCategory(category);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "Data Added Successfully", "OK", request.getRequestURI()));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Map<String,Object> body, HttpServletRequest request) {

		// Get Existing State
		Category existingCategory = categoryService.getCategoryById(id);
		
		// Save State
		Brand brand = brandRepository.getById(Long.parseLong(body.get("brandId").toString()));
		LocalDateTime updatedDateTime = LocalDateTime.now();
		existingCategory.setCategoryName(body.get("categoryName").toString());
		existingCategory.setUpdatedDateTime(updatedDateTime);
		existingCategory.setBrand(brand);
		categoryService.editCategory(existingCategory);
		
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
	    String filename = "Category_"+downloadDate+".xlsx";
	    InputStreamResource file = new InputStreamResource(exportService.categoryInputStream());

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
	        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	        .body(file);
	  }  
	
	@Autowired
	private ImportService importService;
	
	@PostMapping(value = "/upload/import",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} )
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<ImportResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		if (CategoryImportHelper.hasExcelFormat(file)) {
			try {
				importService.saveCategory(file);

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
