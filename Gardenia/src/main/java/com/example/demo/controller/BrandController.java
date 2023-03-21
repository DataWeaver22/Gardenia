package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.example.demo.Import.AreaImportHelper;
import com.example.demo.Import.BrandImportHelper;
import com.example.demo.Import.Service.ImportResponseMessage;
import com.example.demo.Import.Service.ImportService;
import com.example.demo.entity.Brand;
import com.example.demo.repository.BrandRepository;
import com.example.demo.service.BrandService;

@RestController
@RequestMapping("/brand")
public class BrandController {

	@Autowired
	private BrandService brandService;
	
	@Autowired
	private BrandRepository brandRepository;
	
	public BrandController(BrandService brandService) {
		super();
		this.brandService = brandService;
		// TODO Auto-generated constructor stub
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Map<String, Object>> listBrand(
			@RequestParam(defaultValue = "1")Integer page,
			@RequestParam(defaultValue = "updatedDateTime")String sortBy, 
			@RequestParam(defaultValue = "25")Integer pageSize,
			@RequestParam(required = false) Optional<String> brandName,
			@RequestParam(defaultValue = "DESC") String DIR){
		
		try {
			List<Brand> brands = new ArrayList<Brand>();
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
		      
		      
		      Page<Brand> pageBrands;
		      pageBrands = brandRepository.findByFilterParam(brandName,paging);
		      brands = pageBrands.getContent();
		      Map<String, Object> pageContent = new HashMap<>();
		      pageContent.put("currentPage", page);
		      pageContent.put("pageSize", pageBrands.getSize());
		      pageContent.put("totalPages", pageBrands.getTotalPages());
		      pageContent.put("totalElements", pageBrands.getTotalElements());
		      pageContent.put("sortDirection", DIR);
		      Map<String, Object> response = new HashMap<>();
		      response.put("data", brands);
		      response.put("pagination", pageContent);
		      
			return new ResponseEntity<>(response, HttpStatus.OK);
			
		} catch (Exception e) {
		      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@GetMapping("/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_RSM')")
	public List<Map<String, Object>> dropDownValues() {
		// Create student object to hold student form data
		List<Brand> brands= brandService.getAllBrands();
		List<Map<String, Object>> brandList = new ArrayList<Map<String, Object>>();
		for(int i=0;i<brands.size();i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", brands.get(i).getBrandName());
			pageContent.put("value", brands.get(i).getId());
			brandList.add(pageContent);
		}
		return brandList;
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	Brand saveBrand(@RequestBody Map<String,Object> body) {
		Brand brand = new Brand();
		brand.setBrandName(body.get("brandName").toString());
		LocalDateTime updatedDateTime = LocalDateTime.now();
		brand.setUpdatedDateTime(updatedDateTime);
		return brandService.saveBrand(brand);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	Brand updateBrand(@PathVariable Long id, @RequestBody Map<String,Object> body) {

		// Get Existing State
		Brand existingBrand = brandService.getBrandById(id);
		
		// Save State
		LocalDateTime updatedDateTime = LocalDateTime.now();
		existingBrand.setBrandName(body.get("brandName").toString());
		existingBrand.setUpdatedDateTime(updatedDateTime);
		return brandService.editBrand(existingBrand);
	}
	
	@Autowired
	ExportService exportService;
	
	@GetMapping("/export/excel")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Resource> getFile() {
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDate downloadDate = localDateTime.toLocalDate();
	    String filename = "Brand_"+downloadDate+".xlsx";
	    InputStreamResource file = new InputStreamResource(exportService.brandInputStream());

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
	        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	        .body(file);
	  }  
	
	@Autowired
	ImportService importService;
	
	@PostMapping("/upload/import")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<ImportResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,@RequestHeader Map<String,String> headers) {
		String message = "";
		headers.forEach((key,value) ->{
            System.out.println("Header Name: "+key+" Header Value: "+value);
        });
		if (BrandImportHelper.hasExcelFormat(file)) {
			try {
				importService.saveBrand(file);

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
