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

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;


@Component
public class ProductImportHelper {

private static ProductRepository productRepository;
	
	@Autowired
	public ProductImportHelper(ProductRepository productRepository) {
		super();
		ProductImportHelper.productRepository = productRepository;
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
	public static List<Product> convertToProducts(InputStream iStream){
		List<Product> list = new ArrayList<>();
		
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
				Product product = new Product();
				
				while(cells.hasNext()) {
					Cell cell = cells.next();
					
					switch (cid){
					case 0: 
						product.setPname(cell.getStringCellValue());
						break;
					case 1:
						product.setCode(cell.getStringCellValue());
						break;
					case 2:
						product.setBrand(cell.getStringCellValue());
						break;
					case 3:
						product.setCategory(cell.getStringCellValue());
						break;
					case 4:
						product.setFamily(cell.getStringCellValue());
						break;
					case 5:
						product.setVariant(cell.getStringCellValue());
						break;
					case 6:
						product.setGroup_name(cell.getStringCellValue());
						break;
					case 7:
						product.setUom(cell.getStringCellValue());
						break;
					case 8:
						product.setPtd(cell.getStringCellValue());
						break;
					case 9:
						product.setPtr(cell.getStringCellValue());
						break;
					case 10:
						product.setStatus(cell.getStringCellValue());
						break;
					case 11:
						product.setDescription(cell.getStringCellValue());
						break;
					case 12:
						product.setSales_diary(cell.getStringCellValue());
						break;
					case 13:
						product.setMrp(cell.getStringCellValue());
						break;
					default:
						break;
					}
					cid++;
				}
				list.add(product);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
}
