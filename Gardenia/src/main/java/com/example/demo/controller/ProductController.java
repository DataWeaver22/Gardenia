package com.example.demo.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.util.StringUtils;
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

import com.example.demo.Enum.Status;
import com.example.demo.Enum.Uom;
import com.example.demo.Export.ProductExportExcel;
import com.example.demo.Export.Service.ExportService;
import com.example.demo.Import.FamilyImportHelper;
import com.example.demo.Import.ProductImportHelper;
import com.example.demo.Import.UserImportHelper;
import com.example.demo.Import.Service.ImportResponseMessage;
import com.example.demo.Import.Service.ImportService;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Category;
import com.example.demo.entity.Family;
import com.example.demo.entity.FileDB;
import com.example.demo.entity.Product;
import com.example.demo.entity.Region;
import com.example.demo.message.ResponseMessage;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.FamilyRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.ProductImportService;
import com.example.demo.service.ProductService;

/*
import ch.qos.logback.core.status.Status;
*/
@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	BrandRepository brandRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	FamilyRepository familyRepository;

	@Autowired
	private ProductImportService productImportService;

	public ProductController(ProductService productService) {
		super();
		this.productService = productService;
	}

	@PostMapping("/upload/import")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<ImportResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (ProductImportHelper.hasExcelFormat(file)) {
			try {
				productImportService.save(file);

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
	public ResponseEntity<Map<String, Object>> listProduct(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "updatedDateTime") String sortBy,
			@RequestParam(required = false) Optional<String> productCode,
			@RequestParam(required = false) Optional<String> productName,
			@RequestParam(required = false) Optional<String> brandName,
			@RequestParam(required = false) Optional<String> categoryName,
			@RequestParam(required = false) Optional<String> familyName,
			@RequestParam(required = false) Optional<String> variant,
			@RequestParam(required = false) Optional<String> salesDiaryCode,
			@RequestParam(required = false) Optional<BigDecimal> mrp, @RequestParam(defaultValue = "25") Integer pageSize,
			@RequestParam(defaultValue = "DESC") String DIR,
			@RequestParam(defaultValue = "Approved") Optional<String> productStatus) {

		try {
			List<Product> products = new ArrayList<Product>();
			PageRequest pageRequest;
			Pageable paging;
			if (DIR.equals("DESC")) {
				pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, sortBy);
				paging = pageRequest;

			} else {
				pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.ASC, sortBy);
				paging = pageRequest;
			}

			Page<Product> pageProducts;
			System.out.println(brandName);
			pageProducts = productRepository.findByFilterParam(productCode, productName, brandName, categoryName, familyName,
					variant, salesDiaryCode, mrp, productStatus, paging);
			products = pageProducts.getContent();
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("currentPage", page);
			pageContent.put("pageSize", pageProducts.getSize());
			pageContent.put("totalPages", pageProducts.getTotalPages());
			pageContent.put("totalElements", pageProducts.getTotalElements());
			pageContent.put("sortDirection", DIR);
			Map<String, Object> response = new HashMap<>();
			response.put("data", products);
			response.put("pagination", pageContent);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Autowired
	private FileStorageService storageService;

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	Product saveProduct(@RequestBody Map<String, Object> body) {
		Product product = new Product();
		LocalDateTime createDateTime = LocalDateTime.now();
		LocalDateTime updatedDateTime = LocalDateTime.now();
		product.setCreate_date(createDateTime);
		product.setUpdatedDateTime(updatedDateTime);
		product.setApproval_status("Pending");

		product.setCode(body.get("code").toString());
		product.setPname(body.get("pname").toString());
		Brand brand = brandRepository.getById(Long.parseLong(body.get("brandId").toString()));
		product.setBrand(brand);
		Category category = categoryRepository.getById(Long.parseLong(body.get("categoryId").toString()));
		product.setCategory(category);
		Family family = familyRepository.getById(Long.parseLong(body.get("familyId").toString()));
		product.setFamily(family);
		product.setVariant(body.get("variant").toString());
		product.setGroup_name(body.get("group_name").toString());
		product.setUom(body.get("uom").toString());
		product.setPtd(new BigDecimal(body.get("ptd").toString()));
		product.setPtr(new BigDecimal(body.get("ptr").toString()));
		product.setStatus(body.get("status").toString());
		product.setDescription(body.get("description").toString());
		product.setSalesDiaryCode(body.get("salesDiaryCode").toString());
		product.setMrp(new BigDecimal(body.get("mrp").toString()));
		return productService.saveProduct(product);
	}

	@Autowired
	ExportService exportService;

	@GetMapping("/export/excel")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Resource> getFile() {
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDate downloadDate = localDateTime.toLocalDate();
		String filename = "Products_" + downloadDate + ".xlsx";
		InputStreamResource file = new InputStreamResource(exportService.productArrayInputStream());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	Product updateProduct(@PathVariable Long id, @RequestBody Map<String, Object> body) {

		// Get Existing Student
		Product existingProduct = productService.getProduct(id);
		LocalDateTime updatedDateTime = LocalDateTime.now();
		existingProduct.setUpdatedDateTime(updatedDateTime);
		if (body.get("status").toString().equals("Inactive")) {
			System.out.println(body.get("status").toString());
			existingProduct.setInactive_date(updatedDateTime);
		}

		existingProduct.setCode(body.get("code").toString());
		existingProduct.setPname(body.get("pname").toString());
		Brand brand = brandRepository.getById(Long.parseLong(body.get("brandId").toString()));
		existingProduct.setBrand(brand);
		Category category = categoryRepository.getById(Long.parseLong(body.get("categoryId").toString()));
		existingProduct.setCategory(category);
		Family family = familyRepository.getById(Long.parseLong(body.get("familyId").toString()));
		existingProduct.setFamily(family);
		existingProduct.setVariant(body.get("variant").toString());
		existingProduct.setGroup_name(body.get("group_name").toString());
		existingProduct.setUom(body.get("uom").toString());
		existingProduct.setPtd(new BigDecimal(body.get("ptd").toString()));
		existingProduct.setPtr(new BigDecimal(body.get("ptr").toString()));
		existingProduct.setStatus(body.get("status").toString());
		existingProduct.setDescription(body.get("description").toString());
		existingProduct.setSalesDiaryCode(body.get("salesDiaryCode").toString());
		existingProduct.setMrp(new BigDecimal(body.get("mrp").toString()));

		// Save Student

		return productService.editProduct(existingProduct);
	}

	@GetMapping("/approve/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public String approveProduct(@PathVariable Long id) {
		Long pID = id;
		System.out.println(pID);
		String approved = "Approved";
		productRepository.updateByApprovedStatus(approved, pID);
		return "Approved";
	}

	@PostMapping("/reject/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public String rejectProduct(@PathVariable Long id,@RequestBody Map<String, Object> body) {
		Long pID = id;
		System.out.println(pID);
		String rejectReason = body.get("rejectReason").toString();
		String approved = "Rejected";
		productRepository.updateByStatus(approved, rejectReason, pID);
		return "Rejected";
	}

}
