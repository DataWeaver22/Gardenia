package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.Enum.Uom;
import com.example.demo.entity.Country;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.ProductService;

import ch.qos.logback.core.status.Status;

@Controller
public class ProductController {
	
	Uom uom;
	
	@Autowired
	private ProductService productService;

	public ProductController(ProductService productService) {
		super();
		this.productService = productService;
	}
	
	@GetMapping("/product")
	public String listProduct(Model model) {
		model.addAttribute("aproduct", productService.getAllProduct());
		return "product";
	}
	
	@GetMapping("/product/new")
	public String CreateNewForm(Model model){
		
		Product product = new Product();
		 Status status;
//		List<String> status = new ArrayList<String>();
//		status.add("Active");
//		status.add("Inactive");
	    //model.addAttribute("status", status);
		
//		List<String> options = new ArrayList<String>();
//	    options.add("Units");
//	    options.add("Carton");
//	    model.addAttribute("options", options);
		model.addAttribute("aproduct",product);
		//model.addAttribute("status",status.value);
		return "create_product";
	}
	
	@PostMapping("/product")
	public String saveProduct(@ModelAttribute("aproduct") Product product, Model model) {
		String status = product.getStatus();
		System.out.println(status);
		
		String uom = product.getUom();
		System.out.println(uom);
		productService.saveProduct(product);
		return "redirect:/product";
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
	    
		model.addAttribute("product", productService.getProduct(id));
		return "edit_product";
	}
	
	@PostMapping("/product/{id}")
	public String updateProduct(@PathVariable Long id,
			@ModelAttribute("product") Product product,
			Model model) {
		
		//Get Existing Student
		Product existingProduct = productService.getProduct(id);
		existingProduct.setCode(product.getCode());
		existingProduct.setPname(product.getPname());
		
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
