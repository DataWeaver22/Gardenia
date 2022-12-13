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

import com.example.demo.entity.District;
import com.example.demo.entity.Region;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.service.RegionService;

@Component
public class DistrictImportHelper {

	private static DistrictRepository districtRepository;

	private static RegionService regionService;

	@Autowired
	public DistrictImportHelper(DistrictRepository districtRepository, RegionService regionService) {
		super();
		DistrictImportHelper.districtRepository = districtRepository;
		DistrictImportHelper.regionService = regionService;
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
	public static List<District> convertToDistricts(InputStream iStream) {
		List<District> list = new ArrayList<>();

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
				District district = new District();

				while (cells.hasNext()) {
					Cell cell = cells.next();

					switch (cid) {
					case 0:
						district.setDistrictCode(cell.getStringCellValue());
						break;
					case 1:
						district.setDistrictName(cell.getStringCellValue());
						break;
					case 2:
						if (cell.getStringCellValue() != null) {
							String rName = cell.getStringCellValue();
							Long rId = districtRepository.findByRegion(rName);
							Region region = regionService.getRegionById(rId);
							district.setRegion(region);
						}
						break;
					default:
						break;
					}
					cid++;
				}
				list.add(district);

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

}
