package com.example.demo.Import;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Area;
import com.example.demo.entity.City;
import com.example.demo.entity.District;
import com.example.demo.entity.HqMaster;
import com.example.demo.entity.Region;
import com.example.demo.entity.State;
import com.example.demo.entity.User;
import com.example.demo.repository.HqUserRepository;
import com.example.demo.service.AreaService;
import com.example.demo.service.CityService;
import com.example.demo.service.DistrictService;
import com.example.demo.service.HqService;
import com.example.demo.service.RegionService;
import com.example.demo.service.StateService;
import com.example.demo.service.UserService;

@Component
public class UserImportHelper {

	private static HqUserRepository userRepository;
	private static StateService stateService;
	private static RegionService regionService;
	private static DistrictService districtService;
	private static CityService cityService;
	private static AreaService areaService;
	private static HqService hqService;
	private static UserService userService;

	@Autowired
	public UserImportHelper(HqUserRepository userRepository, StateService stateService, RegionService regionService,
			DistrictService districtService, CityService cityService, AreaService areaService, HqService hqService,
			UserService userService) {
		super();
		UserImportHelper.userRepository = userRepository;
		UserImportHelper.stateService = stateService;
		UserImportHelper.regionService = regionService;
		UserImportHelper.districtService = districtService;
		UserImportHelper.cityService = cityService;
		UserImportHelper.areaService = areaService;
		UserImportHelper.hqService = hqService;
		UserImportHelper.userService = userService;
	}

