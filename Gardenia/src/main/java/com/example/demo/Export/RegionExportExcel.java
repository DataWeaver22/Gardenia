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
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Region;
import com.example.demo.entity.State;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.StateRepository;



public class RegionExportExcel {
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	  static String[] HEADERs = { "Id", "Region Name", "Region Code","State Name","State ID"};
	  static String SHEET = "Region";
	  private static StateRepository stateRepository;
		
		@Autowired
		public RegionExportExcel(StateRepository stateRepository) {
			super();
			RegionExportExcel.stateRepository = stateRepository;
		}

	  public static ByteArrayInputStream regionToExcel(List<Region> regions) {

	    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
	      Sheet sheet = workbook.createSheet(SHEET);

	      // Header
	      Row headerRow = sheet.createRow(0);

	      for (int col = 0; col < HEADERs.length; col++) {
	        Cell cell = headerRow.createCell(col);
	        cell.setCellValue(HEADERs[col]);
	      }

	      int rowIdx = 1;
	      for (Region region : regions) {
	        Row row = sheet.createRow(rowIdx++);

	        row.createCell(0).setCellValue(region.getId());
	        row.createCell(1).setCellValue(region.getRegionName());
	        row.createCell(2).setCellValue(region.getRegionCode());
	        if(region.getState() == null) {
	        	row.createCell(3).setCellValue("");
		        row.createCell(4).setCellValue("");
	        }else {
		        row.createCell(3).setCellValue(region.getState().getStateName());
		        row.createCell(4).setCellValue(region.getState().getId());
	        }
	        
	      }

	      workbook.write(out);
	      return new ByteArrayInputStream(out.toByteArray());
	    } catch (IOException e) {
	      throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
	    }
	  }
}
