package com.example.demo.Import;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Brand;
import com.example.demo.entity.Category;
import com.example.demo.entity.Region;
import com.example.demo.entity.State;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.BrandService;

@Component
public class CategoryImportHelper {
	
	private static CategoryRepository categoryRepository;

	private static BrandService brandService;

	@Autowired
	public CategoryImportHelper(CategoryRepository categoryRepository, BrandService brandService) {
		super();
		CategoryImportHelper.categoryRepository = categoryRepository;
		CategoryImportHelper.brandService = brandService;
	}
	
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "Category Name","Brand Name" };
	static String SHEET = "Sheet1";

	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public static List<Category> excelToCategories(InputStream is) throws IOException {
		

		try {
			
			XSSFWorkbook workbook = new XSSFWorkbook(is);
			XSSFSheet sheet = workbook.getSheet("Sheet1");
			List<Category> list = new ArrayList<Category>();
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
				Category category = new Category();

				while (cells.hasNext()) {
					Cell cell = cells.next();

					switch (cid) {
					case 0:
						category.setCategoryName(cell.getStringCellValue());
						break;
					case 1:
						if(cell.getStringCellValue()!=null) {
							
							String bName = cell.getStringCellValue();
							System.out.println(categoryRepository.findByBrand(bName));
							Long bId = categoryRepository.findByBrand(bName);
							Brand brand = brandService.getBrandById(bId);
							category.setBrand(brand);
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
				list.add(category);

			}
			workbook.close();
			return list;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
		
	}
}
