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
import com.example.demo.repository.CityRepository;

@Component
public class CityImportHelper {

private static CityRepository cityRepository;
	
	@Autowired
	public CityImportHelper(CityRepository cityRepository) {
		super();
		CityImportHelper.cityRepository = cityRepository;
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
	public static List<City> convertToCities(InputStream iStream){
		List<City> list = new ArrayList<>();
		
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
				City city = new City();
				
				while(cells.hasNext()) {
					Cell cell = cells.next();
					
					switch (cid){
					case 0: 
						city.setCity_code(cell.getStringCellValue());
						break;
					case 1:
						city.setCity_name(cell.getStringCellValue());
						break;
					case 2:
						city.setDistrict_name(cell.getStringCellValue());
						String cName = cell.getStringCellValue();
						String cId = cityRepository.findByDistrict(cName);
						city.setDistrict_code(cId);
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
