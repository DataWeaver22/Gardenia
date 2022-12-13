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
import com.example.demo.repository.RegionRepository;
import com.example.demo.service.StateService;

@Component
public class RegionImportHelper {

	private static RegionRepository regionRepository;

	private static StateService stateService;

	@Autowired
	public RegionImportHelper(RegionRepository regionRepository, StateService stateService) {
		super();
		RegionImportHelper.regionRepository = regionRepository;
		RegionImportHelper.stateService = stateService;
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
	public static List<Region> convertToRegions(InputStream iStream) {
		List<Region> list = new ArrayList<>();

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
				Region region = new Region();

				while (cells.hasNext()) {
					Cell cell = cells.next();

					switch (cid) {
					case 0:
						region.setRegionCode(cell.getStringCellValue());
						break;
					case 1:
						region.setRegionName(cell.getStringCellValue());
						break;
					case 2:
						if (cell.getStringCellValue() != null) {
							String sName = cell.getStringCellValue();
							Long sId = regionRepository.findByState(sName);
							State state = stateService.getStateById(sId);
							region.setState(state);
						}
						break;
					default:
						break;
					}
					cid++;
					
//					regionCodeString = body.get("regionCode").toString();
//					String codeNumberInteger = "000";
//					distributorCode.setCodeNumber(codeNumberInteger);
//					distributorCode.setRegionCode(regionCodeString);
//					distributorCode.setRegion(region);
////					System.out.println(distributorCode);
//					distributorCodeService.saveDistributorCode(distributorCode);
				}
				list.add(region);

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

}
