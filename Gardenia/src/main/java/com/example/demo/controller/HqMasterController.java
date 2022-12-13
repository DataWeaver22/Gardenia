package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.example.demo.repository.HqRepository;
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
	private HqMasterImportService hqMasterImportService;

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
			pageHqMaster = hqRepository.findAll(paging);
			hqMasters = pageHqMaster.getContent();
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("currentPage", page);
			pageContent.put("pageSize", pageHqMaster.getSize());
			pageContent.put("totalPages", pageHqMaster.getTotalPages());
			pageContent.put("totalElements", pageHqMaster.getTotalElements());
			pageContent.put("sortDirection", DIR);
			Map<String, Object> response = new HashMap<>();
			response.put("data", hqMasters);
			response.put("pagination", pageContent);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	HqMaster saveHqMaster(@RequestBody HqMaster hqmaster) {
		String hq = hqmaster.getHqDesignation();
		System.out.println(hq);

		return hqService.saveHqMaster(hqmaster);
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
	HqMaster updateHqMaster(@PathVariable Long id, @RequestBody HqMaster hqmaster, Model model) {

		// Get Existing HqMaster
		HqMaster existingHqMaster = hqService.getHqMaster(id);
		existingHqMaster.setHqCode(hqmaster.getHqCode());
		existingHqMaster.setHqName(hqmaster.getHqName());
		existingHqMaster.setHqDesignation(hqmaster.getHqDesignation());

		// Save HqMaster

		return hqService.editHqMaster(existingHqMaster);
	}

	@GetMapping("/{id}")
	public String deleteHqMaster(@PathVariable Long id) {
		hqService.deleteHqMasterById(id);
		return "redirect:/hqmaster";
	}
}
