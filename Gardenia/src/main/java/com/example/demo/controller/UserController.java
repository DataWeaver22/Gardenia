package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

import com.example.demo.EmailSenderService.EmailSenderService;
import com.example.demo.Enum.Roles;
import com.example.demo.Enum.Status;
import com.example.demo.Export.CountryExportExcel;
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
import com.example.demo.entity.Country;
import com.example.demo.entity.Distributor;
import com.example.demo.entity.DistributorCode;
import com.example.demo.entity.District;
import com.example.demo.entity.FileDB;
import com.example.demo.entity.HqMaster;
import com.example.demo.entity.Login;
import com.example.demo.entity.RSMAssociatedRegion;
import com.example.demo.entity.Region;
import com.example.demo.entity.RegionAssociatedToHq;
import com.example.demo.entity.State;
import com.example.demo.entity.User;
import com.example.demo.entity.UserFileDB;
import com.example.demo.entity.UserTargetDetails;
import com.example.demo.entity.UserTeam;
import com.example.demo.jwt.JwtController;
import com.example.demo.jwt.JwtRequest;
import com.example.demo.mailExportSheet.EmailTest;
import com.example.demo.message.ErrorMessage;
import com.example.demo.myUserDetailsService.MyUserDetailsService;
import com.example.demo.repository.AreaRepository;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.repository.HqRepository;
import com.example.demo.repository.HqUserRepository;
import com.example.demo.repository.LoginRepository;
import com.example.demo.repository.RSMAssociatedRegionRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.StateRepository;
import com.example.demo.repository.UserFileDBRepository;
import com.example.demo.repository.UserTargetDetailsRepository;
import com.example.demo.repository.UserTeamRepository;
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

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "Title", "First Name", "Middle Name", "Last Name", "Full Name", "Emp Code",
			"Reporting To", "Role", "Region", "State", "District", "City", "Area", "HQ", "Marital Status", "Gender",
			"Aadhar No", "Birth Date", "Join Date", "Grade", "Branch", "Department", "Payment Mode", "Email",
			"Process Start Date", "Company Code", "Last/Current Org. Name", "Last/Current Org. Joining Date",
			"Last/Current Org. Last Date", "Last/Current Org. Designation", "Last/Current Org. Salary",
			"Last/Current Org. Manager Mobile No.", "Last/Current Org. Manager Email ID",
			"Last/Current Org. HR Email ID" };
	static String SHEET = "Sheet1";

	private UserService userService;
	Status status;
	Roles roles;
	String approvalStatusString;

	@Autowired
	private HqUserRepository hqUserRepository;

	@Autowired
	private UserImportService userImportService;

	@Autowired
	private UserTeamRepository userTeamRepository;

	@Autowired
	private CountryRepository countryRepository;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private EmailSenderService emailSenderService;

