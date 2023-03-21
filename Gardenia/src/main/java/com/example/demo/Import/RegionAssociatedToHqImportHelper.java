package com.example.demo.Import;

import java.io.IOException;
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

import com.example.demo.entity.HqMaster;
import com.example.demo.entity.Region;
import com.example.demo.entity.RegionAssociatedToHq;
import com.example.demo.repository.RegionRepository;
import com.example.demo.service.HqService;

@Component
public class RegionAssociatedToHqImportHelper {

	private static RegionRepository regionRepository;

	private static HqService hqService;

	@Autowired
	public RegionAssociatedToHqImportHelper(RegionRepository regionRepository, HqService hqService) {
		super();
		RegionAssociatedToHqImportHelper.regionRepository = regionRepository;
		RegionAssociatedToHqImportHelper.hqService = hqService;
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
	public static List<RegionAssociatedToHq> convertToRegionsHq(InputStream iStream) {
		

		try {
			List<RegionAssociatedToHq> list = new ArrayList<>();
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
				RegionAssociatedToHq regionAssociatedToHq = new RegionAssociatedToHq();

				while (cells.hasNext()) {
					Cell cell = cells.next();

					switch (cid) {
					case 0:
						if (cell.getStringCellValue() != null) {
							String hqName = cell.getStringCellValue();
							System.out.println(hqName);
							Long hqId = regionRepository.findByHq(hqName);
							HqMaster hqMaster = hqService.getHqMaster(hqId);
							regionAssociatedToHq.setHqMaster(hqMaster);
						}
						break;
					case 1:
						if (cell.getStringCellValue() != null) {
							String rName = cell.getStringCellValue();
							System.out.println(rName);
							Long rId = regionRepository.findByRegionName(rName);
							Region region = regionRepository.getById(rId);
							regionAssociatedToHq.setRegion(region);
						}
						break;
					default:
						break;
					}
					cid++;
				}
				list.add(regionAssociatedToHq);

			}
			return list;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
		
	}
}
