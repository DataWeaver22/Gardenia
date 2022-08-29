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

import com.example.demo.entity.Distributor;
import com.example.demo.repository.DistRepository;


@Component
public class DistributorImportHelper {

private static DistRepository distRepository;
	
	@Autowired
	public DistributorImportHelper(DistRepository distRepository) {
		super();
		DistributorImportHelper.distRepository = distRepository;
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
	public static List<Distributor> convertToDistributors(InputStream iStream){
		List<Distributor> list = new ArrayList<>();
		
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
				Distributor distributor = new Distributor();
				
				while(cells.hasNext()) {
					Cell cell = cells.next();
					
					switch (cid){
					case 0: 
						distributor.setDistributor_name(cell.getStringCellValue());
						break;
					case 1:
						distributor.setDistributor_code(cell.getStringCellValue());
						break;
					case 2:
						distributor.setDistributor_type(cell.getStringCellValue());
						break;
					case 3: 
						distributor.setGstin(cell.getStringCellValue());
						break;
					case 4:
						distributor.setPan(cell.getStringCellValue());
						break;
					case 5:
						distributor.setContact(cell.getStringCellValue());
						break;
					case 6: 
						distributor.setMobile(cell.getStringCellValue());
						break;
					case 7:
						distributor.setPhone(cell.getStringCellValue());
						break;
					case 8:
						distributor.setEmail(cell.getStringCellValue());
						break;
					case 9: 
						distributor.setAddress(cell.getStringCellValue());
						break;
					case 10:
						distributor.setRegion_name(cell.getStringCellValue());
						String rName = cell.getStringCellValue();
						String rId = distRepository.findByRegion(rName);
						distributor.setRegion_id(rId);
						break;
					case 11:
						distributor.setState_name(cell.getStringCellValue());
						String sName = cell.getStringCellValue();
						String sId = distRepository.findByState(sName);
						distributor.setState_id(sId);
						break;
					case 12:
						distributor.setCity_name(cell.getStringCellValue());
						String cyName = cell.getStringCellValue();
						String cId = distRepository.findByCity(cyName);
						distributor.setCity_id(cId);
						break;
					case 13:
						distributor.setAssign_tso(cell.getStringCellValue());
						String aTSOName = cell.getStringCellValue();
						String aTSOId = distRepository.findByCity(aTSOName);
						distributor.setAssign_tso_id(aTSOId);
						break;
					case 14: 
						distributor.setSupp_name(cell.getStringCellValue());
						break;
					case 15:
						distributor.setSupp_code(cell.getStringCellValue());
						break;
					case 16:
						distributor.setStatus(cell.getStringCellValue());
						break;
					case 17: 
						distributor.setService_status(cell.getStringCellValue());
						break;
					default:
						break;
					}
					cid++;
				}
				list.add(distributor);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
}
