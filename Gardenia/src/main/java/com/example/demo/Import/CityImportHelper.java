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
import com.example.demo.entity.District;
import com.example.demo.repository.CityRepository;
import com.example.demo.service.DistrictService;

@Component
public class CityImportHelper {

	private static CityRepository cityRepository;

	private static DistrictService districtService;

	@Autowired
	public CityImportHelper(CityRepository cityRepository, DistrictService districtService) {
		super();
		CityImportHelper.cityRepository = cityRepository;
		CityImportHelper.districtService = districtService;
	}

	// check if file type is excel or not
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	
	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	// convert excel to list of states
	public static List<City> convertToCities(InputStream iStream) {
		List<City> list = new ArrayList<>();

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
				City city = new City();

				while (cells.hasNext()) {
					Cell cell = cells.next();

					switch (cid) {
					case 0:
						city.setCityCode(cell.getStringCellValue());
						break;
					case 1:
						city.setCityName(cell.getStringCellValue());
						break;
					case 2:
						if (cell.getStringCellValue() != null) {
							String dName = cell.getStringCellValue();
							System.out.println(cell.getStringCellValue());
							Long dId = cityRepository.findByDistrict(dName);
							District district = districtService.getDistrictById(dId);
							city.setDistrict(district);
						}
						break;
					default:
						break;
					}
					cid++;
				}
				list.add(city);

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

}
