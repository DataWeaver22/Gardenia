package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.http.RequestEntity;
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
import com.example.demo.entity.StatesAssociatedToRegion;
import com.example.demo.message.ErrorMessage;
import com.example.demo.repository.DistributorCodeRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.StateRepository;
import com.example.demo.repository.StatesAssociatedToRegionRepository;
import com.example.demo.Export.RegionExportExcel;
import com.example.demo.Export.StateExportExcel;
import com.example.demo.Export.Service.ExportService;
import com.example.demo.Import.RegionAssociatedToHqImportHelper;
import com.example.demo.Import.RegionImportHelper;
import com.example.demo.Import.StateHelper;
import com.example.demo.Import.StatesAssociatedToRegionImportHelper;
import com.example.demo.Import.Service.ImportResponseMessage;
import com.example.demo.Import.Service.ImportService;
import com.example.demo.entity.Area;
import com.example.demo.entity.Brand;
import com.example.demo.entity.BrandAssociatedToDist;
import com.example.demo.entity.City;
import com.example.demo.entity.Country;
import com.example.demo.entity.Distributor;
import com.example.demo.entity.DistributorCode;
import com.example.demo.entity.Region;
import com.example.demo.service.DistributorCodeService;
import com.example.demo.service.RegionImportService;
import com.example.demo.service.RegionService;
import com.example.demo.service.StateService;
import com.example.demo.service.StatesAssociatedToRegionService;

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
	public ResponseEntity<Map<String, Object>> listRegion(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "regionName") String sortBy,
			@RequestParam(required = false) Optional<String> regionCode,
			@RequestParam(required = false) Optional<String> regionName,
			@RequestParam(required = false) Optional<String> stateName,
			@RequestParam(defaultValue = "25") Integer pageSize, @RequestParam(defaultValue = "DESC") String DIR) {

		try {
			List<Region> regions = new ArrayList<Region>();
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

			Page<Region> pageRegion;
			pageRegion = regionRepository.findByFilterParam(regionCode, regionName, paging);
			regions = pageRegion.getContent();
			
			List<Map<String, Object>> regionList = new ArrayList<Map<String, Object>>();

			for (int i = 0; i < regions.size(); i++) {
				Map<String, Object> regionMap = new HashMap<String, Object>();
				regionMap.put("id", regions.get(i).getId());
				regionMap.put("regionCode", regions.get(i).getRegionCode());
				regionMap.put("regionName", regions.get(i).getRegionName());

				Long regionId = regions.get(i).getId();
				System.out.println(regionId);
				List<StatesAssociatedToRegion> statesAssociatedToRegions = new ArrayList<StatesAssociatedToRegion>();
				statesAssociatedToRegions = statesAssociatedToRegionRepository.findByRegion(regionId);
				List<Map<String, Object>> stateList = new ArrayList<Map<String, Object>>();
				for (int j = 0; j < statesAssociatedToRegions.size(); j++) {
					Map<String, Object> stateMap = new HashMap<String, Object>();
					stateMap.put("label", statesAssociatedToRegions.get(j).getState().getStateName());
					stateMap.put("value", statesAssociatedToRegions.get(j).getState().getId());
					stateList.add(stateMap);
				}
				regionMap.put("stateList", stateList);

				regionList.add(regionMap);
			}
			
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("currentPage", page);
			pageContent.put("pageSize", pageRegion.getSize());
			pageContent.put("totalPages", pageRegion.getTotalPages());
			pageContent.put("totalElements", pageRegion.getTotalElements());
			pageContent.put("sortDirection", DIR);
			Map<String, Object> response = new HashMap<>();
			response.put("data", regionList);
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
	
	@Autowired
	ImportService importService;
	
	@PostMapping("/upload/import/statesAssociated")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<ImportResponseMessage> uploadFileStatesAssociated(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (StatesAssociatedToRegionImportHelper.hasExcelFormat(file)) {
			try {
				importService.saveStatesAssociatedToRegion(file);

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
	
	@PostMapping("/upload/import/hqsAssociated")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<ImportResponseMessage> uploadFileHqsAssociated(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (RegionAssociatedToHqImportHelper.hasExcelFormat(file)) {
			try {
				importService.saveRegionAssociatedToHq(file);

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

	@Autowired
	StatesAssociatedToRegionService statesAssociatedToRegionService;
	
	@GetMapping("/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_USER','ROLE_RSM')")
	public List<Map<String, Object>> dropDownValues(@RequestParam Optional<Long> stateId) {
		// Create student object to hold student form data
		List<StatesAssociatedToRegion> statesAssociatedToRegions;
		if (stateId.isPresent()) {
			statesAssociatedToRegions = regionRepository.filterByState(stateId);
		} else {
			statesAssociatedToRegions = statesAssociatedToRegionService.getAllStatesAssociatedToRegions();
		}
		List<Map<String, Object>> regionList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < statesAssociatedToRegions.size(); i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", statesAssociatedToRegions.get(i).getRegion().getRegionName());
			pageContent.put("value", statesAssociatedToRegions.get(i).getRegion().getId());
			regionList.add(pageContent);
		}
		return regionList;
	}

	@Autowired
	private StatesAssociatedToRegionRepository statesAssociatedToRegionRepository;

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	ResponseEntity<?> saveRegion(@RequestBody Region region,HttpServletRequest request) {
		DistributorCode distributorCode = new DistributorCode();

//		State state = stateRepository.getById(Long.parseLong(region.getStateId()));
//		region.setState(state);

		regionService.saveRegion(region);
		regionCodeString = region.getRegionCode();
		Long regionPresent = regionRepository.regionCount(regionCodeString);
		System.out.println(regionPresent);
		if (regionPresent <= 0) {
			String codeNumberInteger = "00000";
			distributorCode.setCodeNumber(codeNumberInteger);
			distributorCode.setRegionCode(regionCodeString);
			distributorCode.setRegion(region);
			distributorCodeService.saveDistributorCode(distributorCode);
		}
		

		// StateAssociatedToRegion
		if (region.getStateList() != null) {
			for (Map<String, Object> listMap : region.getStateList()) {
				for (Map.Entry<String, Object> entry : listMap.entrySet()) {
					if (entry.getKey() == "value") {
						StatesAssociatedToRegion statesAssociatedToRegion = new StatesAssociatedToRegion();
						State stateAssociated = stateRepository.getById(Long.parseLong(entry.getValue().toString()));
						statesAssociatedToRegion.setState(stateAssociated);
						statesAssociatedToRegion.setRegion(region);
						statesAssociatedToRegionRepository.save(statesAssociatedToRegion);
					}
				}
			}
		}

		// return Object
		List<Map<String, Object>> regionList = new ArrayList<Map<String, Object>>();

		List<Region> returnObjectRegion = regionRepository.findWithoutTransientColumns(region.getId());
		for (int i = 0; i < returnObjectRegion.size(); i++) {
			Map<String, Object> regionMap = new HashMap<String, Object>();
			regionMap.put("id", returnObjectRegion.get(i).getId());
			regionMap.put("regionCode", returnObjectRegion.get(i).getRegionCode());
			regionMap.put("regionName", returnObjectRegion.get(i).getRegionName());

			Long regionId = returnObjectRegion.get(i).getId();
			System.out.println(regionId);
			List<StatesAssociatedToRegion> statesAssociatedToRegions = new ArrayList<StatesAssociatedToRegion>();
			statesAssociatedToRegions = statesAssociatedToRegionRepository.findByRegion(regionId);
			List<Map<String, Object>> stateList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < statesAssociatedToRegions.size(); j++) {
				Map<String, Object> stateMap = new HashMap<String, Object>();
				stateMap.put("label", statesAssociatedToRegions.get(j).getState().getStateName());
				stateMap.put("value", statesAssociatedToRegions.get(j).getState().getId());
				stateList.add(stateMap);
			}
			regionMap.put("stateList", stateList);

			regionList.add(regionMap);
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "Data Added Successfully", "OK", request.getRequestURI()));
	}

	@Autowired
	ExportService exportService;

	@GetMapping("/export/excel")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Resource> getFile() {
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDate downloadDate = localDateTime.toLocalDate();
		String filename = "Region_" + downloadDate + ".xlsx";
		InputStreamResource file = new InputStreamResource(exportService.regionArrayInputStream());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	ResponseEntity<?> updateRegion(@PathVariable Long id, @RequestBody Region region,HttpServletRequest request) {

		// Get Existing Region
		Region existingRegion = regionService.getRegionById(id);
		Long regionId = existingRegion.getId();
		System.out.println(regionId);

		// Save Region
		existingRegion.setRegionCode(region.getRegionCode());
		existingRegion.setRegionName(region.getRegionName());
		regionService.editRegion(existingRegion);

//		regionCodeString = body.get("regionCode").toString();
//		Long rId = id;
//		System.out.println(rId);
//		DistributorCode distributorCode = new DistributorCode();
//		Long dcId = regionRepository.findDCByRegionId(rId);
//		DistributorCode existingdistributorCode = distributorCodeService.getDistributorCodeById(dcId);
//		distributorCode.setCodeNumber(existingdistributorCode.getCodeNumber());
//		distributorCode.setRegionCode(regionCodeString);
//		distributorCode.setRegion(existingRegion);
//		distributorCode.setId(dcId);
//		distributorCodeService.editDistributorCode(distributorCode);
		
		// Deleting removed State
		if (region.getStateList() != null) {
			List<StatesAssociatedToRegion> statesAssociatedToRegionsUpdate = new ArrayList<StatesAssociatedToRegion>();
			statesAssociatedToRegionsUpdate = statesAssociatedToRegionRepository.findByRegion(regionId);
			for (int i = 0; i < statesAssociatedToRegionsUpdate.size(); i++) {
				Integer count = 0;

				for (Map<String, Object> listMap : region.getStateList()) {
					for (Map.Entry<String, Object> entry : listMap.entrySet()) {
						if (entry.getKey() == "value") {
							if (entry.getValue().toString()
									.equals(statesAssociatedToRegionsUpdate.get(i).getState().getId().toString())) {
								count += 1;
							}
						}
					}
				}
				if (count <= 0) {
					System.out.println("Delete");
					statesAssociatedToRegionRepository.deleteByRegionAndId(regionId,
							statesAssociatedToRegionsUpdate.get(i).getId());
				}
			}

			// Upsert State

			for (Map<String, Object> listMap : region.getStateList()) {
				for (Map.Entry<String, Object> entry : listMap.entrySet()) {
					if (entry.getKey() == "value") {
						Integer count = 0;
						List<StatesAssociatedToRegion> statesAssociatedToRegionsUpsert = new ArrayList<StatesAssociatedToRegion>();
						statesAssociatedToRegionsUpsert = statesAssociatedToRegionRepository.findByRegion(regionId);
						for (int i = 0; i < statesAssociatedToRegionsUpsert.size(); i++) {
							if (entry.getValue().toString()
									.equals(statesAssociatedToRegionsUpsert.get(i).getState().getId().toString())) {
								count += 1;
								System.out.println(entry.getKey() + "-" + entry.getValue());
							}
							System.out.println(entry.getKey() + "-" + entry.getValue() + "-" + count);
						}
						if (count <= 0) {
							StatesAssociatedToRegion statesAssociatedToRegion = new StatesAssociatedToRegion();
							State state = stateRepository.getById(Long.parseLong(entry.getValue().toString()));
							statesAssociatedToRegion.setState(state);
							statesAssociatedToRegion.setRegion(existingRegion);
							statesAssociatedToRegionRepository.save(statesAssociatedToRegion);
							System.out.println(entry.getKey() + "-" + entry.getValue());
						}
					}
				}
			}
		} else {
			statesAssociatedToRegionRepository.deleteByRegion(regionId);
		}
		
		// return Object
		List<Map<String, Object>> regionList = new ArrayList<Map<String, Object>>();

		List<Region> returnObjectRegion = regionRepository.findWithoutTransientColumns(regionId);
		for (int i = 0; i < returnObjectRegion.size(); i++) {
			Map<String, Object> regionMap = new HashMap<String, Object>();
			regionMap.put("id", returnObjectRegion.get(i).getId());
			regionMap.put("regionCode", returnObjectRegion.get(i).getRegionCode());
			regionMap.put("regionName", returnObjectRegion.get(i).getRegionName());

			Long returnRegionId = returnObjectRegion.get(i).getId();
			System.out.println(returnRegionId);
			List<StatesAssociatedToRegion> statesAssociatedToRegions = new ArrayList<StatesAssociatedToRegion>();
			statesAssociatedToRegions = statesAssociatedToRegionRepository.findByRegion(returnRegionId);
			List<Map<String, Object>> stateList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < statesAssociatedToRegions.size(); j++) {
				Map<String, Object> stateMap = new HashMap<String, Object>();
				stateMap.put("label", statesAssociatedToRegions.get(j).getState().getStateName());
				stateMap.put("value", statesAssociatedToRegions.get(j).getState().getId());
				stateList.add(stateMap);
			}
			regionMap.put("stateList", stateList);

			regionList.add(regionMap);
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "Data Edited Successfully", "OK", request.getRequestURI()));
	}
}
