package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Export.Service.ExportService;
import com.example.demo.Import.DistributorImportHelper;
import com.example.demo.Import.DistributorImportService;
import com.example.demo.Import.UserImportHelper;
import com.example.demo.Import.Service.ImportResponseMessage;
import com.example.demo.entity.Brand;
import com.example.demo.entity.BrandAssociatedToDist;
import com.example.demo.entity.City;
import com.example.demo.entity.Distributor;
import com.example.demo.entity.DistributorCode;
import com.example.demo.entity.District;
import com.example.demo.entity.FileDB;
import com.example.demo.entity.Region;
import com.example.demo.entity.State;
import com.example.demo.entity.User;
import com.example.demo.repository.BrandAssociatedToDistRepository;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.DistRepository;
import com.example.demo.repository.DistributorCodeRepository;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.repository.FileDBRepository;
import com.example.demo.repository.HqUserRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.StateRepository;
import com.example.demo.service.DistributorCodeService;
import com.example.demo.service.DistributorService;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/distributor")
public class DistController {
	private DistributorService distributorService;
	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private BrandAssociatedToDistRepository brandAssociatedToDistRepository;

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private HqUserRepository hqUserRepository;

	@Autowired
	private DistRepository distRepository;

	@Autowired
	private DistributorCodeRepository distributorCodeRepository;

	@Autowired
	private DistributorCodeService distributorCodeService;

	@Autowired
	private DistributorImportService distributorImportService;

	public DistController(DistributorService distributorService) {
		super();
		this.distributorService = distributorService;
	}

