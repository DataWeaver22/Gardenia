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
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.example.demo.Enum.Roles;
import com.example.demo.Export.HqExportExcel;
import com.example.demo.Export.Service.ExportService;
import com.example.demo.Import.HqMasterImportHelper;
import com.example.demo.Import.StateHelper;
import com.example.demo.Import.Service.ImportResponseMessage;
import com.example.demo.entity.Area;
import com.example.demo.entity.HqMaster;
import com.example.demo.entity.Region;
import com.example.demo.entity.RegionAssociatedToHq;
import com.example.demo.entity.State;
import com.example.demo.entity.StatesAssociatedToRegion;
import com.example.demo.repository.HqRepository;
import com.example.demo.repository.RegionAssociatedToHqRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.service.HqMasterImportService;
import com.example.demo.service.HqService;

@RestController
@RequestMapping("/hqmaster")
public class HqMasterController {

	@Autowired
	private HqService hqService;

	@Autowired
	private HqRepository hqRepository;

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private HqMasterImportService hqMasterImportService;

	@Autowired
	private RegionAssociatedToHqRepository regionAssociatedToHqRepository;
	
	public HqMasterController(HqService hqService) {
		super();
		this.hqService = hqService;
	}

	@PostMapping("/upload/import")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<ImportResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (HqMasterImportHelper.hasExcelFormat(file)) {
			try {
				hqMasterImportService.save(file);

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
	public ResponseEntity<Map<String, Object>> listHq(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "hqName") String sortBy, @RequestParam(defaultValue = "25") Integer pageSize,
			@RequestParam(required = false) Optional<String> hqCode,
			@RequestParam(required = false) Optional<String> hqName,
			@RequestParam(required = false) Optional<String> designation,
			@RequestParam(defaultValue = "DESC") String DIR) {
		try {
			List<HqMaster> hqMasters = new ArrayList<HqMaster>();
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

			Page<HqMaster> pageHqMaster;
			pageHqMaster = hqRepository.findByFilterParam(hqCode, hqName, designation, paging);
			hqMasters = pageHqMaster.getContent();

			List<Map<String, Object>> hqList = new ArrayList<Map<String, Object>>();

			for (int i = 0; i < hqMasters.size(); i++) {
				Map<String, Object> hqMap = new HashMap<String, Object>();
				hqMap.put("id", hqMasters.get(i).getId());
				hqMap.put("hqCode", hqMasters.get(i).getHqCode());
				hqMap.put("hqName", hqMasters.get(i).getHqName());
				hqMap.put("hqDesignation", hqMasters.get(i).getHqDesignation());

				Long hqId = hqMasters.get(i).getId();
				System.out.println(hqId);
				List<RegionAssociatedToHq> regionAssociatedToHqs = new ArrayList<RegionAssociatedToHq>();
				regionAssociatedToHqs = regionAssociatedToHqRepository.findByHq(hqId);
				List<Map<String, Object>> regionList = new ArrayList<Map<String, Object>>();
				for (int j = 0; j < regionAssociatedToHqs.size(); j++) {
					Map<String, Object> regionMap = new HashMap<String, Object>();
					regionMap.put("label", regionAssociatedToHqs.get(j).getRegion().getRegionName());
					regionMap.put("value", regionAssociatedToHqs.get(j).getRegion().getId());
					regionList.add(regionMap);
				}
				hqMap.put("regionList", regionList);

				hqList.add(hqMap);
			}

			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("currentPage", page);
			pageContent.put("pageSize", pageHqMaster.getSize());
			pageContent.put("totalPages", pageHqMaster.getTotalPages());
			pageContent.put("totalElements", pageHqMaster.getTotalElements());
			pageContent.put("sortDirection", DIR);
			Map<String, Object> response = new HashMap<>();
			response.put("data", hqList);
			response.put("pagination", pageContent);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	List<Map<String, Object>> saveHqMaster(@RequestBody HqMaster hqMaster) {

		hqService.saveHqMaster(hqMaster);

		// RegionAssociatedToHq
		if (hqMaster.getRegionList() != null) {
			for (Map<String, Object> listMap : hqMaster.getRegionList()) {
				for (Map.Entry<String, Object> entry : listMap.entrySet()) {
					if (entry.getKey() == "value") {
						RegionAssociatedToHq regionAssociatedToHq = new RegionAssociatedToHq();
						Region region = regionRepository.getById(Long.parseLong(entry.getValue().toString()));
						regionAssociatedToHq.setHqMaster(hqMaster);
						regionAssociatedToHq.setRegion(region);
						regionAssociatedToHqRepository.save(regionAssociatedToHq);
					}
				}
			}
		}

		// return Object
		List<Map<String, Object>> hqmasterList = new ArrayList<Map<String, Object>>();

		List<HqMaster> returnObjectHqMasters = hqRepository.findWithoutTransientColumns(hqMaster.getId());
		for (int i = 0; i < returnObjectHqMasters.size(); i++) {
			Map<String, Object> hqMap = new HashMap<String, Object>();
			hqMap.put("id", returnObjectHqMasters.get(i).getId());
			hqMap.put("hqCode", returnObjectHqMasters.get(i).getHqCode());
			hqMap.put("hqName", returnObjectHqMasters.get(i).getHqName());
			hqMap.put("hqDesignation", returnObjectHqMasters.get(i).getHqDesignation());

			Long hqId = returnObjectHqMasters.get(i).getId();
			System.out.println(hqId);
			List<RegionAssociatedToHq> regionAssociatedToHqs = new ArrayList<RegionAssociatedToHq>();
			regionAssociatedToHqs = regionAssociatedToHqRepository.findByHq(hqId);
			List<Map<String, Object>> regionList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < regionAssociatedToHqs.size(); j++) {
				Map<String, Object> regionMap = new HashMap<String, Object>();
				regionMap.put("label", regionAssociatedToHqs.get(j).getRegion().getRegionName());
				regionMap.put("value", regionAssociatedToHqs.get(j).getRegion().getId());
				regionList.add(regionMap);
			}
			hqMap.put("regionList", regionList);

			hqmasterList.add(hqMap);
		}
		return hqmasterList;
	}

	@Autowired
	ExportService exportService;

	@GetMapping("/export/excel")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Resource> getFile() {
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDate downloadDate = localDateTime.toLocalDate();
		String filename = "HQ_Master_" + downloadDate + ".xlsx";
		InputStreamResource file = new InputStreamResource(exportService.hqArrayInputStream());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	List<Map<String, Object>> updateHqMaster(@PathVariable Long id, @RequestBody HqMaster hqMaster) {

		// Get Existing HqMaster
		HqMaster existingHqMaster = hqService.getHqMaster(id);
		Long hqId = existingHqMaster.getId();
		existingHqMaster.setHqCode(hqMaster.getHqCode());
		existingHqMaster.setHqName(hqMaster.getHqName());
		existingHqMaster.setHqDesignation(hqMaster.getHqDesignation());

		// Save HqMaster
		hqService.editHqMaster(existingHqMaster);

		// Deleting removed Region
		if (hqMaster.getRegionList() != null) {
			List<RegionAssociatedToHq> regionAssociatedToHqsUpdate = new ArrayList<RegionAssociatedToHq>();
			regionAssociatedToHqsUpdate = regionAssociatedToHqRepository.findByHq(hqId);
			for (int i = 0; i < regionAssociatedToHqsUpdate.size(); i++) {
				Integer count = 0;

				for (Map<String, Object> listMap : hqMaster.getRegionList()) {
					for (Map.Entry<String, Object> entry : listMap.entrySet()) {
						if (entry.getKey() == "value") {
							if (entry.getValue().toString()
									.equals(regionAssociatedToHqsUpdate.get(i).getRegion().getId().toString())) {
								count += 1;
							}
						}
					}
				}
				if (count <= 0) {
					System.out.println("Delete");
					regionAssociatedToHqRepository.deleteByHqAndId(hqId, regionAssociatedToHqsUpdate.get(i).getId());
				}
			}

			// Upsert State

			for (Map<String, Object> listMap : hqMaster.getRegionList()) {
				for (Map.Entry<String, Object> entry : listMap.entrySet()) {
					if (entry.getKey() == "value") {
						Integer count = 0;
						List<RegionAssociatedToHq> regionAssociatedToHqsUpsert = new ArrayList<RegionAssociatedToHq>();
						regionAssociatedToHqsUpsert = regionAssociatedToHqRepository.findByHq(hqId);
						for (int i = 0; i < regionAssociatedToHqsUpsert.size(); i++) {
							if (entry.getValue().toString()
									.equals(regionAssociatedToHqsUpsert.get(i).getRegion().getId().toString())) {
								count += 1;
								System.out.println(entry.getKey() + "-" + entry.getValue());
							}
							System.out.println(entry.getKey() + "-" + entry.getValue() + "-" + count);
						}
						if (count <= 0) {
							RegionAssociatedToHq regionAssociatedToHq = new RegionAssociatedToHq();
							Region region = regionRepository.getById(Long.parseLong(entry.getValue().toString()));
							regionAssociatedToHq.setHqMaster(existingHqMaster);
							regionAssociatedToHq.setRegion(region);
							regionAssociatedToHqRepository.save(regionAssociatedToHq);
							System.out.println(entry.getKey() + "-" + entry.getValue());
						}
					}
				}
			}
		} else {
			regionAssociatedToHqRepository.deleteByHq(hqId);
		}

		// return Object
		List<Map<String, Object>> hqmasterList = new ArrayList<Map<String, Object>>();

		List<HqMaster> returnObjectHqMasters = hqRepository.findWithoutTransientColumns(hqId);
		for (int i = 0; i < returnObjectHqMasters.size(); i++) {
			Map<String, Object> hqMap = new HashMap<String, Object>();
			hqMap.put("id", returnObjectHqMasters.get(i).getId());
			hqMap.put("hqCode", returnObjectHqMasters.get(i).getHqCode());
			hqMap.put("hqName", returnObjectHqMasters.get(i).getHqName());
			hqMap.put("hqDesignation", returnObjectHqMasters.get(i).getHqDesignation());

			Long returnhqId = returnObjectHqMasters.get(i).getId();
			System.out.println(returnhqId);
			List<RegionAssociatedToHq> regionAssociatedToHqs = new ArrayList<RegionAssociatedToHq>();
			regionAssociatedToHqs = regionAssociatedToHqRepository.findByHq(returnhqId);
			List<Map<String, Object>> regionList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < regionAssociatedToHqs.size(); j++) {
				Map<String, Object> regionMap = new HashMap<String, Object>();
				regionMap.put("label", regionAssociatedToHqs.get(j).getRegion().getRegionName());
				regionMap.put("value", regionAssociatedToHqs.get(j).getRegion().getId());
				regionList.add(regionMap);
			}
			hqMap.put("regionList", regionList);

			hqmasterList.add(hqMap);
		}
		return hqmasterList;
	}

	@GetMapping("/{id}")
	public String deleteHqMaster(@PathVariable Long id) {
		hqService.deleteHqMasterById(id);
		return "redirect:/hqmaster";
	}
}
