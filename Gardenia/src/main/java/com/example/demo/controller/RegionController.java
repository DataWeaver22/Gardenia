package com.example.demo.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.example.demo.entity.State;
import com.example.demo.repository.DistributorCodeRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.StateRepository;
import com.example.demo.Export.RegionExportExcel;
import com.example.demo.Export.StateExportExcel;
import com.example.demo.Export.Service.ExportService;
import com.example.demo.Import.RegionImportHelper;
import com.example.demo.Import.StateHelper;
import com.example.demo.Import.Service.ImportResponseMessage;
import com.example.demo.entity.Area;
import com.example.demo.entity.City;
import com.example.demo.entity.Country;
import com.example.demo.entity.DistributorCode;
import com.example.demo.entity.Region;
import com.example.demo.service.DistributorCodeService;
import com.example.demo.service.RegionImportService;
import com.example.demo.service.RegionService;
import com.example.demo.service.StateService;

@RestController
@RequestMapping("/region")
public class RegionController {
	String regionCodeString;
	
	
	@Autowired
	private RegionService regionService;
	
	@Autowired
	private RegionRepository regionRepository;
	
	@Autowired
	private StateService stateService;
	
	@Autowired
	private DistributorCodeRepository distributorCodeRepository;
	
	@Autowired
	private StateRepository stateRepository;
	
	@Autowired
	private DistributorCodeService distributorCodeService;
	
	@Autowired
	private RegionImportService regionImportService;

	public RegionController(RegionService regionService) {
		super();
		this.regionService = regionService;
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Map<String, Object>> listRegion(
			@RequestParam(defaultValue = "1")Integer page,
			@RequestParam(defaultValue = "regionName")String sortBy, 
			@RequestParam(defaultValue = "25")Integer pageSize,
			@RequestParam(defaultValue = "DESC") String DIR){
		
		try {
			List<Region> regions = new ArrayList<Region>();
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
		      
		      
		      Page<Region> pageRegion;
		      pageRegion = regionRepository.findAll(paging);
		      regions = pageRegion.getContent();
		      Map<String, Object> pageContent = new HashMap<>();
		      pageContent.put("currentPage", page);
		      pageContent.put("pageSize", pageRegion.getSize());
		      pageContent.put("totalPages", pageRegion.getTotalPages());
		      pageContent.put("totalElements", pageRegion.getTotalElements());
		      pageContent.put("sortDirection", DIR);
		      Map<String, Object> response = new HashMap<>();
		      response.put("data", regions);
		      response.put("pagination", pageContent);
		      
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
		      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@PostMapping("/upload/import")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	  public ResponseEntity<ImportResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
	    String message = "";

	    if (RegionImportHelper.hasExcelFormat(file)) {
	      try {
	        regionImportService.save(file);

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
	
	@GetMapping("/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_USER','ROLE_RSM')")
	public List<Map<String, Object>> dropDownValues(@RequestParam Optional<Long> stateId) {
		// Create student object to hold student form data
		List<Region> regions;
		if(stateId.isPresent()) {
			regions = regionRepository.filterByState(stateId);
		}else {
			regions = regionService.getAllRegion();
		}
		List<Map<String, Object>> regionList = new ArrayList<Map<String, Object>>();
		for(int i=0;i<regions.size();i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", regions.get(i).getRegionName());
			pageContent.put("value", regions.get(i).getId());
			regionList.add(pageContent);
		}
		return regionList;
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	Region saveRegion(@RequestBody Map<String,Object> body ) {
		DistributorCode distributorCode = new DistributorCode();
		Region region = new Region();
		
		State state = stateRepository.getById(Long.parseLong(body.get("stateId").toString()));
		region.setRegionCode(body.get("regionCode").toString());
		region.setRegionName(body.get("regionName").toString());
		region.setState(state);
		System.out.println(state.getStateCode());
		regionService.saveRegion(region);
		
		
		regionCodeString = body.get("regionCode").toString();
		String codeNumberInteger = "000";
		distributorCode.setCodeNumber(codeNumberInteger);
		distributorCode.setRegionCode(regionCodeString);
		distributorCode.setRegion(region);
//		System.out.println(distributorCode);
		distributorCodeService.saveDistributorCode(distributorCode);
		
		return regionService.getRegionById(region.getId());
	}
	
	@Autowired
	ExportService exportService;
	
	@GetMapping("/export/excel")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Resource> getFile() {
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDate downloadDate = localDateTime.toLocalDate();
	    String filename = "Region_"+downloadDate+".xlsx";
	    InputStreamResource file = new InputStreamResource(exportService.regionArrayInputStream());

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
	        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	        .body(file);
	  }
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	Region updateRegion(@PathVariable Long id,
			@RequestBody Map<String,Object> body) {
		
		//Get Existing Region
		Region existingRegion = regionService.getRegionById(id);
				
		//Save Region
		State state = stateRepository.getById(Long.parseLong(body.get("stateId").toString()));
		existingRegion.setRegionCode(body.get("regionCode").toString());
		existingRegion.setRegionName(body.get("regionName").toString());
		existingRegion.setState(state);
		
		
		regionCodeString = body.get("regionCode").toString();
		Long rId = id;
		System.out.println(rId);
		DistributorCode distributorCode = new DistributorCode();
		Long dcId = regionRepository.findDCByRegionId(rId);
		DistributorCode existingdistributorCode = distributorCodeService.getDistributorCodeById(dcId);
		distributorCode.setCodeNumber(existingdistributorCode.getCodeNumber());
		distributorCode.setRegionCode(regionCodeString);
		distributorCode.setRegion(existingRegion);
		distributorCode.setId(dcId);
		distributorCodeService.editDistributorCode(distributorCode);
		
		return regionService.editRegion(existingRegion);
	}
	
	@GetMapping("/{id}")
	public String deleteRegion(@PathVariable Long id) {
		regionService.deleteRegionById(id);
		return "redirect:/region";
	}
}

