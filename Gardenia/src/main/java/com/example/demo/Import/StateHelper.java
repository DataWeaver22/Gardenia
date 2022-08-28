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

import com.example.demo.entity.State;
import com.example.demo.repository.StateRepository;

@Component
public class StateHelper {
	
	private static StateRepository stateRepository;
	
	@Autowired
	public StateHelper(StateRepository stateRepository) {
		super();
		StateHelper.stateRepository = stateRepository;
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
	public static List<State> convertToStates(InputStream iStream){
		List<State> list = new ArrayList<>();
		
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(iStream);
			XSSFSheet sheet = workbook.getSheet("Country");
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
				State state = new State();
				
				while(cells.hasNext()) {
					Cell cell = cells.next();
					
					switch (cid){
					case 0: 
						state.setState_code(cell.getStringCellValue());
						break;
					case 1:
						state.setState_name(cell.getStringCellValue());
						break;
					case 2:
						state.setCountry_name(cell.getStringCellValue());
						String cName = cell.getStringCellValue();
						String cId = stateRepository.findByCountry(cName);
						state.setCountry_code(cId);
						break;
					default:
						break;
					}
					cid++;
				}
				list.add(state);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	
}
