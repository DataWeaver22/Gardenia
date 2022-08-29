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
import com.example.demo.repository.RegionRepository;

@Component
public class RegionImportHelper {

private static RegionRepository regionRepository;
	
	@Autowired
	public RegionImportHelper(RegionRepository regionRepository) {
		super();
		RegionImportHelper.regionRepository = regionRepository;
	}
	//check if file type is excel or not
	public static boolean checkExcelFormat(MultipartFile file) {
		
		String contentTypeString = file.getContentType();
		if(contentTypeString.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			return true;
		}else {
			return false;
		}
	}
	
	//convert excel to list of states
	public static List<Region> convertToRegions(InputStream iStream){
		List<Region> list = new ArrayList<>();
		
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(iStream);
			XSSFSheet sheet = workbook.getSheet("Sheet1");
			int rowNumber = 0;
			Iterator<Row> iterator = sheet.iterator();
			
			while (iterator.hasNext()) {
				Row row = iterator.next();
				
				if(rowNumber == 0) {
					rowNumber++;
					continue;
				}
				
				Iterator<Cell> cells = row.iterator();
				
				int cid=0;
				Region region = new Region();
				
				while(cells.hasNext()) {
					Cell cell = cells.next();
					
					switch (cid){
					case 0: 
						region.setRegion_code(cell.getStringCellValue());
						break;
					case 1:
						region.setRegion_name(cell.getStringCellValue());
						break;
					case 2:
						region.setState_name(cell.getStringCellValue());
						String sName = cell.getStringCellValue();
						String cId = regionRepository.findByState(sName);
						region.setState_code(cId);
						break;
					default:
						break;
					}
					cid++;
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
