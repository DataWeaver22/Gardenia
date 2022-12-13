package com.example.demo.Import;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Area;
import com.example.demo.entity.City;
import com.example.demo.repository.AreaRepository;
import com.example.demo.service.CityService;

@Component
public class AreaImportHelper {

	private static AreaRepository areaRepository;

	private static CityService cityService;

	@Autowired
	public AreaImportHelper(AreaRepository areaRepository, CityService cityService) {
		super();
		AreaImportHelper.areaRepository = areaRepository;
		AreaImportHelper.cityService = cityService;
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
	public static List<Area> convertToAreas(InputStream iStream) {
		List<Area> list = new ArrayList<>();

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
				Area area = new Area();

				while (cells.hasNext()) {
					Cell cell = cells.next();

					switch (cid) {
					case 0:
						area.setAreaCode(cell.getStringCellValue());
						break;
					case 1:
						area.setAreaName(cell.getStringCellValue());
						break;
					case 2:
						if (cell.getCellType() != CellType.BLANK) {
							String cName = cell.getStringCellValue();
							Long cId = areaRepository.findByCity(cName);
							City city = cityService.getCityById(cId);
							area.setCity(city);
						}
						break;
					default:
						break;
					}
					cid++;
				}
				list.add(area);

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

}
