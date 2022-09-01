package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Enum.Status;
import com.example.demo.Enum.Uom;
import com.example.demo.Export.ProductExportExcel;
import com.example.demo.Import.ProductImportHelper;
import com.example.demo.Import.UserImportHelper;
import com.example.demo.entity.NewProduct;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductNew;
import com.example.demo.entity.Region;
import com.example.demo.repository.ProductNewRepository;
import com.example.demo.service.ProductImportService;
import com.example.demo.service.ProductNewService;
import com.example.demo.service.ProductService;

/*
import ch.qos.logback.core.status.Status;
*/
@Controller
public class ProductController {
	
	Uom uom;
	
	@Autowired
	private ProductService productService;

	@Autowired
	ProductNewService productNewService;
	
	ProductNewRepository productNewRepository;
	
	private List<ProductNew> productNew;

	@Autowired
	private ProductImportService productImportService;
	
	public ProductController(ProductService productService) {
		super();
		this.productService = productService;
	}
	
	@GetMapping("/product/upload")
    public String index() {
        return "productUploadPage";
    }
	
	@RequestMapping(value="/product/upload/import", method=RequestMethod.POST)
	public String upload(@RequestParam("file")MultipartFile file){
		if(ProductImportHelper.checkExcelFormat(file)) {
			this.productImportService.save(file);
			
			return "redirect:/product";
		}
		return "success";
	}
	
	@GetMapping("/product")
	public String listProduct(Model model) {
		model.addAttribute("aproduct", productService.getAllProduct());
		return "product";
	}
	
	@GetMapping("/product/new")
	public String CreateNewForm(Model model){
		
		Product product = new Product();
		ProductNew productNew = new ProductNew();
		model.addAttribute("aproduct",product);
		model.addAttribute("productNew", productNew);
		return "create_product";
	}
	
	
	@PostMapping("/product")
	public String saveProduct(@ModelAttribute("aproduct") Product product,@ModelAttribute("productnew") ProductNew productNew, Model model) {
		LocalDateTime createDateTime = LocalDateTime.now();
		String status = product.getStatus();
		
		String uom = product.getUom();
		product.setCreate_date(createDateTime);
		model.addAttribute("productNew",productNew);
		productService.saveProduct(product);
		productNewService.saveProductNew(productNew);
		return "redirect:/product";
	}
	
	@GetMapping("/product/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
		/* DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss"); 
        String currentDateTime = dateFormatter.format(new Date());*/
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=product.xlsx";
        response.setHeader(headerKey, headerValue);
         
        List<Product> listUsers = productService.getAllProduct();
         
        ProductExportExcel excelExporter = new ProductExportExcel(listUsers);
         
        excelExporter.export(response);    
    }  
	
	@GetMapping("/product/edit/{id}")
	public String editProduct(@PathVariable Long id,Product product, Model model) {

//		List<String> status = new ArrayList<String>();
//		status.add("Active");
//		status.add("Inactive");
//	    model.addAttribute("status", status);
		
		Product getProduct = productService.getProduct(id);
		String pStatus = getProduct.getStatus();
		model.addAttribute("pStatus", pStatus);
		System.out.println(pStatus);
		List<String> options = new ArrayList<String>();
	    options.add("Units");
	    options.add("Carton");
	    model.addAttribute("options", options);
	    Product product2 = productService.getProduct(id);
	    String userStatus = product2.getStatus();
		model.addAttribute("userStatus", userStatus);
	    String userUom = product2.getUom();
		model.addAttribute("userUom", userUom);
		model.addAttribute("product", productService.getProduct(id));
		return "edit_product";
	}
	
	@PostMapping("/product/{id}")
	public String updateProduct(@PathVariable Long id,
			@ModelAttribute("product") Product product,@ModelAttribute("productnew") ProductNew productNew,
			Model model) {
		
		//Get Existing Student
		Product existingProduct = productService.getProduct(id);
		existingProduct.setCode(product.getCode());
		existingProduct.setPname(product.getPname());
		existingProduct.setBrand(product.getBrand());
		existingProduct.setCategory(product.getCategory());
		existingProduct.setFamily(product.getFamily());
		existingProduct.setGroup_name(product.getGroup_name());
		existingProduct.setUom(product.getUom());
		existingProduct.setPtd(product.getPtd());
		existingProduct.setPtr(product.getPtr());
		existingProduct.setStatus(product.getStatus());
		existingProduct.setDescription(product.getDescription());
		existingProduct.setSales_diary(product.getSales_diary());
		existingProduct.setMrp(product.getMrp());
		
		
		//Save Student
		productService.editProduct(existingProduct);
		return "redirect:/product";
	}
	
	@GetMapping("/product/{id}")
	public String deleteProduct(@PathVariable Long id) {
		productService.deleteProductById(id);
		return "redirect:/product";
	}
	
}
