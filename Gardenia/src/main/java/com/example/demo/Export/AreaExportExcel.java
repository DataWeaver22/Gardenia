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

import com.example.demo.entity.Area;
import com.example.demo.entity.City;
import com.example.demo.entity.District;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.DistrictRepository;

public class AreaExportExcel {
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	  static String[] HEADERs = { "Id", "Area Name", "Area Code","City Name","City ID"};
	  static String SHEET = "Area";
	  private static CityRepository cityRepository;
		
		@Autowired
		public AreaExportExcel(CityRepository cityRepository) {
			super();
			AreaExportExcel.cityRepository = cityRepository;
		}

	  public static ByteArrayInputStream areaToExcel(List<Area> areas) {

	    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
	      Sheet sheet = workbook.createSheet(SHEET);

	      // Header
	      Row headerRow = sheet.createRow(0);

	      for (int col = 0; col < HEADERs.length; col++) {
	        Cell cell = headerRow.createCell(col);
	        cell.setCellValue(HEADERs[col]);
	      }

	      int rowIdx = 1;
	      for (Area area : areas) {
	        Row row = sheet.createRow(rowIdx++);

	        row.createCell(0).setCellValue(area.getId());
	        row.createCell(1).setCellValue(area.getAreaName());
	        row.createCell(2).setCellValue(area.getAreaCode());
	        if(area.getCity() == null) {
	        	row.createCell(3).setCellValue("");
		        row.createCell(4).setCellValue("");
	        }else {
		        row.createCell(3).setCellValue(area.getCity().getCityName());
		        row.createCell(4).setCellValue(area.getCity().getId());
	        }
	        
	      }

	      workbook.write(out);
	      return new ByteArrayInputStream(out.toByteArray());
	    } catch (IOException e) {
	      throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
	    }
	  }
}
