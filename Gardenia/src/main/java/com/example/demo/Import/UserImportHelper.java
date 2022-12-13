package com.example.demo.Import;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
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
			"State Name", "Region Name", "District Name", "City Name", "Area Name", "HQ Name", "RSM Name", "ASM Name",
			"ASE Name" };
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

				while (cells.hasNext()) {
					Cell cell = cells.next();

					switch (cid) {
					case 0:
						user.setTitle(cell.getStringCellValue());
						break;
					case 1:
						user.setFirstName(cell.getStringCellValue());
						break;
					case 2:
						user.setMiddleName(cell.getStringCellValue());
						break;
					case 3:
						user.setLastName(cell.getStringCellValue());
						break;
					case 4:
						user.setFullName(cell.getStringCellValue());
						break;
					case 5:
						user.setEmpCode(cell.getStringCellValue());
						break;
					case 6:
						//user.setBirthDate(new LocalDate(cell.getStringCellValue()));
						break;
					case 7:
						user.setBranch(cell.getStringCellValue());
						break;
					case 8:
						user.setCompanyCode(cell.getStringCellValue());
						break;
					case 9:
						user.setDepartment(cell.getStringCellValue());
						break;
					case 10:
						user.setEmail(cell.getStringCellValue());
						break;
					case 11:
						user.setGender(cell.getStringCellValue());
						break;
					case 12:
						user.setGrade(cell.getStringCellValue());
						break;
					case 13:
						//user.setJoinDate(cell.getStringCellValue());
						break;
					case 14:
						user.setLogin(cell.getStringCellValue());
						break;
					case 15:
						user.setMaritalStatus(cell.getStringCellValue());
						break;
					case 16:
						user.setPaymentMode(cell.getStringCellValue());
						break;
					case 17:
						//user.setProcessStartDate(new LocalDate(cell.getStringCellValue()));
						break;
					case 18:
						//user.setresignDate(cell.getStringCellValue());
						break;
					case 19:
						user.setRole(cell.getStringCellValue());
						break;
					case 20:
						user.setStatus(cell.getStringCellValue());
						break;
					case 21:
						user.setTeam(cell.getStringCellValue());
						break;
					case 22:
						if(cell.getStringCellValue() != null) {
							String sName = cell.getStringCellValue();
							Long sId = userRepository.findByState(sName);
							State state = stateService.getStateById(sId);
							user.setState(state);
						}
						break;
					case 23:
						if(cell.getStringCellValue() != null) {
							String rName = cell.getStringCellValue();
							Long rId = userRepository.findByRegion(rName);
							Region region = regionService.getRegionById(rId);
							user.setRegion(region);
						}
						break;
					case 24:
						if(cell.getStringCellValue() != null) {
							String dName = cell.getStringCellValue();
							Long dId = userRepository.findByDistrict(dName);
							District district = districtService.getDistrictById(dId);
							user.setDistrict(district);
						}
						break;
					case 25:
						if(cell.getStringCellValue() != null) {
							String cName = cell.getStringCellValue();
							Long cId = userRepository.findByCity(cName);
							City city = cityService.getCityById(cId);
							user.setCity(city);
						}
						break;
					case 26:
						if(cell.getStringCellValue() != null) {
							String aName = cell.getStringCellValue();
							Long aId = userRepository.findByArea(aName);
							Area area = areaService.getAreaById(aId);
							user.setArea(area);
						}
						break;
					case 27:
						if(cell.getStringCellValue() != null) {
							String hqName = cell.getStringCellValue();
							Long hqId = userRepository.findByHq(hqName);
							HqMaster hqMaster = hqService.getHqMaster(hqId);
							user.setHqMaster(hqMaster);
						}
						break;
					case 28:
						if(cell.getStringCellValue() != null) {
							String empCode = cell.getStringCellValue();
							Long uId = userRepository.findByEmpCode(empCode);
							User rsmUser = userService.getUser(uId);
							user.setRsm(rsmUser);
						}
						break;
					case 29:
						if(cell.getStringCellValue() != null) {
							String empCode = cell.getStringCellValue();
							Long uId = userRepository.findByEmpCode(empCode);
							User asmUser = userService.getUser(uId);
							user.setAsm(asmUser);
						}
						break;
					case 30:
						if(cell.getStringCellValue() != null) {
							String empCode = cell.getStringCellValue();
							Long uId = userRepository.findByEmpCode(empCode);
							User aseUser = userService.getUser(uId);
							user.setAse(aseUser);
						}
						break;
					default:
						break;
					}
					cid++;
				}
				list.add(user);

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
}
