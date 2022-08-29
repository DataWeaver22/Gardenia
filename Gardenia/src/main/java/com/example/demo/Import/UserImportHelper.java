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

import com.example.demo.entity.User;
import com.example.demo.repository.HqUserRepository;


@Component
public class UserImportHelper {

private static HqUserRepository userRepository;
	
	@Autowired
	public UserImportHelper(HqUserRepository userRepository) {
		super();
		UserImportHelper.userRepository = userRepository;
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
	public static List<User> convertToUsers(InputStream iStream){
		List<User> list = new ArrayList<>();
		
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
				User user = new User();
				
				while(cells.hasNext()) {
					Cell cell = cells.next();
					
					switch (cid){
					case 0: 
						user.setLogin(cell.getStringCellValue());
						break;
					case 1:
						user.setFirstName(cell.getStringCellValue());
						break;
					case 2:
						user.setLastName(cell.getStringCellValue());
						break;
					case 3:
						user.setEmp_code(cell.getStringCellValue());
						break;
					case 4:
						user.setTeam(cell.getStringCellValue());
						break;
					case 5:
						user.setRoles(cell.getStringCellValue());
						break;
					case 6:
						user.setStatus(cell.getStringCellValue());
						break;
					case 7:
						user.setRegion_name(cell.getStringCellValue());
						String rName = cell.getStringCellValue();
						String rId = userRepository.findByRegion(rName);
						user.setRegion_id(rId);
						break;
					case 8:
						user.setState_name(cell.getStringCellValue());
						String sName = cell.getStringCellValue();
						String sId = userRepository.findByState(sName);
						user.setState_id(sId);
						break;
					case 9:
						user.setArea_name(cell.getStringCellValue());
						String aName = cell.getStringCellValue();
						String aId = userRepository.findByArea(aName);
						user.setArea_id(aId);
						break;
					case 10:
						user.setHq_name(cell.getStringCellValue());
						String hqName = cell.getStringCellValue();
						String hId = userRepository.findByHq(hqName);
						user.setHq_id(hId);
						break;
					default:
						break;
					}
					cid++;
				}
				list.add(user);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
}
