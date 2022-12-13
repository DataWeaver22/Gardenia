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

import com.example.demo.entity.Family;

public class FamilyExportExcel {
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	  static String[] HEADERs = { "Id", "Family Name","Category Name","Category ID"};
	  static String SHEET = "Family";

	  public static ByteArrayInputStream familyToExcel(List<Family> families) {

	    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
	      Sheet sheet = workbook.createSheet(SHEET);

	      // Header
	      Row headerRow = sheet.createRow(0);

	      for (int col = 0; col < HEADERs.length; col++) {
	        Cell cell = headerRow.createCell(col);
	        cell.setCellValue(HEADERs[col]);
	      }

	      int rowIdx = 1;
	      for (Family family : families) {
	        Row row = sheet.createRow(rowIdx++);

	        row.createCell(0).setCellValue(family.getId());
	        row.createCell(1).setCellValue(family.getFamilyName());
	        if(family.getCategory() == null) {
	        	row.createCell(2).setCellValue("");
	        	row.createCell(3).setCellValue("");
	        }else {
	        	row.createCell(2).setCellValue(family.getCategory().getCategoryName());
	        	row.createCell(3).setCellValue(family.getCategory().getId());
			}
	        
	      }

	      workbook.write(out);
	      return new ByteArrayInputStream(out.toByteArray());
	    } catch (IOException e) {
	      throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
	    }
	  }
}
