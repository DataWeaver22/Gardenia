package com.example.demo.Import;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Brand;
import com.example.demo.entity.Category;
import com.example.demo.entity.Family;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.BrandService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.FamilyService;

@Component
public class ProductImportHelper {

	private static ProductRepository productRepository;
	private static BrandService brandService;
	private static CategoryService categoryService;
	private static FamilyService familyService;

	@Autowired
	public ProductImportHelper(ProductRepository productRepository,BrandService brandService,CategoryService categoryService,FamilyService familyService) {
		super();
		ProductImportHelper.productRepository = productRepository;
		ProductImportHelper.brandService = brandService;
		ProductImportHelper.categoryService = categoryService;
		ProductImportHelper.familyService = familyService;
	}

	// check if file type is excel or not
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "Product Code", "Product Name", "Brand Name", "Category Name", "Family Name", "Variant",
			"Group Name", "MRP", "PTD", "PTR", "UOM", "Description", "Sales Diary Code", "Status" };
	static String SHEET = "Sheet1";

	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	// convert excel to list of states
	public static List<Product> convertToProducts(InputStream iStream) {
		List<Product> list = new ArrayList<>();

		try {
			XSSFWorkbook workbook = new XSSFWorkbook(iStream);
			XSSFSheet sheet = workbook.getSheet("Sheet1");
			int rowNumber = 0;
			Iterator<Row> iterator = sheet.iterator();
			int count =1;
			while (iterator.hasNext()) {
				Row row = iterator.next();

				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cells = row.iterator();

				int cid = 0;
				Product product = new Product();

				while (cells.hasNext()) {
					Cell cell = cells.next();
					String brandName = row.getCell(2).getStringCellValue();
					String categoryName = row.getCell(3).getStringCellValue();
					String familyName = row.getCell(4).getStringCellValue();
					
					switch (cid) {
					
					case 0:
						DataFormatter formatter = new DataFormatter();
						String val = formatter.formatCellValue(cell);
						//Long codeInteger = Long.parseLong(cell.getNumericCellValue());
						
						System.out.println(val);
						product.setCode(val);
						break;
					case 1:
						product.setPname(cell.getStringCellValue());
						break;
					case 2:
						if(cell.getStringCellValue()!= null) {
							String bName = cell.getStringCellValue();
							Long bId = productRepository.findByBrand(bName);
							Brand brand = brandService.getBrandById(bId);
							product.setBrand(brand);
						}
						break;
					case 3:
						if(cell.getStringCellValue()!= null) {
							String cName = cell.getStringCellValue();
							Long brandId = productRepository.findByBrand(brandName);
							Long cId = productRepository.findByCategory(cName,brandId);
							Category category = categoryService.getCategoryById(cId);
							product.setCategory(category);
						}
						break;
					case 4:
						if(cell.getStringCellValue()!= null) {
							String fName = cell.getStringCellValue();
							Long brandId = productRepository.findByBrand(brandName);
							Long categoryId = productRepository.findByCategory(categoryName, brandId);
							Long fId = productRepository.findByFamily(fName,categoryId);
							Family family = familyService.getFamilyById(fId);
							product.setFamily(family);
						}
						break;
					case 5:
						product.setVariant(cell.getStringCellValue());
						break;
					case 6:
						product.setGroup_name(cell.getStringCellValue());
						break;
					case 7:
						product.setMrp(new BigDecimal(cell.getNumericCellValue()));
						break;
					case 8:
						product.setPtd(new BigDecimal(cell.getNumericCellValue()));
						break;
					case 9:
						product.setPtr(new BigDecimal(cell.getNumericCellValue()));
						break;
					case 10:
						product.setUom(cell.getStringCellValue());
						break;
					case 11:
						product.setDescription(cell.getStringCellValue());
						break;
					case 12:
						DataFormatter formatterSDC = new DataFormatter();
						String valSDC = formatterSDC.formatCellValue(cell);
						product.setSalesDiaryCode(valSDC);
						break;
					case 13:
						product.setStatus(cell.getStringCellValue());
						
						break;
					default:
						break;
					}
					product.setApproval_status("Approved");
					cid++;
				}
				System.out.println(count);
				count++;
				list.add(product);
				

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
}
