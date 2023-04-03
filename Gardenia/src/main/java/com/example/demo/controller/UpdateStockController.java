package com.example.demo.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Distributor;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetails;
import com.example.demo.entity.Region;
import com.example.demo.entity.UpdateInvoiceStatus;
import com.example.demo.entity.UpdateStock;
import com.example.demo.repository.DistRepository;
import com.example.demo.repository.ProductDetailsRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.UpdateStockRepository;

@RestController
@RequestMapping("/updatestock")
public class UpdateStockController {

	@Autowired 
	private UpdateStockRepository updateStockRepository;
	
	@Autowired
	private RegionRepository regionRepository;
	
	@Autowired
    private DistRepository distRepository ;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductDetailsRepository productDetailsRepository;
	
	@PostMapping
	public ResponseEntity<?> updatestock(@RequestBody UpdateStock updateStock){
		UpdateStock newupdateStock = new UpdateStock();
		
		Region region = new Region();
		region = regionRepository.getById(Long.parseLong(updateStock.getRegionid().toString()));
		newupdateStock.setRegion(region);
		
		Distributor distributor = new Distributor();
		distributor = distRepository.getById(Long.parseLong(updateStock.getDistributor().toString()));
		newupdateStock.setDistributor(distributor);
		
		LocalDateTime createDateTime = LocalDateTime.now();
		newupdateStock.setCreatedDate(createDateTime);
		LocalDateTime updateDateTime = LocalDateTime.now();
		newupdateStock.setUpdatedDate(updateDateTime);
		
		newupdateStock.setMonth(updateStock.getMonth());
		newupdateStock.setYear(updateStock.getYear());
		updateStockRepository.save(newupdateStock);
		
		if(updateStock.getUpdateStockId()!=null) {
			for(Map<String,Object> productlist: updateStock.getUpdateStockId()) {
				ProductDetails productDetails = new ProductDetails();
				productDetails.setQuantity( (int) productlist.get("Quantity"));
				productDetails.setValue((BigDecimal) productlist.get("value"));
			
				Product product = new Product();
				product= productRepository.getById(Long.parseLong(updateStock.getUpdateStockId().toString()));
				productDetails.setProduct(product);
				productDetails.setUpdateStock(newupdateStock);
				
				productDetailsRepository.save(productDetails);
			}
		}
		
		
		return new ResponseEntity<>("saved", HttpStatus.OK);
	
	
	
}
	@GetMapping
	public ResponseEntity<Map<String, Object>> create(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "2") Integer pageSize,

			@RequestParam(defaultValue = "DESC") String DIR){
			
		List<UpdateStock> updateStocks = new ArrayList<UpdateStock>();
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
		
		Page<UpdateStock> pageupdate;
		pageupdate = updateStockRepository.findByFilterParam(paging);
		System.out.println(pageupdate);
		updateStocks = pageupdate.getContent();
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(int i =0; i<updateStocks.size();i++ ) {
			Map<String, Object> updatemap = new HashMap<String, Object>();
			updatemap.put("id", updateStocks.get(i).getId());
			updatemap.put("Month", updateStocks.get(i).getMonth());
			updatemap.put("year", updateStocks.get(i).getYear());
			updatemap.put("createddate", updateStocks.get(i).getCreatedDate());
			updatemap.put("distributor", updateStocks.get(i).getDistributor());
			updatemap.put("region", updateStocks.get(i).getRegion());
			updatemap.put("updateddate", updateStocks.get(i).getUpdatedDate());
			
			long id = updateStocks.get(i).getId();
			List<ProductDetails> productDetails = new ArrayList<ProductDetails>();
			productDetails = productDetailsRepository.findByParam(id);
			
			List<Map<String, Object>> productlist = new ArrayList<Map<String, Object>>();
			for(int j=0; j<productDetails.size();j++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", productDetails.get(j).getId());
				map.put("", productDetails.get(j).getQuantity());
				map.put("", productDetails.get(j).getValue());
				map.put("", productDetails.get(j).getProduct());
				
				productlist.add(map);
				
				
			}
			updatemap.put("productdetails", productlist);
			list.add(updatemap);
			
		}
		
			
	    Map<String, Object> pageContent = new HashMap<>();
		pageContent.put("currentPage", page);
		pageContent.put("pageSize", pageupdate.getSize());
		pageContent.put("totalPages",pageupdate.getTotalPages());
		pageContent.put("totalElements", pageupdate.getTotalElements());
		pageContent.put("sortDirection", DIR);
		Map<String, Object> response = new HashMap<>();
		response.put("data", list);
		response.put("pagination", pageContent);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> create(@PathVariable long id,@RequestBody UpdateStock updateStock){
		UpdateStock newupdateStock = updateStockRepository.getById(updateStock.getId());
		
		newupdateStock.setCreatedDate(updateStock.getCreatedDate());
		LocalDateTime updatedDateTime = LocalDateTime.now();
		newupdateStock.setUpdatedDate(updatedDateTime);
		newupdateStock.setDistributor(updateStock.getDistributor());
		return null;
		
	}

}