	@PostMapping("/upload/import")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<ImportResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (DistributorImportHelper.hasExcelFormat(file)) {
			try {
				distributorImportService.save(file);

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
	private ExportService exportService;

	@GetMapping("/export/excel")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Resource> getFile() {
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDate downloadDate = localDateTime.toLocalDate();
		String filename = "Distributors_" + downloadDate + ".xlsx";
		InputStreamResource file = new InputStreamResource(exportService.distributorArrayInputStream());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public ResponseEntity<Map<String, Object>> listDistributor(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "updatedDateTime") String sortBy,
			@RequestParam(defaultValue = "25") Integer pageSize, @RequestParam(defaultValue = "DESC") String DIR,
			@RequestParam(defaultValue = "Approved") Optional<String> distributorStatus) {

		try {
			List<Distributor> distributors = new ArrayList<Distributor>();
			PageRequest pageRequest;
			Pageable paging;
			if (DIR.equals("DESC")) {
				pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, sortBy);
				paging = pageRequest;

			} else {
				pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.ASC, sortBy);
				paging = pageRequest;
			}

			Page<Distributor> pageDistributors;
			pageDistributors = distRepository.findByDistributorStatus(distributorStatus, paging);
			distributors = pageDistributors.getContent();
			List<Map<String, Object>> distributorList = new ArrayList<Map<String, Object>>();

			for (int i = 0; i < distributors.size(); i++) {
				Map<String, Object> distributorMap = new HashMap<String, Object>();
				distributorMap.put("id", distributors.get(i).getId());
				distributorMap.put("distributorName", distributors.get(i).getDistributorName());
				distributorMap.put("distributorCode", distributors.get(i).getDistributorCode());
				distributorMap.put("distributorType", distributors.get(i).getDistributorType());
				distributorMap.put("gstin", distributors.get(i).getGstin());
				distributorMap.put("pan", distributors.get(i).getPan());
				distributorMap.put("contact", distributors.get(i).getContact());
				distributorMap.put("mobile", distributors.get(i).getMobile());
				distributorMap.put("phone", distributors.get(i).getPhone());
				distributorMap.put("email", distributors.get(i).getEmail());
				distributorMap.put("address", distributors.get(i).getAddress());
				distributorMap.put("suppName", distributors.get(i).getSuppName());
				distributorMap.put("suppCode", distributors.get(i).getSuppCode());
				distributorMap.put("status", distributors.get(i).getStatus());
				distributorMap.put("create_date", distributors.get(i).getCreate_date());
				distributorMap.put("inactive_date", distributors.get(i).getInactive_date());
				distributorMap.put("updatedDateTime", distributors.get(i).getUpdatedDateTime());
				distributorMap.put("serviceStatus", distributors.get(i).getServiceStatus());
				distributorMap.put("approvalStatus", distributors.get(i).getApprovalStatus());
				distributorMap.put("region", distributors.get(i).getRegion());
				distributorMap.put("state", distributors.get(i).getState());
				distributorMap.put("district", distributors.get(i).getDistrict());
				distributorMap.put("city", distributors.get(i).getCity());
				distributorMap.put("user", distributors.get(i).getUser());

				Long distributorId = distributors.get(i).getId();
				System.out.println(distributorId);
				List<BrandAssociatedToDist> brandAssociatedToDists = new ArrayList<BrandAssociatedToDist>();
				brandAssociatedToDists = brandAssociatedToDistRepository.findByDistributor(distributorId);
				List<Map<String, Object>> brandList = new ArrayList<Map<String, Object>>();
				for (int j = 0; j < brandAssociatedToDists.size(); j++) {
					Map<String, Object> brandMap = new HashMap<String, Object>();
					brandMap.put("label", brandAssociatedToDists.get(j).getBrand().getBrandName());
					brandMap.put("value", brandAssociatedToDists.get(j).getBrand().getId());
					brandList.add(brandMap);
				}
				distributorMap.put("brandList", brandList);

				distributorMap.put("gstFile", distributors.get(i).getGstFile());
				distributorMap.put("panFile", distributors.get(i).getPanFile());
				distributorMap.put("companyCertificateFile", distributors.get(i).getCompanyCertificateFile());

				distributorList.add(distributorMap);

			}
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("currentPage", page);
			pageContent.put("pageSize", pageDistributors.getSize());
			pageContent.put("totalPages", pageDistributors.getTotalPages());
			pageContent.put("totalElements", pageDistributors.getTotalElements());
			pageContent.put("sortDirection", DIR);
			Map<String, Object> response = new HashMap<>();
			response.put("data", distributorList);
			response.put("pagination", pageContent);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Autowired
	private FileStorageService storageService;

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	List<Map<String, Object>> saveDistributor(@RequestPart(name = "body", required = false) Distributor distributor,
			@RequestPart(name = "gstFile", required = false) MultipartFile gstFile,
			@RequestPart(name = "panFile", required = false) MultipartFile panFile,
			@RequestPart(name = "companyCertificateFile", required = false) MultipartFile companyCertificateFile) {
		// Distributor distributor = new Distributor();
		LocalDateTime createDateTime = LocalDateTime.now();
		LocalDateTime updatedDateTime = LocalDateTime.now();
		distributor.setCreate_date(createDateTime);
		distributor.setUpdatedDateTime(updatedDateTime);
		distributor.setApprovalStatus("Pending");

		if (distributor.getRegionId() != null) {

			Region region = regionRepository.getById(Long.parseLong(distributor.getRegionId().toString()));
			distributor.setRegion(region);

			// Code
			Long regionDCId = Long.parseLong(distributor.getRegionId().toString());
			DistributorCode distributorCode = distributorCodeRepository.findByRegionId(regionDCId);
			Long codeNumber = Long.parseLong(distributorCode.getCodeNumber().toString());
			String finalCodeNumber = "";
			codeNumber += 1;
			if (codeNumber >= 0 && codeNumber < 10) {
				finalCodeNumber = "00" + codeNumber;
			} else if (codeNumber >= 10 && codeNumber < 100) {
				finalCodeNumber = "0" + codeNumber;
			} else {
				finalCodeNumber = codeNumber.toString();
			}
			distributorCode.setCodeNumber(finalCodeNumber);
			distributorCodeService.editDistributorCode(distributorCode);
			String DistributorCode = distributorCode.getRegionCode().toString() + finalCodeNumber;
			distributor.setDistributorCode(DistributorCode);
		}
		if (distributor.getStateId() != null) {
			State state = stateRepository.getById(Long.parseLong(distributor.getStateId().toString()));
			distributor.setState(state);
		}
		if (distributor.getCityId() != null) {
			City city = cityRepository.getById(Long.parseLong(distributor.getCityId().toString()));
			distributor.setCity(city);
		}
		if (distributor.getDistrictId() != null) {
			District district = districtRepository.getById(Long.parseLong(distributor.getDistrictId()));
			distributor.setDistrict(district);
		}
		if (distributor.getUserId() != null) {
			User user = hqUserRepository.getById(Long.parseLong(distributor.getUserId()));
			distributor.setUser(user);
		}

		// GST File
		if (gstFile != null) {
			FileDB gstFileDB = new FileDB();
			String gstFileName = StringUtils.cleanPath(gstFile.getOriginalFilename());
			gstFileDB.setName(gstFileName);
			gstFileDB.setType(gstFile.getContentType());
			try {
				gstFileDB.setData(gstFile.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			storageService.store(gstFileDB);
			System.out.println(gstFileDB.getId());
			distributor.setGstFile(gstFileDB);
		}

		// PAN File
		if (panFile != null) {
			FileDB panFileDB = new FileDB();
			String panFileName = StringUtils.cleanPath(panFile.getOriginalFilename());
			panFileDB.setName(panFileName);
			panFileDB.setType(panFile.getContentType());
			try {
				panFileDB.setData(panFile.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			storageService.store(panFileDB);
			System.out.println(panFileDB.getId());
			distributor.setPanFile(panFileDB);
		}

		// Company Certificate File
		if (companyCertificateFile != null) {
			FileDB companyCertificateFileDB = new FileDB();
			String companyCertificateFileName = StringUtils.cleanPath(companyCertificateFile.getOriginalFilename());
			companyCertificateFileDB.setName(companyCertificateFileName);
			companyCertificateFileDB.setType(companyCertificateFile.getContentType());
			try {
				companyCertificateFileDB.setData(companyCertificateFile.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			storageService.store(companyCertificateFileDB);
			System.out.println(companyCertificateFileDB.getId());
			distributor.setCompanyCertificateFile(companyCertificateFileDB);
		}
		distributorService.saveDistributor(distributor);

		if (distributor.getBrandList() != null) {
			for (Map<String, Object> listMap : distributor.getBrandList()) {
				for (Map.Entry<String, Object> entry : listMap.entrySet()) {
					if (entry.getKey() == "value") {
						BrandAssociatedToDist brandAssociatedToDist = new BrandAssociatedToDist();
						Brand brand = brandRepository.getById(Long.parseLong(entry.getValue().toString()));
						brandAssociatedToDist.setBrand(brand);
						brandAssociatedToDist.setDistributor(distributor);
						brandAssociatedToDistRepository.save(brandAssociatedToDist);
					}
				}
			}
		}

		// return Object
		List<Map<String, Object>> distributorList = new ArrayList<Map<String, Object>>();

		List<Distributor> returnObjectDistributor = distRepository.findWithoutTransientColumns(distributor.getId());
		for (int i = 0; i < returnObjectDistributor.size(); i++) {
			Map<String, Object> distributorMap = new HashMap<String, Object>();
			distributorMap.put("id", returnObjectDistributor.get(i).getId());
			distributorMap.put("distributorName", returnObjectDistributor.get(i).getDistributorName());
			distributorMap.put("distributorCode", returnObjectDistributor.get(i).getDistributorCode());
			distributorMap.put("distributorType", returnObjectDistributor.get(i).getDistributorType());
			distributorMap.put("gstin", returnObjectDistributor.get(i).getGstin());
			distributorMap.put("pan", returnObjectDistributor.get(i).getPan());
			distributorMap.put("contact", returnObjectDistributor.get(i).getContact());
			distributorMap.put("mobile", returnObjectDistributor.get(i).getMobile());
			distributorMap.put("phone", returnObjectDistributor.get(i).getPhone());
			distributorMap.put("email", returnObjectDistributor.get(i).getEmail());
			distributorMap.put("address", returnObjectDistributor.get(i).getAddress());
			distributorMap.put("suppName", returnObjectDistributor.get(i).getSuppName());
			distributorMap.put("suppCode", returnObjectDistributor.get(i).getSuppCode());
			distributorMap.put("status", returnObjectDistributor.get(i).getStatus());
			distributorMap.put("create_date", returnObjectDistributor.get(i).getCreate_date());
			distributorMap.put("inactive_date", returnObjectDistributor.get(i).getInactive_date());
			distributorMap.put("updatedDateTime", returnObjectDistributor.get(i).getUpdatedDateTime());
			distributorMap.put("serviceStatus", returnObjectDistributor.get(i).getServiceStatus());
			distributorMap.put("approvalStatus", returnObjectDistributor.get(i).getApprovalStatus());
			distributorMap.put("region", returnObjectDistributor.get(i).getRegion());
			distributorMap.put("state", returnObjectDistributor.get(i).getState());
			distributorMap.put("district", returnObjectDistributor.get(i).getDistrict());
			distributorMap.put("city", returnObjectDistributor.get(i).getCity());
			distributorMap.put("user", returnObjectDistributor.get(i).getUser());

			Long distributorId = returnObjectDistributor.get(i).getId();
			System.out.println(distributorId);
			List<BrandAssociatedToDist> brandAssociatedToDists = new ArrayList<BrandAssociatedToDist>();
			brandAssociatedToDists = brandAssociatedToDistRepository.findByDistributor(distributorId);
			List<Map<String, Object>> brandList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < brandAssociatedToDists.size(); j++) {
				Map<String, Object> brandMap = new HashMap<String, Object>();
				brandMap.put("label", brandAssociatedToDists.get(j).getBrand().getBrandName());
				brandMap.put("value", brandAssociatedToDists.get(j).getBrand().getId());
				brandList.add(brandMap);
			}
			distributorMap.put("brandList", brandList);

			distributorMap.put("gstFile", returnObjectDistributor.get(i).getGstFile());
			distributorMap.put("panFile", returnObjectDistributor.get(i).getPanFile());
			distributorMap.put("companyCertificateFile", returnObjectDistributor.get(i).getCompanyCertificateFile());

			distributorList.add(distributorMap);

		}

		return distributorList;
	}

	@Autowired
	private FileDBRepository fileDBRepository;

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	List<Map<String, Object>> updateDistributor(@PathVariable Long id,
			@RequestPart(name = "body", required = false) Distributor distributor,
			@RequestPart(name = "gstFile", required = false) MultipartFile gstFile,
			@RequestPart(name = "panFile", required = false) MultipartFile panFile,
			@RequestPart(name = "companyCertificateFile", required = false) MultipartFile companyCertificateFile) {

		// Get Existing Student
		Distributor existingDistributor = distributorService.getDistributor(id);
		Long distributorId = existingDistributor.getId();
		LocalDateTime updatedDateTime = LocalDateTime.now();
		existingDistributor.setUpdatedDateTime(updatedDateTime);
		if (distributor.getStatus() != null) {
			if (distributor.getStatus().toString().equals("Inactive")) {
				existingDistributor.setInactive_date(updatedDateTime);
			}
		}

		if (distributor.getDistributorName() != null) {
			existingDistributor.setDistributorName(distributor.getDistributorName());
		}
		if (distributor.getDistributorType() != null) {
			existingDistributor.setDistributorType(distributor.getDistributorType());
		}
		if (distributor.getGstin() != null) {
			existingDistributor.setGstin(distributor.getGstin());
		}
		if (distributor.getPan() != null) {
			existingDistributor.setPan(distributor.getPan());
		}
		if (distributor.getContact() != null) {
			existingDistributor.setContact(distributor.getContact());
		}
		if (distributor.getMobile() != null) {
			existingDistributor.setMobile(distributor.getMobile());
		}
		if (distributor.getPhone() != null) {
			existingDistributor.setPhone(distributor.getPhone());
		}
		if (distributor.getEmail() != null) {
			existingDistributor.setEmail(distributor.getEmail());
		}
		if (distributor.getAddress() != null) {
			existingDistributor.setAddress(distributor.getAddress());
		}
		if (distributor.getSuppName() != null) {
			existingDistributor.setSuppName(distributor.getSuppName());
		}
		if (distributor.getSuppCode() != null) {
			existingDistributor.setSuppCode(distributor.getSuppCode());
		}
		if (distributor.getStatus() != null) {
			existingDistributor.setStatus(distributor.getStatus());
		}
		if (distributor.getServiceStatus() != null) {
			existingDistributor.setServiceStatus(distributor.getServiceStatus());
		}
		if (distributor.getApprovalStatus() != null) {
			existingDistributor.setApprovalStatus(distributor.getApprovalStatus());
		}

		if (distributor.getRegionId() != null) {
			// Code
			if (!existingDistributor.getRegion().getId().toString().equals(distributor.getRegionId())) {
				Long regionDCId = Long.parseLong(distributor.getRegionId().toString());
				DistributorCode distributorCode = distributorCodeRepository.findByRegionId(regionDCId);
				Long codeNumber = Long.parseLong(distributorCode.getCodeNumber().toString());
				String finalCodeNumber = "";
				codeNumber += 1;
				if (codeNumber >= 0 && codeNumber < 10) {
					finalCodeNumber = "00" + codeNumber;
				} else if (codeNumber >= 10 && codeNumber < 100) {
					finalCodeNumber = "0" + codeNumber;
				} else {
					finalCodeNumber = codeNumber.toString();
				}
				distributorCode.setCodeNumber(finalCodeNumber);
				distributorCodeService.editDistributorCode(distributorCode);
				String DistributorCode = distributorCode.getRegionCode().toString() + finalCodeNumber;
				existingDistributor.setDistributorCode(DistributorCode);
			}
			Region region = regionRepository.getById(Long.parseLong(distributor.getRegionId().toString()));
			existingDistributor.setRegion(region);
		}
		if (distributor.getStateId() != null) {
			State state = stateRepository.getById(Long.parseLong(distributor.getStateId().toString()));
			existingDistributor.setState(state);
		}
		if (distributor.getCityId() != null) {
			City city = cityRepository.getById(Long.parseLong(distributor.getCityId().toString()));
			existingDistributor.setCity(city);
		}
		if (distributor.getDistrictId() != null) {
			District district = districtRepository.getById(Long.parseLong(distributor.getDistrictId().toString()));
			existingDistributor.setDistrict(district);
		}
		if (distributor.getUserId() != null) {
			User user = hqUserRepository.getById(Long.parseLong(distributor.getUserId().toString()));
			existingDistributor.setUser(user);
		}

		// GST File
		if (gstFile != null) {
			if (distributor.getGstFile() == null) {
				FileDB gstFileDB = new FileDB();
				String gstFileName = StringUtils.cleanPath(gstFile.getOriginalFilename());
				gstFileDB.setName(gstFileName);
				gstFileDB.setType(gstFile.getContentType());
				try {
					gstFileDB.setData(gstFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				storageService.store(gstFileDB);
				existingDistributor.setGstFile(gstFileDB);
			} else {
				FileDB gstFileDB = fileDBRepository.getById(distributor.getGstFile().getId());
				String gstFileName = StringUtils.cleanPath(gstFile.getOriginalFilename());
				gstFileDB.setName(gstFileName);
				gstFileDB.setType(gstFile.getContentType());
				try {
					gstFileDB.setData(gstFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				storageService.edit(gstFileDB);
			}
		}

		// PAN File
		if (panFile != null) {
			if (distributor.getPanFile() == null) {
				FileDB panFileDB = new FileDB();
				String panFileName = StringUtils.cleanPath(panFile.getOriginalFilename());
				panFileDB.setName(panFileName);
				panFileDB.setType(panFile.getContentType());
				try {
					panFileDB.setData(panFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				storageService.store(panFileDB);
				existingDistributor.setPanFile(panFileDB);
			} else {
				FileDB panFileDB = fileDBRepository.getById(distributor.getPanFile().getId());
				String panFileName = StringUtils.cleanPath(panFile.getOriginalFilename());
				panFileDB.setName(panFileName);
				panFileDB.setType(panFile.getContentType());
				try {
					panFileDB.setData(panFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				storageService.edit(panFileDB);
			}
		}
		// Company Certificate File
		if (companyCertificateFile != null) {
			if (distributor.getCompanyCertificateFile() == null) {
				FileDB companyCertificateFileDB = new FileDB();
				String companyCertificateFileName = StringUtils.cleanPath(companyCertificateFile.getOriginalFilename());
				companyCertificateFileDB.setName(companyCertificateFileName);
				companyCertificateFileDB.setType(companyCertificateFile.getContentType());
				try {
					companyCertificateFileDB.setData(companyCertificateFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				storageService.store(companyCertificateFileDB);
				existingDistributor.setCompanyCertificateFile(companyCertificateFileDB);
			} else {
				FileDB companyCertificateFileDB = fileDBRepository
						.getById(distributor.getCompanyCertificateFile().getId());
				String companyCertificateFileName = StringUtils.cleanPath(companyCertificateFile.getOriginalFilename());
				companyCertificateFileDB.setName(companyCertificateFileName);
				companyCertificateFileDB.setType(companyCertificateFile.getContentType());
				try {
					companyCertificateFileDB.setData(companyCertificateFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				storageService.edit(companyCertificateFileDB);
			}
		}

		distributorService.editDistributor(existingDistributor);

		// Deleting removed Brand
		if (distributor.getBrandList() != null) {
			List<BrandAssociatedToDist> brandAssociatedToDistsUpdate = new ArrayList<BrandAssociatedToDist>();
			brandAssociatedToDistsUpdate = brandAssociatedToDistRepository.findByDistributor(distributorId);
			for (int i = 0; i < brandAssociatedToDistsUpdate.size(); i++) {
				Integer count = 0;

				for (Map<String, Object> listMap : distributor.getBrandList()) {
					for (Map.Entry<String, Object> entry : listMap.entrySet()) {
						if (entry.getKey() == "value") {
							if (entry.getValue().toString()
									.equals(brandAssociatedToDistsUpdate.get(i).getBrand().getId().toString())) {
								count += 1;
							}
						}
					}
				}
				if (count <= 0) {
					System.out.println("Delete");
					brandAssociatedToDistRepository.deleteByDistributorAndId(distributorId,
							brandAssociatedToDistsUpdate.get(i).getId());
				}
			}

			// Upsert Brand

			for (Map<String, Object> listMap : distributor.getBrandList()) {
				for (Map.Entry<String, Object> entry : listMap.entrySet()) {
					if (entry.getKey() == "value") {
						Integer count = 0;
						List<BrandAssociatedToDist> brandAssociatedToDistsUpsert = new ArrayList<BrandAssociatedToDist>();
						brandAssociatedToDistsUpsert = brandAssociatedToDistRepository.findByDistributor(distributorId);
						for (int i = 0; i < brandAssociatedToDistsUpsert.size(); i++) {
							if (entry.getValue().toString()
									.equals(brandAssociatedToDistsUpsert.get(i).getBrand().getId().toString())) {
								count += 1;
								System.out.println(entry.getKey() + "-" + entry.getValue());
							}
							System.out.println(entry.getKey() + "-" + entry.getValue() + "-" + count);
						}
						if (count <= 0) {
							BrandAssociatedToDist brandAssociatedToDist = new BrandAssociatedToDist();
							Brand brand = brandRepository.getById(Long.parseLong(entry.getValue().toString()));
							brandAssociatedToDist.setBrand(brand);
							brandAssociatedToDist.setDistributor(existingDistributor);
							brandAssociatedToDistRepository.save(brandAssociatedToDist);
							System.out.println(entry.getKey() + "-" + entry.getValue());
						}
					}
				}
			}
		} else {
			brandAssociatedToDistRepository.deleteByDistributor(distributorId);
		}

		// return Object
		List<Map<String, Object>> distributorList = new ArrayList<Map<String, Object>>();

		List<Distributor> returnObjectDistributor = distRepository
				.findWithoutTransientColumns(existingDistributor.getId());
		for (int i = 0; i < returnObjectDistributor.size(); i++) {
			Map<String, Object> distributorMap = new HashMap<String, Object>();
			distributorMap.put("id", returnObjectDistributor.get(i).getId());
			distributorMap.put("distributorName", returnObjectDistributor.get(i).getDistributorName());
			distributorMap.put("distributorCode", returnObjectDistributor.get(i).getDistributorCode());
			distributorMap.put("distributorType", returnObjectDistributor.get(i).getDistributorType());
			distributorMap.put("gstin", returnObjectDistributor.get(i).getGstin());
			distributorMap.put("pan", returnObjectDistributor.get(i).getPan());
			distributorMap.put("contact", returnObjectDistributor.get(i).getContact());
			distributorMap.put("mobile", returnObjectDistributor.get(i).getMobile());
			distributorMap.put("phone", returnObjectDistributor.get(i).getPhone());
			distributorMap.put("email", returnObjectDistributor.get(i).getEmail());
			distributorMap.put("address", returnObjectDistributor.get(i).getAddress());
			distributorMap.put("suppName", returnObjectDistributor.get(i).getSuppName());
			distributorMap.put("suppCode", returnObjectDistributor.get(i).getSuppCode());
			distributorMap.put("status", returnObjectDistributor.get(i).getStatus());
			distributorMap.put("create_date", returnObjectDistributor.get(i).getCreate_date());
			distributorMap.put("inactive_date", returnObjectDistributor.get(i).getInactive_date());
			distributorMap.put("updatedDateTime", returnObjectDistributor.get(i).getUpdatedDateTime());
			distributorMap.put("serviceStatus", returnObjectDistributor.get(i).getServiceStatus());
			distributorMap.put("approvalStatus", returnObjectDistributor.get(i).getApprovalStatus());
			distributorMap.put("region", returnObjectDistributor.get(i).getRegion());
			distributorMap.put("state", returnObjectDistributor.get(i).getState());
			distributorMap.put("district", returnObjectDistributor.get(i).getDistrict());
			distributorMap.put("city", returnObjectDistributor.get(i).getCity());
			distributorMap.put("user", returnObjectDistributor.get(i).getUser());

			List<BrandAssociatedToDist> brandAssociatedToDists = new ArrayList<BrandAssociatedToDist>();
			brandAssociatedToDists = brandAssociatedToDistRepository.findByDistributor(distributorId);
			List<Map<String, Object>> brandList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < brandAssociatedToDists.size(); j++) {
				Map<String, Object> brandMap = new HashMap<String, Object>();
				brandMap.put("label", brandAssociatedToDists.get(j).getBrand().getBrandName());
				brandMap.put("value", brandAssociatedToDists.get(j).getBrand().getId());
				brandList.add(brandMap);
			}
			distributorMap.put("brandList", brandList);

			distributorMap.put("gstFile", returnObjectDistributor.get(i).getGstFile());
			distributorMap.put("panFile", returnObjectDistributor.get(i).getPanFile());
			distributorMap.put("companyCertificateFile", returnObjectDistributor.get(i).getCompanyCertificateFile());

			distributorList.add(distributorMap);

		}

		return distributorList;
	}

	@GetMapping("/approve/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public String approveDistributor(@PathVariable Long id) {
		Long distID = id;
		System.out.println(distID);
		String approved = "Approved";
		distRepository.updateByStatus(approved, distID);
		return "Approved";
	}

	@GetMapping("/reject/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public String rejectDistributor(@PathVariable Long id) {
		Long distID = id;
		System.out.println(distID);
		String approved = "Rejected";
		distRepository.updateByStatus(approved, distID);
		return "Rejected";
	}
}
