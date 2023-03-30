package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.InvoiceNo;
import com.example.demo.entity.Region;
import com.example.demo.entity.UpdateInvoiceStatus;
import com.example.demo.repository.InvoiceNoRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.UpdateInvoiceStatusRepository;

@RestController
@RequestMapping
public class UpdateInvoiceStatusController {

	@Autowired
	private UpdateInvoiceStatusRepository updateInvoiceStatusRepository;

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private InvoiceNoRepository invoiceNoRepository;

	@PostMapping("/updateInvoice")
	public ResponseEntity<?> createUser(@RequestBody UpdateInvoiceStatus updateInvoiceStatus) {
		UpdateInvoiceStatus newupInvoiceStatus = new UpdateInvoiceStatus();

		Region region = new Region();
		region = regionRepository.getById(Long.parseLong(updateInvoiceStatus.getRegionid().toString()));
		newupInvoiceStatus.setRegion(region);

		LocalDateTime createDateTime = LocalDateTime.now();
		newupInvoiceStatus.setCreatedDate(createDateTime);

		newupInvoiceStatus.setMonth(updateInvoiceStatus.getMonth());
		LocalDateTime updatedDateTime = LocalDateTime.now();
		newupInvoiceStatus.setUpdatedDate(updatedDateTime);
		newupInvoiceStatus.setYear(updateInvoiceStatus.getYear());
		updateInvoiceStatusRepository.save(newupInvoiceStatus);

		if (updateInvoiceStatus.getInvoiceno() != null) {
			// for(Map<String, Object> invoicemap: updateInvoiceStatus.getInvoiceno()) {

			for (int i = 0; i < updateInvoiceStatus.getInvoiceno().size(); i++) {
				InvoiceNo invoiceNo = new InvoiceNo();
				invoiceNo.setInvoiceNo(updateInvoiceStatus.getInvoiceno().get(i));
				invoiceNo.setUpdateInvoiceStatus(newupInvoiceStatus);
				invoiceNoRepository.save(invoiceNo);
			}

			
		}

		return new ResponseEntity<>("Saved Successfully", HttpStatus.OK);

	}

	@GetMapping("/UpdateInvoice")
	public ResponseEntity<Map<String, Object>> list(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "2") Integer pageSize,

			@RequestParam(defaultValue = "DESC") String DIR) {

		List<UpdateInvoiceStatus> updateInvoiceStatus = new ArrayList<UpdateInvoiceStatus>();
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

		Page<UpdateInvoiceStatus> pageupdate;
		pageupdate = updateInvoiceStatusRepository.findByFilterParam(paging);
		System.out.println(pageupdate);
		updateInvoiceStatus = pageupdate.getContent();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < updateInvoiceStatus.size(); i++) {
			Map<String, Object> updatemap = new HashMap<String, Object>();
			updatemap.put("id", updateInvoiceStatus.get(i).getId());
			updatemap.put("Month", updateInvoiceStatus.get(i).getMonth());
			updatemap.put("Year", updateInvoiceStatus.get(i).getYear());
			updatemap.put("CreatedDate", updateInvoiceStatus.get(i).getCreatedDate());
			updatemap.put("UpdatedDate", updateInvoiceStatus.get(i).getUpdatedDate());
			updatemap.put("Regionid", updateInvoiceStatus.get(i).getRegionid());

			Long id = updateInvoiceStatus.get(i).getId();

			List<InvoiceNo> invoiceNos = new ArrayList<InvoiceNo>();
			invoiceNos = invoiceNoRepository.findByid(id);

			List<Map<String, Object>> invoice = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < invoiceNos.size(); j++) {
				Map<String, Object> invoiceMap = new HashMap<String, Object>();
				invoiceMap.put("id", invoiceNos.get(j).getId());
				invoiceMap.put("invoiceno", invoiceNos.get(j).getInvoiceNo());
				invoice.add(invoiceMap);
			}
			updatemap.put("invoiceno", invoice);
			list.add(updatemap);
		}

		Map<String, Object> pageContent = new HashMap<>();
		pageContent.put("currentPage", page);
		pageContent.put("pageSize", pageupdate.getSize());
		pageContent.put("totalPages", pageupdate.getTotalPages());
		pageContent.put("totalElements", pageupdate.getTotalElements());
		pageContent.put("sortDirection", DIR);
		Map<String, Object> response = new HashMap<>();
		response.put("data", list);
		response.put("pagination", pageContent);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@PutMapping("/updateInvoice/{id}")
	public ResponseEntity<?> create(@RequestBody UpdateInvoiceStatus updateInvoiceStatus){
		UpdateInvoiceStatus newupdateInvoiceStatus = updateInvoiceStatusRepository.getById(updateInvoiceStatus.getId());
		
		newupdateInvoiceStatus.setCreatedDate(updateInvoiceStatus.getCreatedDate());
		LocalDateTime updatedDateTime = LocalDateTime.now();
		newupdateInvoiceStatus.setUpdatedDate(updatedDateTime);
		newupdateInvoiceStatus.setMonth(updateInvoiceStatus.getMonth());
		newupdateInvoiceStatus.setYear(updateInvoiceStatus.getYear());
		updateInvoiceStatusRepository.save(newupdateInvoiceStatus);
		
		if(updateInvoiceStatus.getInvoiceno()!=null) {
			List<InvoiceNo> inv = new ArrayList<InvoiceNo>();
			inv = invoiceNoRepository.findByid(updateInvoiceStatus.getId());
			for(int i= 0; i<inv.size();i++) {
				int count =0;
				
				
			}
			
			
//			for (int i = 0; i < updateInvoiceStatus.getInvoiceno().size(); i++) {
//				InvoiceNo invoiceNo = new InvoiceNo();
//				invoiceNo.setInvoiceNo(updateInvoiceStatus.getInvoiceno().get(i));
//				invoiceNo.setUpdateInvoiceStatus(newupdateInvoiceStatus);
//				invoiceNoRepository.save(invoiceNo);
//			}
		}
		
		
		return new ResponseEntity<>("Saved Successfully", HttpStatus.OK);
		
	}
	
}