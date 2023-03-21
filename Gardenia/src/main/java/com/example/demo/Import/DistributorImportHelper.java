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

import com.example.demo.entity.City;
import com.example.demo.entity.Distributor;
import com.example.demo.entity.District;
import com.example.demo.entity.HqMaster;
import com.example.demo.entity.Region;
import com.example.demo.entity.State;
import com.example.demo.entity.User;
import com.example.demo.repository.DistRepository;
import com.example.demo.service.CityService;
import com.example.demo.service.DistrictService;
import com.example.demo.service.HqService;
import com.example.demo.service.RegionService;
import com.example.demo.service.StateService;
import com.example.demo.service.UserService;

@Component
public class DistributorImportHelper {

	private static DistRepository distRepository;
	private static RegionService regionService;
	private static StateService stateService;
	private static DistrictService districtService;
	private static CityService cityService;
	private static HqService hqService;

	@Autowired
	public DistributorImportHelper(DistRepository distRepository) {
		super();
		DistributorImportHelper.distRepository = distRepository;
	}

	// check if file type is excel or not
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "Distributor Code", "Distributor Name", "Distributor Type", "Address", "State Name",
			"Region Name", "District Name", "City Name", "HQ Name", "Contact Name", "Email", "GSTIN", "PAN",
			"Phone", "Mobile", "Service Status", "Supp Code", "Supp Name", "Status" };
	static String SHEET = "Sheet1";

	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	// convert excel to list of states
	public static List<Distributor> convertToDistributors(InputStream iStream) {
		List<Distributor> list = new ArrayList<>();

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
				Distributor distributor = new Distributor();

				while (cells.hasNext()) {
					Cell cell = cells.next();

					switch (cid) {
					case 0:
						distributor.setDistributorCode(cell.getStringCellValue());
						break;
					case 1:
						distributor.setDistributorName(cell.getStringCellValue());
						break;
					case 2:
						distributor.setDistributorType(cell.getStringCellValue());
						break;
					case 3:
						distributor.setBillingAddress(cell.getStringCellValue());
						break;
					case 4:
						if(cell.getStringCellValue()!=null) {
							String sName = cell.getStringCellValue();
							Long sId = distRepository.findByState(sName);
							State state = stateService.getStateById(sId);
							distributor.setState(state);
						}
						break;
					case 5:
						if(cell.getStringCellValue()!=null) {
							String rName = cell.getStringCellValue();
							Long rId = distRepository.findByRegion(rName);
							Region region = regionService.getRegionById(rId);
							distributor.setRegion(region);
						}
						break;
					case 6:
						if(cell.getStringCellValue()!=null) {
							String dName = cell.getStringCellValue();
							Long dId = distRepository.findByDistrict(dName);
							District district = districtService.getDistrictById(dId);
							distributor.setDistrict(district);
						}
						break;
					case 7:
						if(cell.getStringCellValue()!=null) {
							String cName = cell.getStringCellValue();
							Long cId = distRepository.findByCity(cName);
							City city = cityService.getCityById(cId);
							distributor.setCity(city);
						}
						break;
					case 8:
						if(cell.getStringCellValue()!=null) {
							String sName = cell.getStringCellValue();
							Long sId = distRepository.findByState(sName);
							State state = stateService.getStateById(sId);
							distributor.setState(state);
						}
						break;
					case 9:
						if(cell.getStringCellValue()!=null) {
							String hqName = cell.getStringCellValue();
							Long hqId = distRepository.findByHq(hqName);
							HqMaster hqMaster = hqService.getHqMaster(hqId);
							distributor.setHqMaster(hqMaster);
						}
						break;
					case 10:
						distributor.setContact(cell.getStringCellValue());
						break;
					case 11:
						distributor.setEmail(cell.getStringCellValue());
						break;
					case 12:
						distributor.setGstin(cell.getStringCellValue());
						break;
					case 13:
						distributor.setPan(cell.getStringCellValue());
						break;
					case 14:
						distributor.setPhone(cell.getStringCellValue());
						break;
					case 15:
						distributor.setMobile(cell.getStringCellValue());
						break;
					case 16:
						distributor.setServiceStatus(cell.getStringCellValue());
						break;
					case 17:
						distributor.setSuppCode(cell.getStringCellValue());
						break;
					case 18:
						distributor.setSuppName(cell.getStringCellValue());
						break;
					case 19:
						distributor.setStatus(cell.getStringCellValue());
						break;
					default:
						break;
					}
					cid++;
				}
				list.add(distributor);

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
}
