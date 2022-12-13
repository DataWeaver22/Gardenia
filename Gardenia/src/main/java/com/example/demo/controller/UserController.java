package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.EmailSenderService.EmailSenderService;
import com.example.demo.Enum.Roles;
import com.example.demo.Enum.Status;
import com.example.demo.Export.RegionExportExcel;
import com.example.demo.Export.UserExportExcel;
import com.example.demo.Import.CategoryImportHelper;
import com.example.demo.Import.StateHelper;
import com.example.demo.Import.UserImportHelper;
import com.example.demo.Import.Service.ImportResponseMessage;
import com.example.demo.entity.Area;
import com.example.demo.entity.Brand;
import com.example.demo.entity.BrandAssociatedToDist;
import com.example.demo.entity.City;
import com.example.demo.entity.Distributor;
import com.example.demo.entity.DistributorCode;
import com.example.demo.entity.District;
import com.example.demo.entity.FileDB;
import com.example.demo.entity.HqMaster;
import com.example.demo.entity.Region;
import com.example.demo.entity.State;
import com.example.demo.entity.User;
import com.example.demo.entity.UserFileDB;
import com.example.demo.entity.UserTargetDetails;
import com.example.demo.repository.AreaRepository;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.repository.HqRepository;
import com.example.demo.repository.HqUserRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.StateRepository;
import com.example.demo.repository.UserTargetDetailsRepository;
import com.example.demo.service.AreaService;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.HqService;
import com.example.demo.service.RegionService;
import com.example.demo.service.StateService;
import com.example.demo.service.UserFileStorageService;
import com.example.demo.service.UserImportService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	private UserService userService;
	Status status;
	Roles roles;
	String approvalStatusString;

	@Autowired
	private HqUserRepository hqUserRepository;

	@Autowired
	private UserImportService userImportService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@Autowired
	private EmailSenderService emailSenderService;
	
	@PostMapping("/mail")
	public void email() throws MessagingException{
		emailSenderService.sendEmailWithAttachment("bhavikdesai1710@gmail.com", "Test Body", "Test Mail", "C:\\Users\\Bhavik\\OneDrive\\Documents\\Clients\\Gardenia Files\\ASE HQ.csv");
	}

	@PostMapping("/upload/import")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<ImportResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (UserImportHelper.hasExcelFormat(file)) {
			try {
				userImportService.save(file);

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
	private DistrictRepository districtRepository;

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private HqRepository hqRepository;

	@Autowired
	private UserTargetDetailsRepository userTargetDetailsRepository;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_USER','ROLE_RSM')")
	public ResponseEntity<Map<String, Object>> listUser(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "updatedDateTime") String sortBy,
			@RequestParam(defaultValue = "25") Integer pageSize, @RequestParam(defaultValue = "DESC") String DIR,
			@RequestParam(defaultValue = "Approved") Optional<String> userStatus) {

		try {
			List<User> users = new ArrayList<User>();
			PageRequest pageRequest;
			Pageable paging;
			if (DIR.equals("DESC")) {
				pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, sortBy);
				paging = pageRequest;

			} else {
				pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.ASC, sortBy);
				paging = pageRequest;
			}

			Page<User> pageUsers;
			pageUsers = hqUserRepository.findByUserStatus(userStatus, paging);
			users = pageUsers.getContent();
			List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();

			for (int i = 0; i < users.size(); i++) {
				Map<String, Object> userMap = new HashMap<String, Object>();
				userMap.put("id", users.get(i).getId());
				userMap.put("title", users.get(i).getTitle());
				userMap.put("firstName", users.get(i).getFirstName());
				userMap.put("middleName", users.get(i).getMiddleName());
				userMap.put("lastName", users.get(i).getLastName());
				userMap.put("fullName", users.get(i).getFullName());
				userMap.put("login", users.get(i).getLogin());
				userMap.put("gender", users.get(i).getGender());
				userMap.put("maritalStatus", users.get(i).getMaritalStatus());
				userMap.put("birthDate", users.get(i).getBirthDate());
				userMap.put("email", users.get(i).getEmail());
				userMap.put("empCode", users.get(i).getEmpCode());
				userMap.put("team", users.get(i).getTeam());
				userMap.put("companyCode", users.get(i).getCompanyCode());
				userMap.put("grade", users.get(i).getGrade());
				userMap.put("branch", users.get(i).getBranch());
				userMap.put("department", users.get(i).getDepartment());
				userMap.put("processStartDate", users.get(i).getProcessStartDate());
				userMap.put("paymentMode", users.get(i).getPaymentMode());
				userMap.put("region", users.get(i).getRegion());
				userMap.put("state", users.get(i).getState());
				userMap.put("district", users.get(i).getDistrict());
				userMap.put("city", users.get(i).getCity());
				userMap.put("area", users.get(i).getArea());
				userMap.put("status", users.get(i).getStatus());
				userMap.put("hq", users.get(i).getHqMaster());
				userMap.put("role", users.get(i).getRole());
				userMap.put("rsm", users.get(i).getRsm());
				userMap.put("asm", users.get(i).getAsm());
				userMap.put("ase", users.get(i).getAse());
				userMap.put("createDate", users.get(i).getCreateDate());
				userMap.put("inactiveDate", users.get(i).getInactiveDate());
				userMap.put("updatedDateTime", users.get(i).getUpdatedDateTime());
				userMap.put("approvalStatus", users.get(i).getApprovalStatus());

				Long userId = users.get(i).getId();
				List<UserTargetDetails> userTargetDetailsList = new ArrayList<UserTargetDetails>();
				userTargetDetailsList = userTargetDetailsRepository.findByUser(userId);
				List<Map<String, Object>> targetList = new ArrayList<Map<String, Object>>();
				for (int j = 0; j < userTargetDetailsList.size(); j++) {
					Map<String, Object> targetMap = new HashMap<String, Object>();
					targetMap.put("hq", userTargetDetailsList.get(j).getHq());
					targetMap.put("present", userTargetDetailsList.get(j).getPresent());
					targetMap.put("goal", userTargetDetailsList.get(j).getGoal());
					targetMap.put("id", userTargetDetailsList.get(j).getId());
					targetList.add(targetMap);
				}
				userMap.put("userTargetDetails", targetList);

				userMap.put("aadharFile", users.get(i).getAadharFile());
				userMap.put("panFile", users.get(i).getPanFile());
				userMap.put("resumeFile", users.get(i).getResumeFile());
				userMap.put("paySlipFile", users.get(i).getPaySlipFile());
				userMap.put("bankStatementFile", users.get(i).getBankStatementFile());

				userList.add(userMap);

			}
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("currentPage", page);
			pageContent.put("pageSize", pageUsers.getSize());
			pageContent.put("totalPages", pageUsers.getTotalPages());
			pageContent.put("totalElements", pageUsers.getTotalElements());
			pageContent.put("sortDirection", DIR);
			Map<String, Object> response = new HashMap<>();
			response.put("data", userList);
			response.put("pagination", pageContent);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Autowired
	private UserFileStorageService userFileStorageService;

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM')")
	List<Map<String, Object>> saveUser(@RequestPart(name = "body", required = false) User user,
			@RequestPart(name = "gstFile", required = false) MultipartFile aadharFile,
			@RequestPart(name = "panFile", required = false) MultipartFile panFile,
			@RequestPart(name = "companyCertificateFile", required = false) MultipartFile resumeFile,
			@RequestPart(name = "paySlipFile", required = false) MultipartFile paySlipFile,
			@RequestPart(name = "bankStatmentFile", required = false) MultipartFile bankStatementFile) {
		// Distributor distributor = new Distributor();
		LocalDateTime createDateTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		LocalDateTime updatedDateTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		user.setCreateDate(createDateTime);
		user.setUpdatedDateTime(updatedDateTime);
		user.setApprovalStatus("Pending");

		if (user.getRegionId() != null) {
			Region region = regionRepository.getById(Long.parseLong(user.getRegionId().toString()));
			user.setRegion(region);
		}
		if (user.getStateId() != null) {
			State state = stateRepository.getById(Long.parseLong(user.getStateId().toString()));
			user.setState(state);
		}
		if (user.getCityId() != null) {
			City city = cityRepository.getById(Long.parseLong(user.getCityId().toString()));
			user.setCity(city);
		}
		if (user.getDistrictId() != null) {
			District district = districtRepository.getById(Long.parseLong(user.getDistrictId()));
			user.setDistrict(district);
		}
		if (user.getAreaId() != null) {
			Area area = areaRepository.getById(Long.parseLong(user.getAreaId()));
			user.setArea(area);
		}
		if (user.getHqId() != null) {
			HqMaster hqMaster = hqRepository.getById(Long.parseLong(user.getHqId()));
			user.setHqMaster(hqMaster);
		}
		if (user.getRsmId() != null) {
			User rsm = hqUserRepository.getById(Long.parseLong(user.getRsmId()));
			user.setRsm(rsm);
		}
		if (user.getAsmId() != null) {
			User asm = hqUserRepository.getById(Long.parseLong(user.getAsmId()));
			user.setAsm(asm);
		}
		if (user.getAseId() != null) {
			User ase = hqUserRepository.getById(Long.parseLong(user.getAseId()));
			user.setAse(ase);
		}

		// Aadhar File
		if (aadharFile != null) {
			UserFileDB aadharFileDB = new UserFileDB();
			String aadharFileName = StringUtils.cleanPath(aadharFile.getOriginalFilename());
			aadharFileDB.setName(aadharFileName);
			aadharFileDB.setType(aadharFile.getContentType());
			try {
				aadharFileDB.setData(aadharFile.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userFileStorageService.store(aadharFileDB);
			user.setAadharFile(aadharFileDB);
		}

		// PAN File
		if (panFile != null) {
			UserFileDB panFileDB = new UserFileDB();
			String panFileName = StringUtils.cleanPath(panFile.getOriginalFilename());
			panFileDB.setName(panFileName);
			panFileDB.setType(panFile.getContentType());
			try {
				panFileDB.setData(panFile.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userFileStorageService.store(panFileDB);
			user.setPanFile(panFileDB);
		}

		// Resume File
		if (resumeFile != null) {
			UserFileDB resumeFileDB = new UserFileDB();
			String resumeFileName = StringUtils.cleanPath(resumeFile.getOriginalFilename());
			resumeFileDB.setName(resumeFileName);
			resumeFileDB.setType(resumeFile.getContentType());
			try {
				resumeFileDB.setData(resumeFile.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userFileStorageService.store(resumeFileDB);
			user.setResumeFile(resumeFileDB);
		}
		// Pay Slip File
		if (paySlipFile != null) {
			UserFileDB paySlipFileDB = new UserFileDB();
			String paySlipFileName = StringUtils.cleanPath(paySlipFile.getOriginalFilename());
			paySlipFileDB.setName(paySlipFileName);
			paySlipFileDB.setType(paySlipFile.getContentType());
			try {
				paySlipFileDB.setData(paySlipFile.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userFileStorageService.store(paySlipFileDB);
			user.setPaySlipFile(paySlipFileDB);
		}

		// Bank Statement File
		if (bankStatementFile != null) {
			UserFileDB bankStatementFileDB = new UserFileDB();
			String bankStatementFileName = StringUtils.cleanPath(bankStatementFile.getOriginalFilename());
			bankStatementFileDB.setName(bankStatementFileName);
			bankStatementFileDB.setType(bankStatementFile.getContentType());
			try {
				bankStatementFileDB.setData(bankStatementFile.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userFileStorageService.store(bankStatementFileDB);
			user.setBankStatementFile(bankStatementFileDB);
		}

		userService.saveUser(user);

		if (user.getUserTargetDetailsList() != null) {
			for (Map<String, Object> listMap : user.getUserTargetDetailsList()) {
				UserTargetDetails userTargetDetails = new UserTargetDetails();
				for (Map.Entry<String, Object> entry : listMap.entrySet()) {

					if (entry.getKey() == "hq") {
						userTargetDetails.setHq(entry.getValue().toString());
					}
					if (entry.getKey() == "present") {
						userTargetDetails.setPresent(entry.getValue().toString());
					}
					if (entry.getKey() == "goal") {
						userTargetDetails.setGoal(entry.getValue().toString());
					}
				}
				userTargetDetails.setUser(user);
				userTargetDetailsRepository.save(userTargetDetails);
			}
		}

		// return Object
		List<User> users = hqUserRepository.findWithoutTransientColumns(user.getId());

		List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < users.size(); i++) {
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("id", users.get(i).getId());
			userMap.put("title", users.get(i).getTitle());
			userMap.put("firstName", users.get(i).getFirstName());
			userMap.put("middleName", users.get(i).getMiddleName());
			userMap.put("lastName", users.get(i).getLastName());
			userMap.put("fullName", users.get(i).getFullName());
			userMap.put("login", users.get(i).getLogin());
			userMap.put("gender", users.get(i).getGender());
			userMap.put("maritalStatus", users.get(i).getMaritalStatus());
			userMap.put("birthDate", users.get(i).getBirthDate());
			userMap.put("email", users.get(i).getEmail());
			userMap.put("empCode", users.get(i).getEmpCode());
			userMap.put("team", users.get(i).getTeam());
			userMap.put("companyCode", users.get(i).getCompanyCode());
			userMap.put("grade", users.get(i).getGrade());
			userMap.put("branch", users.get(i).getBranch());
			userMap.put("department", users.get(i).getDepartment());
			userMap.put("processStartDate", users.get(i).getProcessStartDate());
			userMap.put("paymentMode", users.get(i).getPaymentMode());
			userMap.put("region", users.get(i).getRegion());
			userMap.put("state", users.get(i).getState());
			userMap.put("district", users.get(i).getDistrict());
			userMap.put("city", users.get(i).getCity());
			userMap.put("area", users.get(i).getArea());
			userMap.put("status", users.get(i).getStatus());
			userMap.put("hq", users.get(i).getHqMaster());
			userMap.put("role", users.get(i).getRole());
			userMap.put("rsm", users.get(i).getRsm());
			userMap.put("asm", users.get(i).getAsm());
			userMap.put("ase", users.get(i).getAse());
			userMap.put("createDate", users.get(i).getCreateDate());
			userMap.put("inactiveDate", users.get(i).getInactiveDate());
			userMap.put("updatedDateTime", users.get(i).getUpdatedDateTime());
			userMap.put("approvalStatus", users.get(i).getApprovalStatus());

			Long userId = users.get(i).getId();
			List<UserTargetDetails> userTargetDetailsList = new ArrayList<UserTargetDetails>();
			userTargetDetailsList = userTargetDetailsRepository.findByUser(userId);
			List<Map<String, Object>> targetList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < userTargetDetailsList.size(); j++) {
				Map<String, Object> targetMap = new HashMap<String, Object>();
				targetMap.put("hq", userTargetDetailsList.get(j).getHq());
				targetMap.put("present", userTargetDetailsList.get(j).getPresent());
				targetMap.put("goal", userTargetDetailsList.get(j).getGoal());
				targetMap.put("id", userTargetDetailsList.get(j).getId());
				targetList.add(targetMap);
			}
			userMap.put("userTargetDetails", targetList);

			userMap.put("aadharFile", users.get(i).getAadharFile());
			userMap.put("panFile", users.get(i).getPanFile());
			userMap.put("resumeFile", users.get(i).getResumeFile());
			userMap.put("paySlipFile", users.get(i).getPaySlipFile());
			userMap.put("bankStatementFile", users.get(i).getBankStatementFile());

			userList.add(userMap);

		}

		return userList;
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM')")
	List<Map<String, Object>> editUser(@PathVariable Long id, @RequestPart(name = "body", required = false) User user,
			@RequestPart(name = "gstFile", required = false) MultipartFile aadharFile,
			@RequestPart(name = "panFile", required = false) MultipartFile panFile,
			@RequestPart(name = "companyCertificateFile", required = false) MultipartFile resumeFile,
			@RequestPart(name = "paySlipFile", required = false) MultipartFile paySlipFile,
			@RequestPart(name = "bankStatmentFile", required = false) MultipartFile bankStatementFile) {
		User existingUser = userService.getUser(id);
		System.out.println(user.toString());
		LocalDateTime updatedDateTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		existingUser.setUpdatedDateTime(updatedDateTime);

		if (user.getStatus() != null) {
			if (user.getStatus().toString().equals("Inactive")) {
				existingUser.setInactiveDate(updatedDateTime);
			}
		}
		
		if(user.getApprovalStatus() != null) {
			existingUser.setApprovalStatus(user.getApprovalStatus());
		}
		if(user.getRole()!= null) {
			existingUser.setRole(user.getRole());
		}
		if (user.getTitle() != null) {
			existingUser.setTitle(user.getTitle());
		}
		if (user.getFirstName() != null) {
			existingUser.setFirstName(user.getFirstName());
		}
		if (user.getMiddleName() != null) {
			existingUser.setMiddleName(user.getMiddleName());
		}
		if (user.getLastName() != null) {
			existingUser.setLastName(user.getLastName());
		}
		if (user.getFullName() != null) {
			existingUser.setFullName(user.getFullName());
		}
		if (user.getLogin() != null) {
			existingUser.setLogin(user.getLogin());
		}
		if (user.getGender() != null) {
			existingUser.setGender(user.getGender());
		}
		if (user.getMaritalStatus() != null) {
			existingUser.setMaritalStatus(user.getMaritalStatus());
		}
		if (user.getBirthDate() != null) {
			existingUser.setBirthDate(user.getBirthDate());
		}
		if (user.getEmail() != null) {
			existingUser.setEmail(user.getEmail());
		}
		if (user.getEmpCode() != null) {
			existingUser.setEmpCode(user.getEmpCode());
		}
		if (user.getTeam() != null) {
			existingUser.setTeam(user.getTeam());
		}
		if (user.getCompanyCode() != null) {
			existingUser.setCompanyCode(user.getCompanyCode());
		}
		if (user.getGrade() != null) {
			existingUser.setGrade(user.getGrade());
		}
		if (user.getBranch() != null) {
			existingUser.setBranch(user.getBranch());
		}
		if (user.getDepartment() != null) {
			existingUser.setDepartment(user.getDepartment());
		}
		if (user.getProcessStartDate() != null) {
			existingUser.setProcessStartDate(user.getProcessStartDate());
		}
		if (user.getPaymentMode() != null) {
			existingUser.setPaymentMode(user.getPaymentMode());
		}

		if (user.getRegionId() != null) {
			Region region = regionRepository.getById(Long.parseLong(user.getRegionId().toString()));
			existingUser.setRegion(region);
		}
		if (user.getStateId() != null) {
			State state = stateRepository.getById(Long.parseLong(user.getStateId().toString()));
			existingUser.setState(state);
		}
		if (user.getCityId() != null) {
			City city = cityRepository.getById(Long.parseLong(user.getCityId().toString()));
			existingUser.setCity(city);
		}
		if (user.getDistrictId() != null) {
			District district = districtRepository.getById(Long.parseLong(user.getDistrictId()));
			existingUser.setDistrict(district);
		}
		if (user.getAreaId() != null) {
			Area area = areaRepository.getById(Long.parseLong(user.getAreaId()));
			existingUser.setArea(area);
		}
		if (user.getHqId() != null) {
			HqMaster hqMaster = hqRepository.getById(Long.parseLong(user.getHqId()));
			existingUser.setHqMaster(hqMaster);
		}
		if (user.getRsmId() != null) {
			User rsm = hqUserRepository.getById(Long.parseLong(user.getRsmId()));
			existingUser.setRsm(rsm);
		}
		if (user.getAsmId() != null) {
			User asm = hqUserRepository.getById(Long.parseLong(user.getAsmId()));
			existingUser.setAsm(asm);
		}
		if (user.getAseId() != null) {
			User ase = hqUserRepository.getById(Long.parseLong(user.getAseId()));
			existingUser.setAse(ase);
		}

		// Aadhar File
		if (aadharFile != null) {
			if (user.getAadharFile() == null) {
				UserFileDB aadharFileDB = new UserFileDB();
				String aadharFileName = StringUtils.cleanPath(aadharFile.getOriginalFilename());
				aadharFileDB.setName(aadharFileName);
				aadharFileDB.setType(aadharFile.getContentType());
				try {
					aadharFileDB.setData(aadharFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userFileStorageService.store(aadharFileDB);
				existingUser.setAadharFile(aadharFileDB);
			} else {
				UserFileDB aadharFileDB = new UserFileDB();
				String aadharFileName = StringUtils.cleanPath(aadharFile.getOriginalFilename());
				aadharFileDB.setName(aadharFileName);
				aadharFileDB.setType(aadharFile.getContentType());
				try {
					aadharFileDB.setData(aadharFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userFileStorageService.edit(aadharFileDB);
			}

		}

		// PAN File
		if (panFile != null) {
			if (user.getPanFile() == null) {
				UserFileDB panFileDB = new UserFileDB();
				String panFileName = StringUtils.cleanPath(panFile.getOriginalFilename());
				panFileDB.setName(panFileName);
				panFileDB.setType(panFile.getContentType());
				try {
					panFileDB.setData(panFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userFileStorageService.store(panFileDB);
				existingUser.setPanFile(panFileDB);
			} else {
				UserFileDB panFileDB = new UserFileDB();
				String panFileName = StringUtils.cleanPath(panFile.getOriginalFilename());
				panFileDB.setName(panFileName);
				panFileDB.setType(panFile.getContentType());
				try {
					panFileDB.setData(panFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userFileStorageService.edit(panFileDB);
			}
		}

		// Resume File
		if (resumeFile != null) {
			if (user.getResumeFile() == null) {
				UserFileDB resumeFileDB = new UserFileDB();
				String resumeFileName = StringUtils.cleanPath(resumeFile.getOriginalFilename());
				resumeFileDB.setName(resumeFileName);
				resumeFileDB.setType(resumeFile.getContentType());
				try {
					resumeFileDB.setData(resumeFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userFileStorageService.store(resumeFileDB);
				existingUser.setResumeFile(resumeFileDB);
			} else {
				UserFileDB resumeFileDB = new UserFileDB();
				String resumeFileName = StringUtils.cleanPath(resumeFile.getOriginalFilename());
				resumeFileDB.setName(resumeFileName);
				resumeFileDB.setType(resumeFile.getContentType());
				try {
					resumeFileDB.setData(resumeFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userFileStorageService.edit(resumeFileDB);
			}
		}
		// Pay Slip File
		if (paySlipFile != null) {
			if (user.getPaySlipFile() == null) {
				UserFileDB paySlipFileDB = new UserFileDB();
				String paySlipFileName = StringUtils.cleanPath(paySlipFile.getOriginalFilename());
				paySlipFileDB.setName(paySlipFileName);
				paySlipFileDB.setType(paySlipFile.getContentType());
				try {
					paySlipFileDB.setData(paySlipFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userFileStorageService.store(paySlipFileDB);
				existingUser.setPaySlipFile(paySlipFileDB);
			} else {
				UserFileDB paySlipFileDB = new UserFileDB();
				String paySlipFileName = StringUtils.cleanPath(paySlipFile.getOriginalFilename());
				paySlipFileDB.setName(paySlipFileName);
				paySlipFileDB.setType(paySlipFile.getContentType());
				try {
					paySlipFileDB.setData(paySlipFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userFileStorageService.edit(paySlipFileDB);
			}
		}

		// Bank Statement File
		if (bankStatementFile != null) {
			if (user.getBankStatementFile() == null) {
				UserFileDB bankStatementFileDB = new UserFileDB();
				String bankStatementFileName = StringUtils.cleanPath(bankStatementFile.getOriginalFilename());
				bankStatementFileDB.setName(bankStatementFileName);
				bankStatementFileDB.setType(bankStatementFile.getContentType());
				try {
					bankStatementFileDB.setData(bankStatementFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userFileStorageService.store(bankStatementFileDB);
				existingUser.setBankStatementFile(bankStatementFileDB);
			} else {
				UserFileDB bankStatementFileDB = new UserFileDB();
				String bankStatementFileName = StringUtils.cleanPath(bankStatementFile.getOriginalFilename());
				bankStatementFileDB.setName(bankStatementFileName);
				bankStatementFileDB.setType(bankStatementFile.getContentType());
				try {
					bankStatementFileDB.setData(bankStatementFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userFileStorageService.edit(bankStatementFileDB);
			}
		}

		userService.editUser(existingUser);

//		if (user.getUserTargetDetailsList() != null) {
//			for (Map<String, Object> listMap : user.getUserTargetDetailsList()) {
//				UserTargetDetails userTargetDetails = new UserTargetDetails();
//				for (Map.Entry<String, Object> entry : listMap.entrySet()) {
//
//					if (entry.getKey() == "hq") {
//						userTargetDetails.setHq(entry.getValue().toString());
//					}
//					if (entry.getKey() == "present") {
//						userTargetDetails.setPresent(entry.getValue().toString());
//					}
//					if (entry.getKey() == "goal") {
//						userTargetDetails.setGoal(entry.getValue().toString());
//					}
//				}
//				userTargetDetails.setUser(user);
//				userTargetDetailsRepository.save(userTargetDetails);
//			}
//		}

		// return Object
		List<User> users = hqUserRepository.findWithoutTransientColumns(existingUser.getId());

		List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < users.size(); i++) {
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("id", users.get(i).getId());
			userMap.put("title", users.get(i).getTitle());
			userMap.put("firstName", users.get(i).getFirstName());
			userMap.put("middleName", users.get(i).getMiddleName());
			userMap.put("lastName", users.get(i).getLastName());
			userMap.put("fullName", users.get(i).getFullName());
			userMap.put("login", users.get(i).getLogin());
			userMap.put("gender", users.get(i).getGender());
			userMap.put("maritalStatus", users.get(i).getMaritalStatus());
			userMap.put("birthDate", users.get(i).getBirthDate());
			userMap.put("email", users.get(i).getEmail());
			userMap.put("empCode", users.get(i).getEmpCode());
			userMap.put("team", users.get(i).getTeam());
			userMap.put("companyCode", users.get(i).getCompanyCode());
			userMap.put("grade", users.get(i).getGrade());
			userMap.put("branch", users.get(i).getBranch());
			userMap.put("department", users.get(i).getDepartment());
			userMap.put("processStartDate", users.get(i).getProcessStartDate());
			userMap.put("paymentMode", users.get(i).getPaymentMode());
			userMap.put("region", users.get(i).getRegion());
			userMap.put("state", users.get(i).getState());
			userMap.put("district", users.get(i).getDistrict());
			userMap.put("city", users.get(i).getCity());
			userMap.put("area", users.get(i).getArea());
			userMap.put("status", users.get(i).getStatus());
			userMap.put("hq", users.get(i).getHqMaster());
			userMap.put("role", users.get(i).getRole());
			userMap.put("rsm", users.get(i).getRsm());
			userMap.put("asm", users.get(i).getAsm());
			userMap.put("ase", users.get(i).getAse());
			userMap.put("createDate", users.get(i).getCreateDate());
			userMap.put("inactiveDate", users.get(i).getInactiveDate());
			userMap.put("updatedDateTime", users.get(i).getUpdatedDateTime());
			userMap.put("approvalStatus", users.get(i).getApprovalStatus());

			Long userId = users.get(i).getId();
			List<UserTargetDetails> userTargetDetailsList = new ArrayList<UserTargetDetails>();
			userTargetDetailsList = userTargetDetailsRepository.findByUser(userId);
			List<Map<String, Object>> targetList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < userTargetDetailsList.size(); j++) {
				Map<String, Object> targetMap = new HashMap<String, Object>();
				targetMap.put("hq", userTargetDetailsList.get(j).getHq());
				targetMap.put("present", userTargetDetailsList.get(j).getPresent());
				targetMap.put("goal", userTargetDetailsList.get(j).getGoal());
				targetMap.put("id", userTargetDetailsList.get(j).getId());
				targetList.add(targetMap);
			}
			userMap.put("userTargetDetails", targetList);

			userMap.put("aadharFile", users.get(i).getAadharFile());
			userMap.put("panFile", users.get(i).getPanFile());
			userMap.put("resumeFile", users.get(i).getResumeFile());
			userMap.put("paySlipFile", users.get(i).getPaySlipFile());
			userMap.put("bankStatementFile", users.get(i).getBankStatementFile());

			userList.add(userMap);

		}

		return userList;
	}

	@GetMapping("/approve/{id}")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String approveUser(@PathVariable Long id) {
		Long uID = id;
		System.out.println(uID);
		String approved = "Approved";
		hqUserRepository.updateByStatus(approved, uID);
		return "redirect:/user";
	}

	@GetMapping("/reject/{id}")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String rejectUser(@PathVariable Long id) {
		Long uID = id;
		System.out.println(uID);
		String approved = "Rejected";
		hqUserRepository.updateByStatus(approved, uID);
		return "redirect:/user";
	}
}