	// check if file type is excel or not
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "Title", "First Name", "Middle Name", "Last Name", "Full Name", "Emp Code",
			"Birth Date", "Branch", "Company Code", "Department", "Email", "Gender", "Grade", "Join Date", "Login",
			"Marital Status", "Payment Mode", "Process Start Date", "Resign Date", "Role", "Status", "Team",
			"State Name", "Region Name", "District Name", "City Name", "Area Name", "HQ Name" };
	static String SHEET = "Sheet1";

	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	// convert excel to list of states
	public static List<User> convertToUsers(InputStream iStream) {
		List<User> list = new ArrayList<>();
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(iStream);
			XSSFSheet sheet = workbook.getSheet("Sheet1");
			int rowNumber = 0;
			Iterator<Row> iterator = sheet.iterator();

			while (iterator.hasNext()) {
				Row row = iterator.next();

				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cells = row.iterator();

				int cid = 0;
				User user = new User();
				String title = "", firstName = "", middleName = "", lastName = "", fullName = "", empCode = "",
						branch = "", companyCode = "", department = "", email = "", gender = "", grade = "", login = "",
						maritalStatus = "", paymentMode = "", role = "", status = "", reportingTo = "", stateName = "",
						regionName = "", districtName = "", cityName = "", areaName = "", hqName = "";
				LocalDate birthDate = null, joinDate = null, processStartDate = null, resignDate = null;
				State state;
				Region region;
				District district;
				City city;
				Area area;
				HqMaster hqMaster;
				if (row.getCell(0) != null) {
					title = row.getCell(0).getStringCellValue();
					System.out.println(title);
				}
				if (row.getCell(1) != null) {
					firstName = row.getCell(1).getStringCellValue();
					System.out.println(firstName);
				}
				if (row.getCell(2) != null) {
					middleName = row.getCell(2).getStringCellValue();
					System.out.println(middleName);
				}
				if (row.getCell(3) != null) {
					lastName = row.getCell(3).getStringCellValue();
					System.out.println(lastName);
				}
				if (row.getCell(4) != null) {
					fullName = row.getCell(4).getStringCellValue();
				}
				if (row.getCell(5) != null) {
					empCode = row.getCell(5).getStringCellValue();
				}
				if (row.getCell(6) != null) {
					Date birthDateDateTime = row.getCell(6).getDateCellValue();
					birthDate = birthDateDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					System.out.println(birthDate);
				}
				if (row.getCell(7) != null) {
					branch = row.getCell(7).getStringCellValue();
				}
				if (row.getCell(8) != null) {
					companyCode = row.getCell(8).getStringCellValue();
				}
				if (row.getCell(9) != null) {
					department = row.getCell(9).getStringCellValue();
				}
				if (row.getCell(10) != null) {
					email = row.getCell(10).getStringCellValue();
				}
				if (row.getCell(11) != null) {
					gender = row.getCell(11).getStringCellValue();
				}
				if (row.getCell(12) != null) {
					grade = row.getCell(12).getStringCellValue();
				}
				if (row.getCell(13) != null) {
					Date joinDateDate = row.getCell(13).getDateCellValue();
					joinDate = joinDateDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				}
				if (row.getCell(14) != null) {
					login = row.getCell(14).getStringCellValue();
				}
				if (row.getCell(15) != null) {
					maritalStatus = row.getCell(15).getStringCellValue();
				}
				if (row.getCell(16) != null) {
					paymentMode = row.getCell(16).getStringCellValue();
				}
				if (row.getCell(17) != null) {
					Date processStartDateDate = row.getCell(17).getDateCellValue();
					processStartDate = processStartDateDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				}
				if (row.getCell(18) != null) {
					Date resignDateDate = row.getCell(18).getDateCellValue();
					resignDate = resignDateDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				}
				if (row.getCell(19) != null) {
					role = row.getCell(19).getStringCellValue();
				}
				if (row.getCell(20) != null) {
					status = row.getCell(20).getStringCellValue();
				}
				if (row.getCell(21) != null) {
					reportingTo = row.getCell(21).getStringCellValue();
				}
				if (row.getCell(22) != null) {
					stateName = row.getCell(22).getStringCellValue();
					Long sId = userRepository.findByState(stateName);
					state = stateService.getStateById(sId);
					user.setState(state);
				}
				if (row.getCell(23) != null) {
					regionName = row.getCell(23).getStringCellValue();
					System.out.println(regionName +"-" +stateName);
					Long rId = userRepository.findByRegionName(regionName);
					region = regionService.getRegionById(rId);
					user.setRegion(region);
				}
				if (row.getCell(24) != null) {
					districtName = row.getCell(24).getStringCellValue();
					Long dId = userRepository.findByDistrict(districtName);
					district = districtService.getDistrictById(dId);
					user.setDistrict(district);
				}
				if (row.getCell(25) != null) {
					cityName = row.getCell(25).getStringCellValue();
					Long cId = userRepository.findByCity(cityName);
					city = cityService.getCityById(cId);
					user.setCity(city);
				}
				if (row.getCell(26) != null) {
					areaName = row.getCell(26).getStringCellValue();
					Long aId = userRepository.findByArea(areaName);
					area = areaService.getAreaById(aId);
					user.setArea(area);
				}
				if (row.getCell(27) != null) {
					hqName = row.getCell(27).getStringCellValue();
					Long hqId = userRepository.findByHq(hqName);
					hqMaster = hqService.getHqMaster(hqId);
					user.setHqMaster(hqMaster);
				}
				if(role.equals("Territory Sales Officer")) {
					role = "TSO";
				}else if(role.equals("Sales Representative")) {
					role = "SR";
				}else if(role.equals("Sr Territory Sales Officer")) {
					role = "Sr TSO";
				}else if(role.equals("Dy Area Sales Executive")) {
					role = "Dy ASE";
				}else if(role.equals("Area Sales Executive")) {
					role = "ASE";
				}else if(role.equals("Sr Area Sales Executive")) {
					role = "Sr ASE";
				}else if(role.equals("Dy Area Sales Manager")) {
					role = "Dy ASM";
				}else if(role.equals("Area Sales Manager")) {
					role = "ASM";
				}else if(role.equals("Sr Area Sales Manager")) {
					role = "Sr ASM";
				}else if(role.equals("Dy Regional Sales Manager")) {
					role = "Dy RSM";
				}else if(role.equals("Regional Sales Manager")) {
					role = "RSM";
				}else if(role.equals("Sr Regional Sales Manager")) {
					role = "Sr RSM";
				}else if(role.equals("Zone Sales Manager")) {
					role = "ZSM";
				}
				user.setTitle(title);
				user.setFirstName(firstName);
				user.setMiddleName(middleName);
				user.setLastName(lastName);
				user.setFullName(fullName);
				user.setEmpCode(empCode);
				user.setBirthDate(birthDate);
				user.setBranch(branch);
				user.setCompanyCode(companyCode);
				user.setDepartment(department);
				user.setEmail(email);
				user.setGender(gender);
				user.setGrade(grade);
				user.setJoinDate(joinDate);
				user.setLogin(login);
				user.setMaritalStatus(maritalStatus);
				user.setPaymentMode(paymentMode);
				user.setProcessStartDate(processStartDate);
				user.setResignDate(resignDate);
				user.setRole(role);
				user.setStatus(status);
				user.setReportingTo(reportingTo);
				user.setApprovalStatus("Approved");
				System.out.println(user.getApprovalStatus());
				list.add(user);

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
}
