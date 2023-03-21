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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
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

import com.example.demo.Export.Service.ExportService;
import com.example.demo.Import.DistCodeImportHelper;
import com.example.demo.Import.DistributorImportHelper;
import com.example.demo.Import.DistributorImportService;
import com.example.demo.Import.UserImportHelper;
import com.example.demo.Import.Service.ImportResponseMessage;
import com.example.demo.Import.Service.ImportService;
import com.example.demo.entity.Brand;
import com.example.demo.entity.BrandAssociatedToDist;
import com.example.demo.entity.City;
import com.example.demo.entity.CurrentBusinessAssociation;
import com.example.demo.entity.Distributor;
import com.example.demo.entity.DistributorCode;
import com.example.demo.entity.District;
import com.example.demo.entity.FileDB;
import com.example.demo.entity.HqMaster;
import com.example.demo.entity.Login;
import com.example.demo.entity.RSMAssociatedRegion;
import com.example.demo.entity.Region;
import com.example.demo.entity.State;
import com.example.demo.entity.User;
import com.example.demo.entity.UserTargetDetails;
import com.example.demo.message.ErrorMessage;
import com.example.demo.repository.BrandAssociatedToDistRepository;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.CurrentBusinessAssociationRepository;
import com.example.demo.repository.DistRepository;
import com.example.demo.repository.DistributorCodeRepository;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.repository.FileDBRepository;
import com.example.demo.repository.HqRepository;
import com.example.demo.repository.HqUserRepository;
import com.example.demo.repository.LoginRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.RSMAssociatedRegionRepository;
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

	@Autowired
	private ImportService importService;

	@Autowired
	private HqRepository hqRepository;

	@Autowired
	private LoginRepository loginRepository;

	@Autowired
	private RSMAssociatedRegionRepository rsmAssociatedRegionRepository;

	@Autowired
	private CurrentBusinessAssociationRepository currentBusinessAssociationRepository;

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

	@PostMapping("/upload/code/import")
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_RSM')")
	public ResponseEntity<ImportResponseMessage> uploadCode(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (DistCodeImportHelper.hasExcelFormat(file)) {
			try {
				importService.saveCode(file);

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
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_RSM')")
	public ResponseEntity<Resource> getFile() {
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDate downloadDate = localDateTime.toLocalDate();
		String filename = "Distributors_" + downloadDate + ".xlsx";
		InputStreamResource file = new InputStreamResource(exportService.distributorArrayInputStream());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

	@GetMapping
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_RSM')")
	public ResponseEntity<Map<String, Object>> listDistributor(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "updatedDateTime") String sortBy,
			@RequestParam(defaultValue = "25") Integer pageSize, @RequestParam(defaultValue = "DESC") String DIR,
			@RequestParam(defaultValue = "Approved") Optional<String> distributorStatus) {

		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName();
			System.out.println(username);
			Login login = loginRepository.findByUsernames(username);
			Long loginId = login.getId();
			List<RSMAssociatedRegion> rsmAssociatedRegionsList = rsmAssociatedRegionRepository.findByLoginId(loginId);

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
			List<Long> regionList = new ArrayList<Long>();
			for (int i = 0; i < rsmAssociatedRegionsList.size(); i++) {
				Long regionIdLong = rsmAssociatedRegionsList.get(i).getRegion().getId();
				regionList.add(regionIdLong);
			}

			pageDistributors = distRepository.findByDistributorStatus(distributorStatus, regionList, paging);
			distributors = pageDistributors.getContent();
			System.out.println(distributors.toString());
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
				distributorMap.put("billingAddress", distributors.get(i).getBillingAddress());
				distributorMap.put("deliveryAddress", distributors.get(i).getDeliveryAddress());
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
				distributorMap.put("hq", distributors.get(i).getHqMaster());
				distributorMap.put("pinCode", distributors.get(i).getPinCode());
				distributorMap.put("rejectReason", distributors.get(i).getRejectReason());

				// Brand List

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

				// Current Bussiness Association

				List<CurrentBusinessAssociation> currentBusinessAssociations = new ArrayList<CurrentBusinessAssociation>();
				currentBusinessAssociations = currentBusinessAssociationRepository.findByDistributor(distributorId);
				List<Map<String, Object>> currentBusinessAssociationList = new ArrayList<Map<String, Object>>();
				for (int j = 0; j < currentBusinessAssociations.size(); j++) {
					Map<String, Object> cbaMap = new HashMap<String, Object>();
					cbaMap.put("companyName", currentBusinessAssociations.get(j).getCompanyName());
					cbaMap.put("distributorSince", currentBusinessAssociations.get(j).getDistributorSince());
					cbaMap.put("routesCovered", currentBusinessAssociations.get(j).getRoutesCovered());
					cbaMap.put("annualTurnover", currentBusinessAssociations.get(j).getAnnualTurnover());
					cbaMap.put("typeOfDistributor", currentBusinessAssociations.get(j).getTypeOfDistributor());
					cbaMap.put("id", currentBusinessAssociations.get(j).getId());
					currentBusinessAssociationList.add(cbaMap);
				}
				distributorMap.put("currentBusinessAssociation", currentBusinessAssociationList);

				distributorMap.put("gstFile", distributors.get(i).getGstFile());
				distributorMap.put("panFile", distributors.get(i).getPanFile());
				distributorMap.put("scannedCopyFile", distributors.get(i).getScannedCopyFile());

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
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_RSM')")
	ResponseEntity<?> saveDistributor(HttpServletRequest request,
			@RequestPart(name = "body", required = false) Distributor distributor,
			@RequestPart(name = "gstFile", required = false) MultipartFile gstFile,
			@RequestPart(name = "panFile", required = false) MultipartFile panFile,
			@RequestPart(name = "scannedCopyFile", required = false) MultipartFile scannedCopyFile) {
		// Distributor distributor = new Distributor();
		LocalDateTime createDateTime = LocalDateTime.now();
		LocalDateTime updatedDateTime = LocalDateTime.now();
		distributor.setCreate_date(createDateTime);
		distributor.setUpdatedDateTime(updatedDateTime);
		distributor.setApprovalStatus("Pending");
		if (distRepository.isDistributorPresent(distributor.getGstin()) > 0) {

			String errorMsg = "GST No: " + distributor.getGstin().toString() + " is already registered";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorMessage(400, errorMsg, "Bad Request", request.getRequestURI()));
		}
		if (distRepository.isDistributorNamePresent(distributor.getDistributorName()) > 0) {
			String errorMsg = "Distributor: " + distributor.getDistributorName().toString() + " is already registered";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorMessage(400, errorMsg, "Bad Request", request.getRequestURI()));
		}
		if (distributor.getPinCode() != null) {
			distributor.setPinCode(distributor.getPinCode());
		}

		if (distributor.getRegionId() != null) {

			Region region = regionRepository.getById(Long.parseLong(distributor.getRegionId().toString()));
			distributor.setRegion(region);
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
		if (distributor.getHqId() != null) {
			HqMaster hqMaster = hqRepository.getById(Long.parseLong(distributor.getHqId()));
			distributor.setHqMaster(hqMaster);
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
		if (scannedCopyFile != null) {
			FileDB scannedCopyFileDB = new FileDB();
			String scannedCopyFileName = StringUtils.cleanPath(scannedCopyFile.getOriginalFilename());
			scannedCopyFileDB.setName(scannedCopyFileName);
			scannedCopyFileDB.setType(scannedCopyFile.getContentType());
			try {
				scannedCopyFileDB.setData(scannedCopyFile.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			storageService.store(scannedCopyFileDB);
			System.out.println(scannedCopyFileDB.getId());
			distributor.setScannedCopyFile(scannedCopyFileDB);
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

		if (distributor.getCurrentBusinessAssociation() != null) {
			for (Map<String, Object> listMap : distributor.getCurrentBusinessAssociation()) {
				CurrentBusinessAssociation currentBusinessAssociation = new CurrentBusinessAssociation();
				currentBusinessAssociation.setCompanyName(listMap.get("companyName").toString());
				currentBusinessAssociation.setDistributorSince(listMap.get("distributorSince").toString());
				currentBusinessAssociation.setRoutesCovered(listMap.get("routesCovered").toString());
				currentBusinessAssociation.setAnnualTurnover(listMap.get("annualTurnover").toString());
				currentBusinessAssociation.setTypeOfDistributor(listMap.get("typeOfDistributor").toString());
				currentBusinessAssociation.setDistributor(distributor);
				currentBusinessAssociationRepository.save(currentBusinessAssociation);
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
			distributorMap.put("billingAddress", returnObjectDistributor.get(i).getBillingAddress());
			distributorMap.put("deliveryAddress", returnObjectDistributor.get(i).getDeliveryAddress());
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
			distributorMap.put("hq", returnObjectDistributor.get(i).getHqMaster());
			distributorMap.put("pinCode", returnObjectDistributor.get(i).getPinCode());

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
			distributorMap.put("scannedCopyFile", returnObjectDistributor.get(i).getScannedCopyFile());

			distributorList.add(distributorMap);

		}

		return ResponseEntity.status(HttpStatus.OK).body(distributorList);
	}

	@Autowired
	private FileDBRepository fileDBRepository;

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_RSM')")
	List<Map<String, Object>> updateDistributor(@PathVariable Long id,
			@RequestPart(name = "body", required = false) Distributor distributor,
			@RequestPart(name = "gstFile", required = false) MultipartFile gstFile,
			@RequestPart(name = "panFile", required = false) MultipartFile panFile,
			@RequestPart(name = "scannedCopyFile", required = false) MultipartFile scannedCopyFile) {

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
		if (distributor.getBillingAddress() != null) {
			existingDistributor.setBillingAddress(distributor.getBillingAddress());
		}
		if (distributor.getDeliveryAddress() != null) {
			existingDistributor.setDeliveryAddress(distributor.getDeliveryAddress());
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
		if (distributor.getPinCode() != null) {
			existingDistributor.setPinCode(distributor.getPinCode());
		}

		if (distributor.getRegionId() != null) {
			// Code
			if (!existingDistributor.getRegion().getId().toString().equals(distributor.getRegionId())) {
				Region newRegion = regionRepository.getById(Long.parseLong(distributor.getRegionId().toString()));
				String regionCode = newRegion.getRegionCode();
				if (regionCode.equals("HR1") || regionCode.equals("HR2")) {
					regionCode = "HR";
				}
				DistributorCode distributorCode = distributorCodeRepository.findByRegionDCode(regionCode);
				Long codeNumber = Long.parseLong(distributorCode.getCodeNumber().toString());
				String finalCodeNumber = "";
				codeNumber += 1;
				if (codeNumber >= 0 && codeNumber < 10) {
					finalCodeNumber = "0000" + codeNumber;
				} else if (codeNumber >= 10 && codeNumber < 100) {
					finalCodeNumber = "000" + codeNumber;
				} else if (codeNumber >= 100 && codeNumber < 1000) {
					finalCodeNumber = "00" + codeNumber;
				} else if (codeNumber >= 1000 && codeNumber < 10000) {
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
		if (distributor.getHqId() != null) {
			HqMaster hqMaster = hqRepository.getById(Long.parseLong(distributor.getHqId().toString()));
			existingDistributor.setHqMaster(hqMaster);
		}

		// GST File
		if (gstFile != null) {
			if (existingDistributor.getGstFile() == null) {
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
				FileDB gstFileDB = fileDBRepository.getById(existingDistributor.getGstFile().getId());
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
			if (existingDistributor.getPanFile() == null) {
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
				FileDB panFileDB = fileDBRepository.getById(existingDistributor.getPanFile().getId());
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
		if (scannedCopyFile != null) {
			if (existingDistributor.getScannedCopyFile() == null) {
				FileDB scannedCopyFileDB = new FileDB();
				String scannedCopyFileName = StringUtils.cleanPath(scannedCopyFile.getOriginalFilename());
				scannedCopyFileDB.setName(scannedCopyFileName);
				scannedCopyFileDB.setType(scannedCopyFile.getContentType());
				try {
					scannedCopyFileDB.setData(scannedCopyFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				storageService.store(scannedCopyFileDB);
				existingDistributor.setScannedCopyFile(scannedCopyFileDB);
			} else {
				FileDB scannedCopyFileDB = fileDBRepository.getById(existingDistributor.getScannedCopyFile().getId());
				String scannedCopyFileName = StringUtils.cleanPath(scannedCopyFile.getOriginalFilename());
				scannedCopyFileDB.setName(scannedCopyFileName);
				scannedCopyFileDB.setType(scannedCopyFile.getContentType());
				try {
					scannedCopyFileDB.setData(scannedCopyFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				storageService.edit(scannedCopyFileDB);
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

		// Deleting removed Current Business Association
		if (distributor.getCurrentBusinessAssociation() != null) {
			List<CurrentBusinessAssociation> currentBusinessAssociationsUpdate = new ArrayList<CurrentBusinessAssociation>();
			currentBusinessAssociationsUpdate = currentBusinessAssociationRepository.findByDistributor(distributorId);
			for (int i = 0; i < currentBusinessAssociationsUpdate.size(); i++) {
				Integer count = 0;

				for (Map<String, Object> listMap : distributor.getCurrentBusinessAssociation()) {
					for (Map.Entry<String, Object> entry : listMap.entrySet()) {
						if (entry.getKey() == "id") {
							if (entry.getValue().toString()
									.equals(currentBusinessAssociationsUpdate.get(i).getId().toString())) {
								count += 1;
							}
						}
					}
				}
				if (count <= 0) {
					System.out.println("Delete");
					currentBusinessAssociationRepository.deleteByDistributorAndId(distributorId,
							currentBusinessAssociationsUpdate.get(i).getId());
				}
			}

			// Upsert Current Business Association

			for (Map<String, Object> listMap : distributor.getCurrentBusinessAssociation()) {
				Integer count = 0;
				List<CurrentBusinessAssociation> currentBusinessAssociationsUpsert = new ArrayList<CurrentBusinessAssociation>();
				currentBusinessAssociationsUpsert = currentBusinessAssociationRepository
						.findByDistributor(distributorId);
				for (int i = 0; i < currentBusinessAssociationsUpsert.size(); i++) {
					if (listMap.get("id") != null) {
						if (listMap.get("id").toString()
								.equals(currentBusinessAssociationsUpsert.get(i).getId().toString())) {
							count += 1;
						}
					}
				}
				if (count > 0) {
					CurrentBusinessAssociation currentBusinessAssociation = currentBusinessAssociationRepository
							.getById(Long.parseLong(listMap.get("id").toString()));
					currentBusinessAssociation.setCompanyName(listMap.get("companyName").toString());
					currentBusinessAssociation.setDistributorSince(listMap.get("distributorSince").toString());
					currentBusinessAssociation.setAnnualTurnover(listMap.get("annualTurnover").toString());
					currentBusinessAssociation.setRoutesCovered(listMap.get("routesCovered").toString());
					currentBusinessAssociation.setTypeOfDistributor(listMap.get("typeOfDistributor").toString());
					currentBusinessAssociation.setDistributor(existingDistributor);
					currentBusinessAssociationRepository.save(currentBusinessAssociation);
				} else if (count <= 0) {
					CurrentBusinessAssociation currentBusinessAssociation = new CurrentBusinessAssociation();
					currentBusinessAssociation.setCompanyName(listMap.get("companyName").toString());
					currentBusinessAssociation.setDistributorSince(listMap.get("distributorSince").toString());
					currentBusinessAssociation.setAnnualTurnover(listMap.get("annualTurnover").toString());
					currentBusinessAssociation.setRoutesCovered(listMap.get("routesCovered").toString());
					currentBusinessAssociation.setTypeOfDistributor(listMap.get("typeOfDistributor").toString());
					currentBusinessAssociation.setDistributor(existingDistributor);
					currentBusinessAssociationRepository.save(currentBusinessAssociation);
				}
			}
		} else {
			currentBusinessAssociationRepository.deleteByDistributor(distributorId);
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
			distributorMap.put("billingAddress", returnObjectDistributor.get(i).getBillingAddress());
			distributorMap.put("deliveryAddress", returnObjectDistributor.get(i).getDeliveryAddress());
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
			distributorMap.put("hq", returnObjectDistributor.get(i).getHqMaster());
			distributorMap.put("pinCode", returnObjectDistributor.get(i).getPinCode());

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

			// Current Bussiness Association

			List<CurrentBusinessAssociation> currentBusinessAssociations = new ArrayList<CurrentBusinessAssociation>();
			currentBusinessAssociations = currentBusinessAssociationRepository.findByDistributor(distributorId);
			List<Map<String, Object>> currentBusinessAssociationList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < currentBusinessAssociations.size(); j++) {
				Map<String, Object> cbaMap = new HashMap<String, Object>();
				cbaMap.put("companyName", currentBusinessAssociations.get(j).getCompanyName());
				cbaMap.put("distributorSince", currentBusinessAssociations.get(j).getDistributorSince());
				cbaMap.put("routesCovered", currentBusinessAssociations.get(j).getRoutesCovered());
				cbaMap.put("annualTurnover", currentBusinessAssociations.get(j).getAnnualTurnover());
				cbaMap.put("typeOfDistributor", currentBusinessAssociations.get(j).getTypeOfDistributor());
				cbaMap.put("id", currentBusinessAssociations.get(j).getId());
				currentBusinessAssociationList.add(cbaMap);
			}
			distributorMap.put("currentBusinessAssociation", currentBusinessAssociationList);

			distributorMap.put("gstFile", returnObjectDistributor.get(i).getGstFile());
			distributorMap.put("panFile", returnObjectDistributor.get(i).getPanFile());
			distributorMap.put("scannedCopyFile", returnObjectDistributor.get(i).getScannedCopyFile());

			distributorList.add(distributorMap);

		}

		return distributorList;
	}

	@PutMapping("/{id}/reapproval")
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_RSM')")
	List<Map<String, Object>> reapprovalDistributor(@PathVariable Long id,
			@RequestPart(name = "body", required = false) Distributor distributor,
			@RequestPart(name = "gstFile", required = false) MultipartFile gstFile,
			@RequestPart(name = "panFile", required = false) MultipartFile panFile,
			@RequestPart(name = "scannedCopyFile", required = false) MultipartFile scannedCopyFile) {

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

		existingDistributor.setApprovalStatus("Pending");

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
		if (distributor.getBillingAddress() != null) {
			existingDistributor.setBillingAddress(distributor.getBillingAddress());
		}
		if (distributor.getDeliveryAddress() != null) {
			existingDistributor.setDeliveryAddress(distributor.getDeliveryAddress());
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

		if (distributor.getRegionId() != null) {
			// Code
			if (!existingDistributor.getRegion().getId().toString().equals(distributor.getRegionId())) {
				Region newRegion = regionRepository.getById(Long.parseLong(distributor.getRegionId().toString()));
				String regionCode = newRegion.getRegionCode();
				if (regionCode.equals("HR1") || regionCode.equals("HR2")) {
					regionCode = "HR";
				}
				DistributorCode distributorCode = distributorCodeRepository.findByRegionDCode(regionCode);
				Long codeNumber = Long.parseLong(distributorCode.getCodeNumber().toString());
				String finalCodeNumber = "";
				codeNumber += 1;
				if (codeNumber >= 0 && codeNumber < 10) {
					finalCodeNumber = "0000" + codeNumber;
				} else if (codeNumber >= 10 && codeNumber < 100) {
					finalCodeNumber = "000" + codeNumber;
				} else if (codeNumber >= 100 && codeNumber < 1000) {
					finalCodeNumber = "00" + codeNumber;
				} else if (codeNumber >= 1000 && codeNumber < 10000) {
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
		if (distributor.getHqId() != null) {
			HqMaster hqMaster = hqRepository.getById(Long.parseLong(distributor.getHqId().toString()));
			existingDistributor.setHqMaster(hqMaster);
		}
		if (distributor.getPinCode() != null) {
			existingDistributor.setPinCode(distributor.getPinCode());
		}

		// GST File
		if (gstFile != null) {
			if (existingDistributor.getGstFile() == null) {
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
				FileDB gstFileDB = fileDBRepository.getById(existingDistributor.getGstFile().getId());
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
			if (existingDistributor.getPanFile() == null) {
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
				FileDB panFileDB = fileDBRepository.getById(existingDistributor.getPanFile().getId());
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
		if (scannedCopyFile != null) {
			if (existingDistributor.getScannedCopyFile() == null) {
				FileDB scannedCopyFileDB = new FileDB();
				String scannedCopyFileName = StringUtils.cleanPath(scannedCopyFile.getOriginalFilename());
				scannedCopyFileDB.setName(scannedCopyFileName);
				scannedCopyFileDB.setType(scannedCopyFile.getContentType());
				try {
					scannedCopyFileDB.setData(scannedCopyFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				storageService.store(scannedCopyFileDB);
				existingDistributor.setScannedCopyFile(scannedCopyFileDB);
			} else {
				FileDB scannedCopyFileDB = fileDBRepository.getById(existingDistributor.getScannedCopyFile().getId());
				String scannedCopyFileName = StringUtils.cleanPath(scannedCopyFile.getOriginalFilename());
				scannedCopyFileDB.setName(scannedCopyFileName);
				scannedCopyFileDB.setType(scannedCopyFile.getContentType());
				try {
					scannedCopyFileDB.setData(scannedCopyFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				storageService.edit(scannedCopyFileDB);
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

		// Deleting removed Current Business Association
		if (distributor.getCurrentBusinessAssociation() != null) {
			List<CurrentBusinessAssociation> currentBusinessAssociationsUpdate = new ArrayList<CurrentBusinessAssociation>();
			currentBusinessAssociationsUpdate = currentBusinessAssociationRepository.findByDistributor(distributorId);
			for (int i = 0; i < currentBusinessAssociationsUpdate.size(); i++) {
				Integer count = 0;

				for (Map<String, Object> listMap : distributor.getCurrentBusinessAssociation()) {
					for (Map.Entry<String, Object> entry : listMap.entrySet()) {
						if (entry.getKey() == "id") {
							if (entry.getValue().toString()
									.equals(currentBusinessAssociationsUpdate.get(i).getId().toString())) {
								count += 1;
							}
						}
					}
				}
				if (count <= 0) {
					System.out.println("Delete");
					currentBusinessAssociationRepository.deleteByDistributorAndId(distributorId,
							currentBusinessAssociationsUpdate.get(i).getId());
				}
			}

			// Upsert Current Business Association

			for (Map<String, Object> listMap : distributor.getCurrentBusinessAssociation()) {
				Integer count = 0;
				List<CurrentBusinessAssociation> currentBusinessAssociationsUpsert = new ArrayList<CurrentBusinessAssociation>();
				currentBusinessAssociationsUpsert = currentBusinessAssociationRepository
						.findByDistributor(distributorId);
				for (int i = 0; i < currentBusinessAssociationsUpsert.size(); i++) {
					if (listMap.get("id") != null) {
						if (listMap.get("id").toString()
								.equals(currentBusinessAssociationsUpsert.get(i).getId().toString())) {
							count += 1;
						}
					}
				}
				if (count > 0) {
					CurrentBusinessAssociation currentBusinessAssociation = currentBusinessAssociationRepository
							.getById(Long.parseLong(listMap.get("id").toString()));
					currentBusinessAssociation.setCompanyName(listMap.get("companyName").toString());
					currentBusinessAssociation.setDistributorSince(listMap.get("distributorSince").toString());
					currentBusinessAssociation.setAnnualTurnover(listMap.get("annualTurnover").toString());
					currentBusinessAssociation.setRoutesCovered(listMap.get("routesCovered").toString());
					currentBusinessAssociation.setTypeOfDistributor(listMap.get("typeOfDistributor").toString());
					currentBusinessAssociation.setDistributor(existingDistributor);
					currentBusinessAssociationRepository.save(currentBusinessAssociation);
				} else if (count <= 0) {
					CurrentBusinessAssociation currentBusinessAssociation = new CurrentBusinessAssociation();
					currentBusinessAssociation.setCompanyName(listMap.get("companyName").toString());
					currentBusinessAssociation.setDistributorSince(listMap.get("distributorSince").toString());
					currentBusinessAssociation.setAnnualTurnover(listMap.get("annualTurnover").toString());
					currentBusinessAssociation.setRoutesCovered(listMap.get("routesCovered").toString());
					currentBusinessAssociation.setTypeOfDistributor(listMap.get("typeOfDistributor").toString());
					currentBusinessAssociation.setDistributor(existingDistributor);
					currentBusinessAssociationRepository.save(currentBusinessAssociation);
				}
			}
		} else {
			currentBusinessAssociationRepository.deleteByDistributor(distributorId);
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
			distributorMap.put("billingAddress", returnObjectDistributor.get(i).getBillingAddress());
			distributorMap.put("deliveryAddress", returnObjectDistributor.get(i).getDeliveryAddress());
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
			distributorMap.put("hq", returnObjectDistributor.get(i).getHqMaster());
			distributorMap.put("pinCode", returnObjectDistributor.get(i).getPinCode());

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

			// Current Bussiness Association

			List<CurrentBusinessAssociation> currentBusinessAssociations = new ArrayList<CurrentBusinessAssociation>();
			currentBusinessAssociations = currentBusinessAssociationRepository.findByDistributor(distributorId);
			List<Map<String, Object>> currentBusinessAssociationList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < currentBusinessAssociations.size(); j++) {
				Map<String, Object> cbaMap = new HashMap<String, Object>();
				cbaMap.put("companyName", currentBusinessAssociations.get(j).getCompanyName());
				cbaMap.put("distributorSince", currentBusinessAssociations.get(j).getDistributorSince());
				cbaMap.put("routesCovered", currentBusinessAssociations.get(j).getRoutesCovered());
				cbaMap.put("annualTurnover", currentBusinessAssociations.get(j).getAnnualTurnover());
				cbaMap.put("typeOfDistributor", currentBusinessAssociations.get(j).getTypeOfDistributor());
				cbaMap.put("id", currentBusinessAssociations.get(j).getId());
				currentBusinessAssociationList.add(cbaMap);
			}
			distributorMap.put("currentBusinessAssociation", currentBusinessAssociationList);

			distributorMap.put("gstFile", returnObjectDistributor.get(i).getGstFile());
			distributorMap.put("panFile", returnObjectDistributor.get(i).getPanFile());
			distributorMap.put("scannedCopyFile", returnObjectDistributor.get(i).getScannedCopyFile());

			distributorList.add(distributorMap);

		}

		return distributorList;
	}

	@GetMapping("/approve/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public String approveDistributor(@PathVariable Long id) {
		Long distID = id;

		Distributor distributor = distributorService.getDistributor(distID);
		// Code
		String regionCode = distributor.getRegion().getRegionCode();
		if (regionCode.equals("HR1") || regionCode.equals("HR2")) {
			regionCode = "HR";
		}
		DistributorCode distributorCode = distributorCodeRepository.findByRegionDCode(regionCode);
		Long codeNumber = Long.parseLong(distributorCode.getCodeNumber().toString());
		String finalCodeNumber = "";
		codeNumber += 1;
		if (codeNumber >= 0 && codeNumber < 10) {
			finalCodeNumber = "0000" + codeNumber;
		} else if (codeNumber >= 10 && codeNumber < 100) {
			finalCodeNumber = "000" + codeNumber;
		} else if (codeNumber >= 100 && codeNumber < 1000) {
			finalCodeNumber = "00" + codeNumber;
		} else if (codeNumber >= 1000 && codeNumber < 10000) {
			finalCodeNumber = "0" + codeNumber;
		} else {
			finalCodeNumber = codeNumber.toString();
		}
		distributorCode.setCodeNumber(finalCodeNumber);
		distributorCodeService.editDistributorCode(distributorCode);
		String distCode = distributorCode.getRegionCode().toString() + finalCodeNumber;
		String approved = "Approved";
		distRepository.updateStatusAndCode(approved, distCode, distID);
		return "Approved";
	}

	@PostMapping("/reject/{id}")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public String rejectDistributor(@PathVariable Long id, @RequestBody Map<String, Object> body) {
		Long distID = id;
		System.out.println(distID);
		String rejectReason = body.get("rejectReason").toString();
		String approved = "Rejected";
		distRepository.updateByRejectStatus(approved, rejectReason, distID);
		return "Rejected";
	}
}
