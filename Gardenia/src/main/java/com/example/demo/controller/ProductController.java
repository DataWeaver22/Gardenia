package com.example.demo.controller;

import java.io.ByteArrayOutputStream;
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

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
import org.springframework.web.bind.annotation.RequestPart;
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
import com.example.demo.entity.Country;
import com.example.demo.entity.Distributor;
import com.example.demo.entity.Family;
import com.example.demo.entity.FileDB;
import com.example.demo.entity.Product;
import com.example.demo.entity.Region;
import com.example.demo.message.ErrorMessage;
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

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "Id", "Brand", "Category", "Family", "Variant", "Product Name", "Product Code",
			"Group Name", "UOM", "Description", "PTD", "PTR", "MRP" };
	static String SHEET = "Sheet1";

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

	@Autowired
	private JavaMailSender javaMailSender;

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
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_PRODUCTAPPROVER','ROLE_PRODUCT')")
	public ResponseEntity<Map<String, Object>> listProduct(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "updatedDateTime") String sortBy,
			@RequestParam(required = false) Optional<String> productCode,
			@RequestParam(required = false) Optional<String> productName,
			@RequestParam(required = false) Optional<String> brandName,
			@RequestParam(required = false) Optional<String> categoryName,
			@RequestParam(required = false) Optional<String> familyName,
			@RequestParam(required = false) Optional<String> variant,
			@RequestParam(required = false) Optional<String> salesDiaryCode,
			@RequestParam(required = false) Optional<BigDecimal> mrp,
			@RequestParam(defaultValue = "25") Integer pageSize, @RequestParam(defaultValue = "DESC") String DIR,
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
			pageProducts = productRepository.findByFilterParam(productCode, productName, brandName, categoryName,
					familyName, variant, salesDiaryCode, mrp, productStatus, paging);
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
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_PRODUCTAPPROVER','ROLE_PRODUCT')")
	ResponseEntity<?> saveProduct(@RequestPart(name = "body", required = false) Product product,
			HttpServletRequest request) throws MessagingException {
		LocalDateTime createDateTime = LocalDateTime.now();
		LocalDateTime updatedDateTime = LocalDateTime.now();
		product.setCreate_date(createDateTime);
		product.setUpdatedDateTime(updatedDateTime);
		product.setApproval_status("Pending");
		Brand brand = brandRepository.getById(Long.parseLong(product.getBrandId()));
		product.setBrand(brand);
		Category category = categoryRepository.getById(Long.parseLong(product.getCategoryId()));
		product.setCategory(category);
		Family family = familyRepository.getById(Long.parseLong(product.getFamilyId()));
		product.setFamily(family);

		if (productRepository.findIfExists(Long.parseLong(product.getBrandId()),
				Long.parseLong(product.getCategoryId()), Long.parseLong(product.getFamilyId()),
				product.getPname()) > 0) {
			String errorMsg = "Product: " + product.getPname() + "for Brand: " + brand.getBrandName()
					+ " and Category: " + category.getCategoryName() + " and Family: " + family.getFamilyName()
					+ " is already registered";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorMessage(400, errorMsg, "Bad Request", request.getRequestURI()));
		}

		product.setStatus("Active");
		productService.saveProduct(product);

		List<Product> products = productRepository.findByProductIdForMail(product.getId());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (Workbook workbook = new XSSFWorkbook();) {
			Sheet sheet = workbook.createSheet(SHEET);

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < HEADERs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(HEADERs[col]);
			}

			int rowIdx = 1;
			for (Product mailProduct : products) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(mailProduct.getId());
				row.createCell(1).setCellValue(mailProduct.getBrand().getBrandName());
				row.createCell(2).setCellValue(mailProduct.getCategory().getCategoryName());
				row.createCell(3).setCellValue(mailProduct.getFamily().getFamilyName());
				row.createCell(4).setCellValue(mailProduct.getVariant());
				row.createCell(5).setCellValue(mailProduct.getPname());
				row.createCell(6).setCellValue(mailProduct.getCode());
				row.createCell(7).setCellValue(mailProduct.getGroup_name());
				row.createCell(8).setCellValue(mailProduct.getUom());
				row.createCell(9).setCellValue(mailProduct.getDescription());

			}

			workbook.write(out);
		} catch (IOException e) {
			throw new RuntimeException("fail to convert data to Excel file: " + e.getMessage());
		}
		byte[] excelFileAsBytes = out.toByteArray();
		ByteArrayResource resource = new ByteArrayResource(excelFileAsBytes);
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		mimeMessageHelper.setFrom("mis.gcllp@gmail.com");
		mimeMessageHelper
				.setTo(new String[] { "mis@gardenia.ws", "vishal.thakur@gardenia.ws", "anoop.motiani@gardenia.ws" });
		mimeMessageHelper.setSubject("New Product");
		mimeMessageHelper.setText("New Product has been Added to Pending State to review.");
		mimeMessageHelper.addAttachment("Product.xlsx", resource);
		javaMailSender.send(mimeMessage);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "Product Added Successfully", "OK", request.getRequestURI()));
	}

	@Autowired
	ExportService exportService;

	@GetMapping("/export/excel")
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_PRODUCTAPPROVER','ROLE_PRODUCT')")
	public ResponseEntity<Resource> getFile() {
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDate downloadDate = localDateTime.toLocalDate();
		String filename = "Products_" + downloadDate + ".xlsx";
		InputStreamResource file = new InputStreamResource(exportService.productArrayInputStream());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_PRODUCTAPPROVER','ROLE_PRODUCT')")
	ResponseEntity<?> updateProduct(@PathVariable Long id,
			@RequestPart(name = "body", required = false) Product product, HttpServletRequest request)
			throws MessagingException {

		// Get Existing Student
		Product existingProduct = productService.getProduct(id);
		LocalDateTime updatedDateTime = LocalDateTime.now();
		existingProduct.setUpdatedDateTime(updatedDateTime);
		if (product.getStatus() == "Inactive") {
			existingProduct.setInactive_date(updatedDateTime);
		}

		existingProduct.setPname(product.getPname());
		Brand brand = brandRepository.getById(Long.parseLong(product.getBrandId()));
		existingProduct.setBrand(brand);
		Category category = categoryRepository.getById(Long.parseLong(product.getCategoryId()));
		existingProduct.setCategory(category);
		Family family = familyRepository.getById(Long.parseLong(product.getFamilyId()));
		existingProduct.setFamily(family);
		existingProduct.setVariant(product.getVariant());
		if (product.getGroup_name() != null) {
			existingProduct.setGroup_name(product.getGroup_name());
		}
		if (product.getPtd() != null) {
			existingProduct.setPtd(product.getPtd());
		}
		if (product.getPtr() != null) {
			existingProduct.setPtr(product.getPtr());
		}
		if (product.getDescription() != null) {
			existingProduct.setDescription(product.getDescription());
		}
		if (product.getSalesDiaryCode() != null) {
			existingProduct.setSalesDiaryCode(product.getSalesDiaryCode());
		}
		if (product.getMrp() != null) {
			existingProduct.setMrp(product.getMrp());
		}
		if (product.getSchemeQty() != null) {
			existingProduct.setSchemeQty(product.getSchemeQty());
		}
		if (product.getGstRate() != null) {
			existingProduct.setGstRate(product.getGstRate());
		}
		if (product.getMinQty() != null) {
			existingProduct.setMinQty(product.getMinQty());
		}
		if (product.getHSNCode() != null) {
			existingProduct.setHSNCode(product.getHSNCode());
		}
		existingProduct.setUom(product.getUom());
		existingProduct.setStatus(product.getStatus());
		// Save Student
		productService.editProduct(existingProduct);

//		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//		mimeMessageHelper.setFrom("mis.gcllp@gmail.com");
//		mimeMessageHelper.setTo(new String[]{"bhavikdesai1710@gmail.com","bhavikdesai1717@gmail.com"});
//		mimeMessageHelper.setSubject("Product");
//		mimeMessageHelper.setText("Product Edited");
//		javaMailSender.send(mimeMessage);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "Product Edited Successfully", "OK", request.getRequestURI()));
	}

	@GetMapping("/approve/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_PRODUCTAPPROVER')")
	public ResponseEntity<?> approveProduct(@PathVariable Long id, HttpServletRequest request)
			throws MessagingException {

		Integer code = productRepository.findByCode();

		code += 1;

		Long pID = id;
		String approved = "Approved";
		String productName= "";
		productRepository.updateByApprovedStatus(approved, pID, code.toString());
		productRepository.updateCode(code);
		List<Product> products = productRepository.findByProductIdForMail(pID);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (Workbook workbook = new XSSFWorkbook();) {
			Sheet sheet = workbook.createSheet(SHEET);

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < HEADERs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(HEADERs[col]);
			}

			int rowIdx = 1;
			for (Product product : products) {
				Row row = sheet.createRow(rowIdx++);
				productName = product.getPname();
				row.createCell(0).setCellValue(product.getId());
				row.createCell(1).setCellValue(product.getBrand().getBrandName());
				row.createCell(2).setCellValue(product.getCategory().getCategoryName());
				row.createCell(3).setCellValue(product.getFamily().getFamilyName());
				row.createCell(4).setCellValue(product.getVariant());
				row.createCell(5).setCellValue(product.getPname());
				row.createCell(6).setCellValue(product.getCode());
				row.createCell(7).setCellValue(product.getGroup_name());
				row.createCell(8).setCellValue(product.getUom());
				row.createCell(9).setCellValue(product.getDescription());

			}

			workbook.write(out);
		} catch (IOException e) {
			throw new RuntimeException("fail to convert data to Excel file: " + e.getMessage());
		}
		byte[] excelFileAsBytes = out.toByteArray();
		ByteArrayResource resource = new ByteArrayResource(excelFileAsBytes);
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		mimeMessageHelper.setFrom("mis.gcllp@gmail.com");
		mimeMessageHelper.setTo(new String[] { "jasmeet@gardenia.ws", "harish.singh@gardenia.ws" });
		mimeMessageHelper.setCc(new String[] { "mis@gardenia.ws", "anoop.motiani@gardenia.ws",
				"vishal.thakur@gardenia.ws", "chandrakant@gardenia.ws", "bhupendra.singh@gardenia.ws" });
		mimeMessageHelper.setSubject("Product Approval Status");
		mimeMessageHelper.setText("Product: " + productName + " has been Approved. Please find the approved product details attachment.");
		mimeMessageHelper.addAttachment("Product.xlsx", resource);
		javaMailSender.send(mimeMessage);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "Product Approved", "OK", request.getRequestURI()));
	}

	@PostMapping("/reject/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_PRODUCTAPPROVER')")
	public ResponseEntity<?> rejectProduct(@PathVariable Long id, @RequestBody Map<String, Object> body,
			HttpServletRequest request) {
		Long pID = id;
		String rejectReason = body.get("rejectReason").toString();
		String approved = "Rejected";
		productRepository.updateByStatus(approved, rejectReason, pID);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "Product Rejected", "OK", request.getRequestURI()));
	}

}
