package com.example.demo.Export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.entity.Brand;
import com.example.demo.entity.Product;

public class ProductExportExcel {
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "Id", "Product Name", "Code", "Brand Id", "Brand", "Category Id", "Category",
			"Family Id", "Family", "Variant", "Group Name", "UOM", "PTD", "PTR", "Status", "Description",
			"Sales Diary Code", "MRP" };
	static String SHEET = "Products";

	public static ByteArrayInputStream productToExcel(List<Product> products) {

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet(SHEET);

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < HEADERs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(HEADERs[col]);
			}

			int rowIdx = 1;
			for (Product product : products) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(product.getId());
				row.createCell(1).setCellValue(product.getPname());
				row.createCell(2).setCellValue(product.getCode());
				if (product.getBrand() == null) {
					row.createCell(3).setCellValue("");
					row.createCell(4).setCellValue("");
				} else {
					row.createCell(3).setCellValue(product.getBrand().getId());
					row.createCell(4).setCellValue(product.getBrand().getBrandName());
				}
				if (product.getCategory() == null) {
					row.createCell(5).setCellValue("");
					row.createCell(6).setCellValue("");
				} else {
					row.createCell(5).setCellValue(product.getCategory().getId());
					row.createCell(6).setCellValue(product.getCategory().getCategoryName());
				}
				if (product.getFamily() == null) {
					row.createCell(7).setCellValue("");
					row.createCell(8).setCellValue("");
				} else {
					row.createCell(7).setCellValue(product.getFamily().getId());
					row.createCell(8).setCellValue(product.getFamily().getFamilyName());
				}
				row.createCell(9).setCellValue(product.getVariant());
				row.createCell(10).setCellValue(product.getGroup_name());
				row.createCell(11).setCellValue(product.getUom());
				row.createCell(12).setCellValue(new Double(product.getPtd().toString()));
				row.createCell(13).setCellValue(new Double(product.getPtr().toString()));
				row.createCell(14).setCellValue(product.getStatus());
				row.createCell(15).setCellValue(product.getDescription());
				row.createCell(16).setCellValue(product.getSalesDiaryCode());
				row.createCell(17).setCellValue(new Double(product.getMrp().toString()));
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}
}
