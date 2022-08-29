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

import com.example.demo.entity.HqMaster;
import com.example.demo.repository.HqRepository;


@Component
public class HqMasterImportHelper {

private static HqRepository hqRepository;
	
	@Autowired
	public HqMasterImportHelper(HqRepository hqRepository) {
		super();
		HqMasterImportHelper.hqRepository = hqRepository;
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
	public static List<HqMaster> convertToHqMasters(InputStream iStream){
		List<HqMaster> list = new ArrayList<>();
		
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
				HqMaster hqMaster = new HqMaster();
				
				while(cells.hasNext()) {
					Cell cell = cells.next();
					
					switch (cid){
					case 0: 
						hqMaster.setHq_code(cell.getStringCellValue());
						break;
					case 1:
						hqMaster.setHq_name(cell.getStringCellValue());
						break;
					case 2:
						hqMaster.setHq_designation(cell.getStringCellValue());
						break;
					default:
						break;
					}
					cid++;
				}
				list.add(hqMaster);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

}
