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

import com.example.demo.entity.Region;
import com.example.demo.entity.State;
import com.example.demo.entity.StatesAssociatedToRegion;
import com.example.demo.repository.RegionRepository;
import com.example.demo.service.StateService;

@Component
public class StatesAssociatedToRegionImportHelper {

	private static RegionRepository regionRepository;

	private static StateService stateService;

	@Autowired
	public StatesAssociatedToRegionImportHelper(RegionRepository regionRepository, StateService stateService) {
		super();
		StatesAssociatedToRegionImportHelper.regionRepository = regionRepository;
		StatesAssociatedToRegionImportHelper.stateService = stateService;
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
	public static List<StatesAssociatedToRegion> convertToRegions(InputStream iStream) {
		List<StatesAssociatedToRegion> list = new ArrayList<>();

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
				StatesAssociatedToRegion statesAssociatedToRegion = new StatesAssociatedToRegion();

				while (cells.hasNext()) {
					Cell cell = cells.next();

					switch (cid) {
					case 0:
						if (cell.getStringCellValue() != null) {
							String rName = cell.getStringCellValue();
							System.out.println(rName);
							Long rId = regionRepository.findByRegionName(rName);
							Region region = regionRepository.getById(rId);
							statesAssociatedToRegion.setRegion(region);
						}
						break;
					case 1:
						if (cell.getStringCellValue() != null) {
							String sName = cell.getStringCellValue();
							System.out.println(sName);
							Long sId = regionRepository.findByState(sName);
							State state = stateService.getStateById(sId);
							statesAssociatedToRegion.setState(state);
						}
						break;
					default:
						break;
					}
					cid++;
				}
				list.add(statesAssociatedToRegion);

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
}