//	@PostMapping("/mail")
//	@PreAuthorize("hasAuthority('ROLE_USER')")
//	public String email() throws MessagingException {
//		List<Country> countries = countryRepository.findAll();
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		try (Workbook workbook = new XSSFWorkbook(); ) {
//			Sheet sheet = workbook.createSheet(SHEET);
//
//			// Header
//			Row headerRow = sheet.createRow(0);
//
//			for (int col = 0; col < HEADERs.length; col++) {
//				Cell cell = headerRow.createCell(col);
//				cell.setCellValue(HEADERs[col]);
//			}
//
//			int rowIdx = 1;
//			for (Country country : countries) {
//				Row row = sheet.createRow(rowIdx++);
//
//				row.createCell(0).setCellValue(country.getId());
//				row.createCell(1).setCellValue(country.getCountryName());
//				row.createCell(2).setCellValue(country.getCountryCode());
//			}
//
//			workbook.write(out);
//		} catch (IOException e) {
//			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
//		}
//		byte[] excelFileAsBytes = out.toByteArray();
//		ByteArrayResource resource = new ByteArrayResource(excelFileAsBytes);
//		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//		mimeMessageHelper.setFrom("bhavikdesai1717@gmail.com");
//		mimeMessageHelper.setTo("bhavikdesai1710@gmail.com");
//		mimeMessageHelper.setSubject("Test");
//		mimeMessageHelper.setText("Test");
//		mimeMessageHelper.addAttachment("Test.xlsx", resource);
//		javaMailSender.send(mimeMessage);
//		return "Sent";
//	}

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

	@Autowired
	private RSMAssociatedRegionRepository rsmAssociatedRegionRepository;

	@Autowired
	private LoginRepository loginRepository;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_USER','ROLE_RSM')")
	public ResponseEntity<Map<String, Object>> listUser(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "updatedDateTime") String sortBy,
			@RequestParam(defaultValue = "25") Integer pageSize, @RequestParam(defaultValue = "DESC") String DIR,
			@RequestParam(defaultValue = "Approved") Optional<String> userStatus) {

		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName();
			System.out.println(username);
			Login login = loginRepository.findByUsernames(username);
			Long loginId = login.getId();
			List<RSMAssociatedRegion> rsmAssociatedRegionsList = rsmAssociatedRegionRepository.findByLoginId(loginId);
			// System.out.println(rsmAssociatedRegionsList.get(0).getRegion().getRegionName());
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

			Page<User> pageUsers = null;
			List<Long> regionList = new ArrayList<Long>();
			for (int i = 0; i < rsmAssociatedRegionsList.size(); i++) {
				Long regionIdLong = rsmAssociatedRegionsList.get(i).getRegion().getId();
				regionList.add(regionIdLong);
			}
			System.out.println(regionList);
			pageUsers = hqUserRepository.findByUserStatus(userStatus, regionList, paging);
			users = pageUsers.getContent();
			List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
			System.out.println(users.size());
			for (int i = 0; i < users.size(); i++) {
				Map<String, Object> userMap = new HashMap<String, Object>();
				userMap.put("id", users.get(i).getId());
				userMap.put("title", users.get(i).getTitle());
				userMap.put("firstName", users.get(i).getFirstName());
				userMap.put("middleName", users.get(i).getMiddleName());
				userMap.put("lastName", users.get(i).getLastName());
				userMap.put("fullName", users.get(i).getFullName());
				userMap.put("aadharNo", users.get(i).getAadharNo());
				userMap.put("login", users.get(i).getLogin());
				userMap.put("gender", users.get(i).getGender());
				userMap.put("maritalStatus", users.get(i).getMaritalStatus());
				userMap.put("birthDate", users.get(i).getBirthDate());
				userMap.put("email", users.get(i).getEmail());
				userMap.put("empCode", users.get(i).getEmpCode());
				userMap.put("reportingTo", users.get(i).getReportingTo());
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
				userMap.put("lcOrgName", users.get(i).getlcOrgName());
				userMap.put("lcJoiningDate", users.get(i).getlcJoiningDate());
				userMap.put("lcLastDate", users.get(i).getlcLastDate());
				userMap.put("lcOrgDesignation", users.get(i).getlcOrgDesignation());
				userMap.put("lcOrgSalary", users.get(i).getlcOrgSalary());
				userMap.put("lcOrgManagerMobile", users.get(i).getlcOrgManagerMobile());
				userMap.put("lcOrgManagerEmailID", users.get(i).getlcOrgManagerEmailID());
				userMap.put("lcOrgHREmailID", users.get(i).getlcOrgHREmailID());
				userMap.put("dateOfJoining", users.get(i).getDateOfJoining());
				userMap.put("designationRecommended", users.get(i).getDesignationRecommended());
				userMap.put("goSalary", users.get(i).getgoSalary());
				userMap.put("growthPercentage", users.get(i).getGrowthPercentage());
				userMap.put("rejectReason", users.get(i).getRejectReason());

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

				// User Team
				List<UserTeam> userTeamList = new ArrayList<UserTeam>();
				userTeamList = userTeamRepository.findByUser(userId);
				List<Map<String, Object>> teamList = new ArrayList<Map<String, Object>>();
				for (int j = 0; j < userTeamList.size(); j++) {
					Map<String, Object> teamMap = new HashMap<String, Object>();
					teamMap.put("employeeName", userTeamList.get(j).getEmployee().getId());
					teamMap.put("id", userTeamList.get(j).getId());
					teamList.add(teamMap);
				}
				userMap.put("userTeam", teamList);

				userMap.put("aadharFile", users.get(i).getAadharFile());
				userMap.put("panFile", users.get(i).getPanFile());
				userMap.put("resumeFile", users.get(i).getResumeFile());
				userMap.put("paySlipFile", users.get(i).getPaySlipFile());
				userMap.put("bankStatementFile", users.get(i).getBankStatementFile());
				userMap.put("beatPlanFile", users.get(i).getBeatPlanFile());

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
	ResponseEntity<?> saveUser(@RequestPart(name = "body", required = false) User user, HttpServletRequest request,
			@RequestPart(name = "aadharFile", required = false) MultipartFile aadharFile,
			@RequestPart(name = "panFile", required = false) MultipartFile panFile,
			@RequestPart(name = "resumeFile", required = false) MultipartFile resumeFile,
			@RequestPart(name = "paySlipFile", required = false) MultipartFile paySlipFile,
			@RequestPart(name = "bankStatementFile", required = false) MultipartFile bankStatementFile,
			@RequestPart(name = "beatPlanFile", required = false) MultipartFile beatPlanFile)
			throws MessagingException {
		// Distributor distributor = new Distributor();
		LocalDateTime createDateTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		LocalDateTime updatedDateTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		System.out.println(createDateTime);
		user.setCreateDate(createDateTime);
		user.setUpdatedDateTime(updatedDateTime);
		user.setApprovalStatus("Pending");

		if (hqUserRepository.isAadharNoPresent(user.getAadharNo()) > 0) {
			String errorMsg = "Aadhar No: " + user.getAadharNo().toString() + " is already registered";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorMessage(400, errorMsg, "Bad Request", request.getRequestURI()));
		} else if (hqUserRepository.isUserPresent(user.getFullName()) > 0) {
			String errorMsg = "User: " + user.getFullName().toString() + " is already registered";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorMessage(400, errorMsg, "Bad Request", request.getRequestURI()));
		}

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

		// Beat Plan File
		if (beatPlanFile != null) {
			UserFileDB beatPlanFileDB = new UserFileDB();
			String beatPlanFileName = StringUtils.cleanPath(beatPlanFile.getOriginalFilename());
			beatPlanFileDB.setName(beatPlanFileName);
			beatPlanFileDB.setType(beatPlanFile.getContentType());
			try {
				beatPlanFileDB.setData(beatPlanFile.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userFileStorageService.store(beatPlanFileDB);
			user.setBeatPlanFile(beatPlanFileDB);
		}

		userService.saveUser(user);

		if (user.getUserTargetDetails() != null) {
			for (Map<String, Object> listMap : user.getUserTargetDetails()) {
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

		if (user.getUserTeam() != null) {
			for (Map<String, Object> listMap : user.getUserTeam()) {
				UserTeam userTeam = new UserTeam();
				User findUser = userService.getUser(Long.parseLong(listMap.get("employeeName").toString()));
				userTeam.setHqMaster(findUser.getHqMaster());
				userTeam.setEmployee(findUser);
				userTeam.setDesignation(findUser.getRole());
				userTeam.setUser(user);
				userTeamRepository.save(userTeam);
			}
		}

		// return Object
		List<User> users = hqUserRepository.findWithoutTransientColumns(user.getId());

		// Mail

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (Workbook workbook = new XSSFWorkbook();) {
			Sheet sheet = workbook.createSheet(SHEET);

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < HEADERs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(HEADERs[col]);
			}

			int rowIdx = 1;
			for (User mailUser : users) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(mailUser.getTitle());
				row.createCell(1).setCellValue(mailUser.getFirstName());
				row.createCell(2).setCellValue(mailUser.getMiddleName());
				row.createCell(3).setCellValue(mailUser.getLastName());
				row.createCell(4).setCellValue(mailUser.getFullName());
				row.createCell(5).setCellValue(mailUser.getEmpCode());
				row.createCell(6).setCellValue(mailUser.getReportingTo());
				row.createCell(7).setCellValue(mailUser.getRole());
				row.createCell(8).setCellValue(mailUser.getRegion().getRegionName());
				row.createCell(9).setCellValue(mailUser.getState().getStateName());
				row.createCell(10).setCellValue(mailUser.getDistrict().getDistrictName());
				row.createCell(11).setCellValue(mailUser.getCity().getCityName());
				row.createCell(12).setCellValue(mailUser.getArea().getAreaName());
				row.createCell(13).setCellValue(mailUser.getHqMaster().getHqName());
				row.createCell(14).setCellValue(mailUser.getMaritalStatus());
				row.createCell(15).setCellValue(mailUser.getGender());
				row.createCell(16).setCellValue(mailUser.getAadharNo());
				row.createCell(17).setCellValue(mailUser.getBirthDate().toString());
				row.createCell(18).setCellValue(mailUser.getJoinDate().toString());
				row.createCell(19).setCellValue(mailUser.getGrade());
				row.createCell(20).setCellValue(mailUser.getBranch());
				row.createCell(21).setCellValue(mailUser.getDepartment());
				row.createCell(22).setCellValue(mailUser.getPaymentMode());
				row.createCell(23).setCellValue(mailUser.getEmail());
				row.createCell(24).setCellValue(mailUser.getProcessStartDate().toString());
				row.createCell(25).setCellValue(mailUser.getCompanyCode());
				row.createCell(26).setCellValue(mailUser.getlcOrgName());
				row.createCell(27).setCellValue(mailUser.getlcJoiningDate().toString());
				row.createCell(28).setCellValue(mailUser.getlcLastDate().toString());
				row.createCell(29).setCellValue(mailUser.getlcOrgDesignation());
				row.createCell(30).setCellValue(mailUser.getlcOrgSalary());
				row.createCell(31).setCellValue(mailUser.getlcOrgManagerMobile());
				row.createCell(32).setCellValue(mailUser.getlcOrgManagerEmailID());
				row.createCell(33).setCellValue(mailUser.getlcOrgHREmailID());
			}

			workbook.write(out);
		} catch (IOException e) {
			throw new RuntimeException("fail to convert data to Excel file: " + e.getMessage());
		}
		byte[] excelFileAsBytes = out.toByteArray();
		ByteArrayResource resource = new ByteArrayResource(excelFileAsBytes);
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		mimeMessageHelper.setFrom("mis.gcllp@gmail.com");
		mimeMessageHelper.setTo(new String[] { "hr@gardenia.ws", "pratik.babar@gardenia.ws" });
		mimeMessageHelper.setSubject("New User");
		mimeMessageHelper.setText("New User has been Added to Pending State to review.");
		mimeMessageHelper.addAttachment("User.xlsx", resource);
		javaMailSender.send(mimeMessage);

//		for (int i = 0; i < users.size(); i++) {
//			Map<String, Object> userMap = new HashMap<String, Object>();
//			userMap.put("id", users.get(i).getId());
//			userMap.put("title", users.get(i).getTitle());
//			userMap.put("firstName", users.get(i).getFirstName());
//			userMap.put("middleName", users.get(i).getMiddleName());
//			userMap.put("lastName", users.get(i).getLastName());
//			userMap.put("fullName", users.get(i).getFullName());
//			userMap.put("aadharNo", users.get(i).getAadharNo());
//			userMap.put("login", users.get(i).getLogin());
//			userMap.put("gender", users.get(i).getGender());
//			userMap.put("maritalStatus", users.get(i).getMaritalStatus());
//			userMap.put("birthDate", users.get(i).getBirthDate());
//			userMap.put("email", users.get(i).getEmail());
//			userMap.put("empCode", users.get(i).getEmpCode());
//			userMap.put("reportingTo", users.get(i).getReportingTo());
//			userMap.put("companyCode", users.get(i).getCompanyCode());
//			userMap.put("grade", users.get(i).getGrade());
//			userMap.put("branch", users.get(i).getBranch());
//			userMap.put("department", users.get(i).getDepartment());
//			userMap.put("processStartDate", users.get(i).getProcessStartDate());
//			userMap.put("paymentMode", users.get(i).getPaymentMode());
//			userMap.put("region", users.get(i).getRegion());
//			userMap.put("state", users.get(i).getState());
//			userMap.put("district", users.get(i).getDistrict());
//			userMap.put("city", users.get(i).getCity());
//			userMap.put("area", users.get(i).getArea());
//			userMap.put("status", users.get(i).getStatus());
//			userMap.put("hq", users.get(i).getHqMaster());
//			userMap.put("role", users.get(i).getRole());
//			userMap.put("rsm", users.get(i).getRsm());
//			userMap.put("asm", users.get(i).getAsm());
//			userMap.put("ase", users.get(i).getAse());
//			userMap.put("createDate", users.get(i).getCreateDate());
//			userMap.put("inactiveDate", users.get(i).getInactiveDate());
//			userMap.put("updatedDateTime", users.get(i).getUpdatedDateTime());
//			userMap.put("approvalStatus", users.get(i).getApprovalStatus());
//			userMap.put("lcOrgName", users.get(i).getlcOrgName());
//			userMap.put("lcJoiningDate", users.get(i).getlcJoiningDate());
//			userMap.put("lcLastDate", users.get(i).getlcLastDate());
//			userMap.put("lcOrgDesignation", users.get(i).getlcOrgDesignation());
//			userMap.put("lcOrgSalary", users.get(i).getlcOrgSalary());
//			userMap.put("lcOrgManagerMobile", users.get(i).getlcOrgManagerMobile());
//			userMap.put("lcOrgManagerEmailID", users.get(i).getlcOrgManagerEmailID());
//			userMap.put("lcOrgHREmailID", users.get(i).getlcOrgHREmailID());
//			userMap.put("dateOfJoining", users.get(i).getDateOfJoining());
//			userMap.put("designationRecommended", users.get(i).getDesignationRecommended());
//			userMap.put("goSalary", users.get(i).getgoSalary());
//			userMap.put("growthPercentage", users.get(i).getGrowthPercentage());
//
//			Long userId = users.get(i).getId();
//			List<UserTargetDetails> userTargetDetailsList = new ArrayList<UserTargetDetails>();
//			userTargetDetailsList = userTargetDetailsRepository.findByUser(userId);
//			List<Map<String, Object>> targetList = new ArrayList<Map<String, Object>>();
//			for (int j = 0; j < userTargetDetailsList.size(); j++) {
//				Map<String, Object> targetMap = new HashMap<String, Object>();
//				targetMap.put("hq", userTargetDetailsList.get(j).getHq());
//				targetMap.put("present", userTargetDetailsList.get(j).getPresent());
//				targetMap.put("goal", userTargetDetailsList.get(j).getGoal());
//				targetMap.put("id", userTargetDetailsList.get(j).getId());
//				targetList.add(targetMap);
//			}
//			userMap.put("userTargetDetails", targetList);
//
//			// User Team
//			List<UserTeam> userTeamList = new ArrayList<UserTeam>();
//			userTeamList = userTeamRepository.findByUser(userId);
//			List<Map<String, Object>> teamList = new ArrayList<Map<String, Object>>();
//			for (int j = 0; j < userTeamList.size(); j++) {
//				Map<String, Object> teamMap = new HashMap<String, Object>();
//				teamMap.put("employeeName", userTeamList.get(j).getEmployee().getId());
//				teamMap.put("id", userTeamList.get(j).getId());
//				teamList.add(teamMap);
//			}
//			userMap.put("userTeam", teamList);
//
//			userMap.put("aadharFile", users.get(i).getAadharFile());
//			userMap.put("panFile", users.get(i).getPanFile());
//			userMap.put("resumeFile", users.get(i).getResumeFile());
//			userMap.put("paySlipFile", users.get(i).getPaySlipFile());
//			userMap.put("bankStatementFile", users.get(i).getBankStatementFile());
//			userMap.put("beatPlanFile", users.get(i).getBeatPlanFile());
//
//			userList.add(userMap);
//
//		}

//		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//		mimeMessageHelper.setFrom("bhavikdesai1717@gmail.com");
//		mimeMessageHelper.setTo("bhavikdesai1710@gmail.com");
//		mimeMessageHelper.setSubject("Employee");
//		mimeMessageHelper.setText("New User Added in Pending");
//		javaMailSender.send(mimeMessage);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "User Added Successfully", "OK", request.getRequestURI()));
	}

	@Autowired
	private UserFileDBRepository userFileDBRepository;

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM')")
	ResponseEntity<?> editUser(@PathVariable Long id, @RequestPart(name = "body", required = false) User user,
			HttpServletRequest request, @RequestPart(name = "aadharFile", required = false) MultipartFile aadharFile,
			@RequestPart(name = "panFile", required = false) MultipartFile panFile,
			@RequestPart(name = "resumeFile", required = false) MultipartFile resumeFile,
			@RequestPart(name = "paySlipFile", required = false) MultipartFile paySlipFile,
			@RequestPart(name = "bankStatementFile", required = false) MultipartFile bankStatementFile,
			@RequestPart(name = "beatPlanFile", required = false) MultipartFile beatPlanFile) {
		User existingUser = userService.getUser(id);
		Long targetUserId = existingUser.getId();
		System.out.println(user.toString());
		LocalDateTime updatedDateTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		existingUser.setUpdatedDateTime(updatedDateTime);

		if (user.getStatus() != null) {
			if (user.getStatus().toString().equals("Inactive")) {
				existingUser.setInactiveDate(updatedDateTime);
			}
		}

		if (user.getApprovalStatus() != null) {
			existingUser.setApprovalStatus(user.getApprovalStatus());
		}
		if (user.getRole() != null) {
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
		if (user.getAadharNo() != null) {
			existingUser.setAadharNo(user.getAadharNo());
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
		if (user.getReportingTo() != null) {
			existingUser.setReportingTo(user.getReportingTo());
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
		// Last / Current Organisation Detail
		if (user.getlcOrgName() != null) {
			existingUser.setlcOrgName(user.getlcOrgName());
		}
		if (user.getlcJoiningDate() != null) {
			existingUser.setlcJoiningDate(user.getlcJoiningDate());
		}
		if (user.getlcLastDate() != null) {
			existingUser.setlcLastDate(user.getlcLastDate());
		}
		if (user.getlcOrgDesignation() != null) {
			existingUser.setlcOrgDesignation(user.getlcOrgDesignation());
		}
		if (user.getlcOrgSalary() != null) {
			existingUser.setlcOrgSalary(user.getlcOrgSalary());
		}
		if (user.getlcOrgManagerMobile() != null) {
			existingUser.setlcOrgManagerMobile(user.getlcOrgManagerMobile());
		}
		if (user.getlcOrgManagerEmailID() != null) {
			existingUser.setlcOrgManagerEmailID(user.getlcOrgManagerEmailID());
		}
		if (user.getlcOrgHREmailID() != null) {
			existingUser.setlcOrgHREmailID(user.getlcOrgHREmailID());
		}

		// Recommendations
		if (user.getDateOfJoining() != null) {
			existingUser.setDateOfJoining(user.getDateOfJoining());
		}
		if (user.getDesignationRecommended() != null) {
			existingUser.setDesignationRecommended(user.getDesignationRecommended());
		}
		if (user.getgoSalary() != null) {
			existingUser.setgoSalary(user.getgoSalary());
		}
		if (user.getGrowthPercentage() != null) {
			existingUser.setGrowthPercentage(user.getGrowthPercentage());
		}

		// Aadhar File
		if (aadharFile != null) {
			if (existingUser.getAadharFile() == null) {
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
				UserFileDB aadharFileDB = userFileDBRepository.getById(existingUser.getAadharFile().getId());
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
			if (existingUser.getPanFile() == null) {
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
				UserFileDB panFileDB = userFileDBRepository.getById(existingUser.getPanFile().getId());
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
			if (existingUser.getResumeFile() == null) {
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
				UserFileDB resumeFileDB = userFileDBRepository.getById(existingUser.getResumeFile().getId());
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
			if (existingUser.getPaySlipFile() == null) {
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
				UserFileDB paySlipFileDB = userFileDBRepository.getById(existingUser.getPaySlipFile().getId());
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
			if (existingUser.getBankStatementFile() == null) {
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
				UserFileDB bankStatementFileDB = userFileDBRepository
						.getById(existingUser.getBankStatementFile().getId());
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

		// Beat Plan File
		if (beatPlanFile != null) {
			if (existingUser.getBeatPlanFile() == null) {
				UserFileDB beatPlanFileDB = new UserFileDB();
				String beatPlanFileName = StringUtils.cleanPath(beatPlanFile.getOriginalFilename());
				beatPlanFileDB.setName(beatPlanFileName);
				beatPlanFileDB.setType(beatPlanFile.getContentType());
				try {
					beatPlanFileDB.setData(beatPlanFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userFileStorageService.store(beatPlanFileDB);
				existingUser.setBeatPlanFile(beatPlanFileDB);
			} else {
				UserFileDB beatPlanFileDB = userFileDBRepository.getById(existingUser.getBeatPlanFile().getId());
				String beatPlanFileName = StringUtils.cleanPath(beatPlanFile.getOriginalFilename());
				beatPlanFileDB.setName(beatPlanFileName);
				beatPlanFileDB.setType(beatPlanFile.getContentType());
				try {
					beatPlanFileDB.setData(beatPlanFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userFileStorageService.edit(beatPlanFileDB);
			}
		}

		userService.editUser(existingUser);

		// Deleting removed Target
		if (user.getUserTargetDetails() != null) {
			List<UserTargetDetails> userTargetDetailsUpdate = new ArrayList<UserTargetDetails>();
			userTargetDetailsUpdate = userTargetDetailsRepository.findByUser(targetUserId);
			for (int i = 0; i < userTargetDetailsUpdate.size(); i++) {
				Integer count = 0;

				for (Map<String, Object> listMap : user.getUserTargetDetails()) {
					for (Map.Entry<String, Object> entry : listMap.entrySet()) {
						if (entry.getKey() == "id") {
							System.out.println(
									entry.getValue() + " - " + userTargetDetailsUpdate.get(i).getId().toString());
							if (entry.getValue().toString().equals(userTargetDetailsUpdate.get(i).getId().toString())) {
								count += 1;
							}
						}
					}
				}
				if (count <= 0) {
					System.out.println("Delete");
					userTargetDetailsRepository.deleteByUserAndId(targetUserId, userTargetDetailsUpdate.get(i).getId());
				}
			}

			// Upsert Target

			for (Map<String, Object> listMap : user.getUserTargetDetails()) {
				Integer count = 0;
				List<UserTargetDetails> userTargetDetailsUpsert = new ArrayList<UserTargetDetails>();
				userTargetDetailsUpsert = userTargetDetailsRepository.findByUser(targetUserId);
				for (int i = 0; i < userTargetDetailsUpsert.size(); i++) {
					if (listMap.get("id") != null) {
						if (listMap.get("id").toString().equals(userTargetDetailsUpsert.get(i).getId().toString())) {
							count += 1;
						}
					}
				}
				if (count > 0) {
					UserTargetDetails userTargetDetails = userTargetDetailsRepository
							.getById(Long.parseLong(listMap.get("id").toString()));
					userTargetDetails.setHq(listMap.get("hq").toString());
					userTargetDetails.setPresent(listMap.get("present").toString());
					userTargetDetails.setGoal(listMap.get("goal").toString());
					userTargetDetails.setUser(existingUser);
					userTargetDetailsRepository.save(userTargetDetails);
				} else if (count <= 0) {
					UserTargetDetails userTargetDetails = new UserTargetDetails();
					userTargetDetails.setHq(listMap.get("hq").toString());
					userTargetDetails.setPresent(listMap.get("present").toString());
					userTargetDetails.setGoal(listMap.get("goal").toString());
					userTargetDetails.setUser(existingUser);
					userTargetDetailsRepository.save(userTargetDetails);
				}
			}
		} else {
			userTargetDetailsRepository.deleteByUser(targetUserId);
		}

		// Deleting removed Team
		if (user.getUserTeam() != null) {
			List<UserTeam> userTeamUpdate = new ArrayList<UserTeam>();
			userTeamUpdate = userTeamRepository.findByUser(targetUserId);
			for (int i = 0; i < userTeamUpdate.size(); i++) {
				Integer count = 0;

				for (Map<String, Object> listMap : user.getUserTeam()) {
					if (listMap.get("id").toString().equals(userTeamUpdate.get(i).getId().toString())) {
						count += 1;
					}
				}
				if (count <= 0) {
					System.out.println("Delete");
					userTeamRepository.deleteByUserAndId(targetUserId, userTeamUpdate.get(i).getId());
				}
			}

			// Upsert Team

			for (Map<String, Object> listMap : user.getUserTeam()) {
				Integer count = 0;
				List<UserTeam> userTeamUpsert = new ArrayList<UserTeam>();
				System.out.println(targetUserId);
				userTeamUpsert = userTeamRepository.findByUser(targetUserId);
				System.out.println(userTeamUpsert.toString());
				System.out.println(listMap.get("id").toString());
				for (int i = 0; i < userTeamUpsert.size(); i++) {
					if (listMap.get("id") != null) {
						if (listMap.get("id").toString().equals(userTeamUpsert.get(i).getId().toString())) {
							count += 1;
						}
					}
				}
				if (count > 0) {
					UserTeam userTeam = userTeamRepository.getById(Long.parseLong(listMap.get("id").toString()));
					User findUser = userService.getUser(Long.parseLong(listMap.get("employeeName").toString()));
					userTeam.setHqMaster(findUser.getHqMaster());
					userTeam.setEmployee(findUser);
					userTeam.setDesignation(findUser.getRole());
					userTeam.setUser(existingUser);
					userTeamRepository.save(userTeam);
				} else if (count <= 0) {
					UserTeam userTeam = new UserTeam();
					User findUser = userService.getUser(Long.parseLong(listMap.get("employeeName").toString()));
					userTeam.setHqMaster(findUser.getHqMaster());
					userTeam.setEmployee(findUser);
					userTeam.setDesignation(findUser.getRole());
					userTeam.setUser(existingUser);
					userTeamRepository.save(userTeam);
				}
			}
		} else {
			userTeamRepository.deleteByUser(targetUserId);
		}

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
			userMap.put("aadharNo", users.get(i).getAadharNo());
			userMap.put("login", users.get(i).getLogin());
			userMap.put("gender", users.get(i).getGender());
			userMap.put("maritalStatus", users.get(i).getMaritalStatus());
			userMap.put("birthDate", users.get(i).getBirthDate());
			userMap.put("email", users.get(i).getEmail());
			userMap.put("empCode", users.get(i).getEmpCode());
			userMap.put("reportingTo", users.get(i).getReportingTo());
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
			userMap.put("lcOrgName", users.get(i).getlcOrgName());
			userMap.put("lcJoiningDate", users.get(i).getlcJoiningDate());
			userMap.put("lcLastDate", users.get(i).getlcLastDate());
			userMap.put("lcOrgDesignation", users.get(i).getlcOrgDesignation());
			userMap.put("lcOrgSalary", users.get(i).getlcOrgSalary());
			userMap.put("lcOrgManagerMobile", users.get(i).getlcOrgManagerMobile());
			userMap.put("lcOrgManagerEmailID", users.get(i).getlcOrgManagerEmailID());
			userMap.put("lcOrgHREmailID", users.get(i).getlcOrgHREmailID());
			userMap.put("dateOfJoining", users.get(i).getDateOfJoining());
			userMap.put("designationRecommended", users.get(i).getDesignationRecommended());
			userMap.put("goSalary", users.get(i).getgoSalary());
			userMap.put("growthPercentage", users.get(i).getGrowthPercentage());

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

			// User Team
			List<UserTeam> userTeamList = new ArrayList<UserTeam>();
			userTeamList = userTeamRepository.findByUser(userId);
			List<Map<String, Object>> teamList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < userTeamList.size(); j++) {
				Map<String, Object> teamMap = new HashMap<String, Object>();
				teamMap.put("employeeName", userTeamList.get(j).getEmployee().getId());
				teamMap.put("id", userTeamList.get(j).getId());
				teamList.add(teamMap);
			}
			userMap.put("userTeam", teamList);

			userMap.put("aadharFile", users.get(i).getAadharFile());
			userMap.put("panFile", users.get(i).getPanFile());
			userMap.put("resumeFile", users.get(i).getResumeFile());
			userMap.put("paySlipFile", users.get(i).getPaySlipFile());
			userMap.put("bankStatementFile", users.get(i).getBankStatementFile());
			userMap.put("beatPlanFile", users.get(i).getBeatPlanFile());

			userList.add(userMap);

		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "User Edited Successfully", "OK", request.getRequestURI()));
	}

	@PutMapping("/{id}/reapproval")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM')")
	ResponseEntity<?> reApproveUser(@PathVariable Long id, HttpServletRequest request,
			@RequestPart(name = "body", required = false) User user,
			@RequestPart(name = "aadharFile", required = false) MultipartFile aadharFile,
			@RequestPart(name = "panFile", required = false) MultipartFile panFile,
			@RequestPart(name = "resumeFile", required = false) MultipartFile resumeFile,
			@RequestPart(name = "paySlipFile", required = false) MultipartFile paySlipFile,
			@RequestPart(name = "bankStatementFile", required = false) MultipartFile bankStatementFile,
			@RequestPart(name = "beatPlanFile", required = false) MultipartFile beatPlanFile) {
		User existingUser = userService.getUser(id);
		Long targetUserId = existingUser.getId();
		System.out.println(user.toString());
		LocalDateTime updatedDateTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		existingUser.setUpdatedDateTime(updatedDateTime);

		if (user.getStatus() != null) {
			if (user.getStatus().toString().equals("Inactive")) {
				existingUser.setInactiveDate(updatedDateTime);
			}
		}

		existingUser.setApprovalStatus("Pending");
		if (user.getRole() != null) {
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
		if (user.getAadharNo() != null) {
			existingUser.setAadharNo(user.getAadharNo());
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
		if (user.getReportingTo() != null) {
			existingUser.setReportingTo(user.getReportingTo());
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

		// Last / Current Organisation Detail
		if (user.getlcOrgName() != null) {
			existingUser.setlcOrgName(user.getlcOrgName());
		}
		if (user.getlcJoiningDate() != null) {
			existingUser.setlcJoiningDate(user.getlcJoiningDate());
		}
		if (user.getlcLastDate() != null) {
			existingUser.setlcLastDate(user.getlcLastDate());
		}
		if (user.getlcOrgDesignation() != null) {
			existingUser.setlcOrgDesignation(user.getlcOrgDesignation());
		}
		if (user.getlcOrgSalary() != null) {
			existingUser.setlcOrgSalary(user.getlcOrgSalary());
		}
		if (user.getlcOrgManagerMobile() != null) {
			existingUser.setlcOrgManagerMobile(user.getlcOrgManagerMobile());
		}
		if (user.getlcOrgManagerEmailID() != null) {
			existingUser.setlcOrgManagerEmailID(user.getlcOrgManagerEmailID());
		}
		if (user.getlcOrgHREmailID() != null) {
			existingUser.setlcOrgHREmailID(user.getlcOrgHREmailID());
		}

		// Recommendations
		if (user.getDateOfJoining() != null) {
			existingUser.setDateOfJoining(user.getDateOfJoining());
		}
		if (user.getDesignationRecommended() != null) {
			existingUser.setDesignationRecommended(user.getDesignationRecommended());
		}
		if (user.getgoSalary() != null) {
			existingUser.setgoSalary(user.getgoSalary());
		}
		if (user.getGrowthPercentage() != null) {
			existingUser.setGrowthPercentage(user.getGrowthPercentage());
		}

		// Aadhar File
		if (aadharFile != null) {
			if (existingUser.getAadharFile() == null) {
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
				UserFileDB aadharFileDB = userFileDBRepository.getById(existingUser.getAadharFile().getId());
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
			if (existingUser.getPanFile() == null) {
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
				UserFileDB panFileDB = userFileDBRepository.getById(existingUser.getPanFile().getId());
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
			if (existingUser.getResumeFile() == null) {
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
				UserFileDB resumeFileDB = userFileDBRepository.getById(existingUser.getResumeFile().getId());
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
			if (existingUser.getPaySlipFile() == null) {
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
				UserFileDB paySlipFileDB = userFileDBRepository.getById(existingUser.getPaySlipFile().getId());
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
			if (existingUser.getBankStatementFile() == null) {
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
				UserFileDB bankStatementFileDB = userFileDBRepository
						.getById(existingUser.getBankStatementFile().getId());
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

		// Beat Plan File
		if (beatPlanFile != null) {
			if (existingUser.getBeatPlanFile() == null) {
				UserFileDB beatPlanFileDB = new UserFileDB();
				String beatPlanFileName = StringUtils.cleanPath(beatPlanFile.getOriginalFilename());
				beatPlanFileDB.setName(beatPlanFileName);
				beatPlanFileDB.setType(beatPlanFile.getContentType());
				try {
					beatPlanFileDB.setData(beatPlanFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userFileStorageService.store(beatPlanFileDB);
				existingUser.setBeatPlanFile(beatPlanFileDB);
			} else {
				UserFileDB beatPlanFileDB = userFileDBRepository.getById(existingUser.getBeatPlanFile().getId());
				String beatPlanFileName = StringUtils.cleanPath(beatPlanFile.getOriginalFilename());
				beatPlanFileDB.setName(beatPlanFileName);
				beatPlanFileDB.setType(beatPlanFile.getContentType());
				try {
					beatPlanFileDB.setData(beatPlanFile.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userFileStorageService.edit(beatPlanFileDB);
			}
		}

		userService.editUser(existingUser);

		// Deleting removed Target
		if (user.getUserTargetDetails() != null) {
			List<UserTargetDetails> userTargetDetailsUpdate = new ArrayList<UserTargetDetails>();
			userTargetDetailsUpdate = userTargetDetailsRepository.findByUser(targetUserId);
			for (int i = 0; i < userTargetDetailsUpdate.size(); i++) {
				Integer count = 0;

				for (Map<String, Object> listMap : user.getUserTargetDetails()) {
					for (Map.Entry<String, Object> entry : listMap.entrySet()) {
						if (entry.getKey() == "id") {
							System.out.println(
									entry.getValue() + " - " + userTargetDetailsUpdate.get(i).getId().toString());
							if (entry.getValue().toString().equals(userTargetDetailsUpdate.get(i).getId().toString())) {
								count += 1;
							}
						}
					}
				}
				if (count <= 0) {
					System.out.println("Delete");
					userTargetDetailsRepository.deleteByUserAndId(targetUserId, userTargetDetailsUpdate.get(i).getId());
				}
			}

			// Upsert Target

			for (Map<String, Object> listMap : user.getUserTargetDetails()) {
				Integer count = 0;
				List<UserTargetDetails> userTargetDetailsUpsert = new ArrayList<UserTargetDetails>();
				userTargetDetailsUpsert = userTargetDetailsRepository.findByUser(targetUserId);
				for (int i = 0; i < userTargetDetailsUpsert.size(); i++) {
					if (listMap.get("id") != null) {
						if (listMap.get("id").toString().equals(userTargetDetailsUpsert.get(i).getId().toString())) {
							count += 1;
						}
					}
				}
				if (count > 0) {
					UserTargetDetails userTargetDetails = userTargetDetailsRepository
							.getById(Long.parseLong(listMap.get("id").toString()));
					userTargetDetails.setHq(listMap.get("hq").toString());
					userTargetDetails.setPresent(listMap.get("present").toString());
					userTargetDetails.setGoal(listMap.get("goal").toString());
					userTargetDetails.setUser(existingUser);
					userTargetDetailsRepository.save(userTargetDetails);
				} else if (count <= 0) {
					UserTargetDetails userTargetDetails = new UserTargetDetails();
					userTargetDetails.setHq(listMap.get("hq").toString());
					userTargetDetails.setPresent(listMap.get("present").toString());
					userTargetDetails.setGoal(listMap.get("goal").toString());
					userTargetDetails.setUser(existingUser);
					userTargetDetailsRepository.save(userTargetDetails);
				}
			}
		} else {
			userTargetDetailsRepository.deleteByUser(targetUserId);
		}

		// Deleting removed Team
		if (user.getUserTeam() != null) {
			List<UserTeam> userTeamUpdate = new ArrayList<UserTeam>();
			userTeamUpdate = userTeamRepository.findByUser(targetUserId);
			for (int i = 0; i < userTeamUpdate.size(); i++) {
				Integer count = 0;

				for (Map<String, Object> listMap : user.getUserTargetDetails()) {
					if (listMap.get("id").toString().equals(userTeamUpdate.get(i).getId().toString())) {
						count += 1;
					}
				}
				if (count <= 0) {
					System.out.println("Delete");
					userTeamRepository.deleteByUserAndId(targetUserId, userTeamUpdate.get(i).getId());
				}
			}

			// Upsert Team

			for (Map<String, Object> listMap : user.getUserTeam()) {
				Integer count = 0;
				List<UserTeam> userTeamUpsert = new ArrayList<UserTeam>();
				userTeamUpsert = userTeamRepository.findByUser(targetUserId);
				for (int i = 0; i < userTeamUpsert.size(); i++) {
					if (listMap.get("id") != null) {
						if (listMap.get("id").toString().equals(userTeamUpsert.get(i).getId().toString())) {
							count += 1;
						}
					}
				}
				if (count > 0) {
					UserTeam userTeam = userTeamRepository.getById(Long.parseLong(listMap.get("id").toString()));
					User findUser = userService.getUser(Long.parseLong(listMap.get("employeeName").toString()));
					userTeam.setHqMaster(findUser.getHqMaster());
					userTeam.setEmployee(findUser);
					userTeam.setDesignation(findUser.getRole());
					userTeam.setUser(existingUser);
					userTeamRepository.save(userTeam);
				} else if (count <= 0) {
					UserTeam userTeam = new UserTeam();
					User findUser = userService.getUser(Long.parseLong(listMap.get("employeeName").toString()));
					userTeam.setHqMaster(findUser.getHqMaster());
					userTeam.setEmployee(findUser);
					userTeam.setDesignation(findUser.getRole());
					userTeam.setUser(existingUser);
					userTeamRepository.save(userTeam);
				}
			}
		} else {
			userTeamRepository.deleteByUser(targetUserId);
		}

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
			userMap.put("aadharNo", users.get(i).getAadharNo());
			userMap.put("login", users.get(i).getLogin());
			userMap.put("gender", users.get(i).getGender());
			userMap.put("maritalStatus", users.get(i).getMaritalStatus());
			userMap.put("birthDate", users.get(i).getBirthDate());
			userMap.put("email", users.get(i).getEmail());
			userMap.put("empCode", users.get(i).getEmpCode());
			userMap.put("reportingTo", users.get(i).getReportingTo());
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
			userMap.put("lcOrgName", users.get(i).getlcOrgName());
			userMap.put("lcJoiningDate", users.get(i).getlcJoiningDate());
			userMap.put("lcLastDate", users.get(i).getlcLastDate());
			userMap.put("lcOrgDesignation", users.get(i).getlcOrgDesignation());
			userMap.put("lcOrgSalary", users.get(i).getlcOrgSalary());
			userMap.put("lcOrgManagerMobile", users.get(i).getlcOrgManagerMobile());
			userMap.put("lcOrgManagerEmailID", users.get(i).getlcOrgManagerEmailID());
			userMap.put("lcOrgHREmailID", users.get(i).getlcOrgHREmailID());
			userMap.put("dateOfJoining", users.get(i).getDateOfJoining());
			userMap.put("designationRecommended", users.get(i).getDesignationRecommended());
			userMap.put("goSalary", users.get(i).getgoSalary());
			userMap.put("growthPercentage", users.get(i).getGrowthPercentage());

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

			// User Team
			List<UserTeam> userTeamList = new ArrayList<UserTeam>();
			userTeamList = userTeamRepository.findByUser(userId);
			List<Map<String, Object>> teamList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < userTeamList.size(); j++) {
				Map<String, Object> teamMap = new HashMap<String, Object>();
				teamMap.put("employeeName", userTeamList.get(j).getEmployee().getId());
				teamMap.put("id", userTeamList.get(j).getId());
				teamList.add(teamMap);
			}
			userMap.put("userTeam", teamList);

			userMap.put("aadharFile", users.get(i).getAadharFile());
			userMap.put("panFile", users.get(i).getPanFile());
			userMap.put("resumeFile", users.get(i).getResumeFile());
			userMap.put("paySlipFile", users.get(i).getPaySlipFile());
			userMap.put("bankStatementFile", users.get(i).getBankStatementFile());
			userMap.put("beatPlanFile", users.get(i).getBeatPlanFile());

			userList.add(userMap);

		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "User Sent for Re-Approval Successfully", "OK", request.getRequestURI()));
	}

	@GetMapping("/approve/{id}")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<?> approveUser(@PathVariable Long id, HttpServletRequest request) throws MessagingException {
		Long uID = id;
		System.out.println(uID);
		String approved = "Approved";
		hqUserRepository.updateByStatus(approved, uID);
		String userName = "";

		// return Object
		List<User> users = hqUserRepository.findWithoutTransientColumns(uID);

		// Mail

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (Workbook workbook = new XSSFWorkbook();) {
			Sheet sheet = workbook.createSheet(SHEET);

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < HEADERs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(HEADERs[col]);
			}

			int rowIdx = 1;
			for (User mailUser : users) {
				Row row = sheet.createRow(rowIdx++);
				userName = mailUser.getFullName();
				row.createCell(0).setCellValue(mailUser.getTitle());
				row.createCell(1).setCellValue(mailUser.getFirstName());
				row.createCell(2).setCellValue(mailUser.getMiddleName());
				row.createCell(3).setCellValue(mailUser.getLastName());
				row.createCell(4).setCellValue(mailUser.getFullName());
				row.createCell(5).setCellValue(mailUser.getEmpCode());
				row.createCell(6).setCellValue(mailUser.getReportingTo());
				row.createCell(7).setCellValue(mailUser.getRole());
				row.createCell(8).setCellValue(mailUser.getRegion().getRegionName());
				row.createCell(9).setCellValue(mailUser.getState().getStateName());
				row.createCell(10).setCellValue(mailUser.getDistrict().getDistrictName());
				row.createCell(11).setCellValue(mailUser.getCity().getCityName());
				row.createCell(12).setCellValue(mailUser.getArea().getAreaName());
				row.createCell(13).setCellValue(mailUser.getHqMaster().getHqName());
				row.createCell(14).setCellValue(mailUser.getMaritalStatus());
				row.createCell(15).setCellValue(mailUser.getGender());
				row.createCell(16).setCellValue(mailUser.getAadharNo());
				row.createCell(17).setCellValue(mailUser.getBirthDate().toString());
				row.createCell(18).setCellValue(mailUser.getJoinDate().toString());
				row.createCell(19).setCellValue(mailUser.getGrade());
				row.createCell(20).setCellValue(mailUser.getBranch());
				row.createCell(21).setCellValue(mailUser.getDepartment());
				row.createCell(22).setCellValue(mailUser.getPaymentMode());
				row.createCell(23).setCellValue(mailUser.getEmail());
				row.createCell(24).setCellValue(mailUser.getProcessStartDate().toString());
				row.createCell(25).setCellValue(mailUser.getCompanyCode());
				row.createCell(26).setCellValue(mailUser.getlcOrgName());
				row.createCell(27).setCellValue(mailUser.getlcJoiningDate().toString());
				row.createCell(28).setCellValue(mailUser.getlcLastDate().toString());
				row.createCell(29).setCellValue(mailUser.getlcOrgDesignation());
				row.createCell(30).setCellValue(mailUser.getlcOrgSalary());
				row.createCell(31).setCellValue(mailUser.getlcOrgManagerMobile());
				row.createCell(32).setCellValue(mailUser.getlcOrgManagerEmailID());
				row.createCell(33).setCellValue(mailUser.getlcOrgHREmailID());
			}

			workbook.write(out);
		} catch (IOException e) {
			throw new RuntimeException("fail to convert data to Excel file: " + e.getMessage());
		}
		byte[] excelFileAsBytes = out.toByteArray();
		ByteArrayResource resource = new ByteArrayResource(excelFileAsBytes);
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		mimeMessageHelper.setFrom("mis.gcllp@gmail.com");
		mimeMessageHelper.setTo(new String[] { "hr@gardenia.ws","pratik.babar@gardenia.ws", "harish.singh@gardenia.ws" });
		mimeMessageHelper.setCc(new String[] { "mis@gardenia.ws", "bhupendra.singh@gardenia.ws",
				"vishal.thakur@gardenia.ws", "chandrakant@gardenia.ws" });
		mimeMessageHelper.setSubject("User Approval Status");
		mimeMessageHelper.setText("User: " + userName + " has been Approved. Please find the approved User details attachment.");
		mimeMessageHelper.addAttachment("User.xlsx", resource);
		javaMailSender.send(mimeMessage);

//		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//		mimeMessageHelper.setFrom("bhavikdesai1717@gmail.com");
//		mimeMessageHelper.setTo("bhavikdesai1710@gmail.com");
//		mimeMessageHelper.setSubject("Employee");
//		mimeMessageHelper.setText("User Approved");
//		javaMailSender.send(mimeMessage);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "User Approved", "OK", request.getRequestURI()));
	}

	@PostMapping("/reject/{id}")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<?> rejectUser(@PathVariable Long id, @RequestBody Map<String, Object> body,
			HttpServletRequest request) throws MessagingException {
		Long uID = id;
		System.out.println(uID);
		String rejectReason = body.get("rejectReason").toString();
		String approved = "Rejected";
		hqUserRepository.updateByRejectStatus(approved, rejectReason, uID);

//		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//		mimeMessageHelper.setFrom("bhavikdesai1717@gmail.com");
//		mimeMessageHelper.setTo("bhavikdesai1710@gmail.com");
//		mimeMessageHelper.setSubject("Employee");
//		mimeMessageHelper.setText("User Rejected");
//		javaMailSender.send(mimeMessage);abcs

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ErrorMessage(200, "User Rejected", "OK", request.getRequestURI()));
	}
}
