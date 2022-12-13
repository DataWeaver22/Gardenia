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
import com.example.demo.entity.Family;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.FamilyRepository;
import com.example.demo.service.BrandService;
import com.example.demo.service.CategoryService;

@Component
public class FamilyImportHelper {

	private static FamilyRepository familyRepository;

	private static CategoryService categoryService;

	@Autowired
	public FamilyImportHelper(FamilyRepository familyRepository, CategoryService categoryService) {
		super();
		FamilyImportHelper.familyRepository = familyRepository;
		FamilyImportHelper.categoryService = categoryService;
	}

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "Family Name", "Category Name" };
	static String SHEET = "Sheet1";

	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public static List<Family> excelToFamilies(InputStream is) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(is);

			XSSFSheet sheet = workbook.getSheet("Sheet1");
			Iterator<Row> rows = sheet.iterator();

			List<Family> families = new ArrayList<Family>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();

				Family family = new Family();

				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();
					
					String brandName = currentRow.getCell(1).getStringCellValue();
					
					switch (cellIdx) {
					case 0:
						family.setFamilyName(currentCell.getStringCellValue());
						break;
					case 2:
						if (currentCell.getStringCellValue() != null) {
							String cName = currentCell.getStringCellValue();
							System.out.println(brandName);
							Long brandId = familyRepository.findByBrand(brandName);
							Long cId = familyRepository.findByCategory(cName,brandId);
							System.out.println(cId);
							Category category = categoryService.getCategoryById(cId);
							family.setCategory(category);
						}
						break;

					default:
						break;
					}
					LocalDateTime updatedDateTime = LocalDateTime.now();
					family.setUpdatedDateTime(updatedDateTime);
					cellIdx++;
				}
				families.add(family);
			}

			workbook.close();

			return families;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}
}
