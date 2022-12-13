package com.example.demo.Export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.entity.Category;

public class CategoryExportExcel {
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	  static String[] HEADERs = { "Id", "Category Name","Brand Name","Brand ID"};
	  static String SHEET = "Category";

	  public static ByteArrayInputStream categoryToExcel(List<Category> categories) {

	    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
	      Sheet sheet = workbook.createSheet(SHEET);

	      // Header
	      Row headerRow = sheet.createRow(0);

	      for (int col = 0; col < HEADERs.length; col++) {
	        Cell cell = headerRow.createCell(col);
	        cell.setCellValue(HEADERs[col]);
	      }

	      int rowIdx = 1;
	      for (Category category : categories) {
	        Row row = sheet.createRow(rowIdx++);

	        row.createCell(0).setCellValue(category.getId());
	        row.createCell(1).setCellValue(category.getCategoryName());
	        if(category.getBrand() == null) {
	        	row.createCell(2).setCellValue("");
	        	row.createCell(3).setCellValue("");
	        }else {
	        	row.createCell(2).setCellValue(category.getBrand().getBrandName());
	        	row.createCell(3).setCellValue(category.getBrand().getId());
			}
	        
	      }

	      workbook.write(out);
	      return new ByteArrayInputStream(out.toByteArray());
	    } catch (IOException e) {
	      throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
	    }
	  }
}
