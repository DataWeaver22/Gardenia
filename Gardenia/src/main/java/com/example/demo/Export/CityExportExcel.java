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

import com.example.demo.entity.City;
import com.example.demo.entity.District;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.repository.RegionRepository;

public class CityExportExcel {
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	  static String[] HEADERs = { "Id", "City Name", "City Code","District Name","District ID"};
	  static String SHEET = "City";
	  private static DistrictRepository districtRepository;
		
		@Autowired
		public CityExportExcel(DistrictRepository districtRepository) {
			super();
			CityExportExcel.districtRepository = districtRepository;
		}

	  public static ByteArrayInputStream cityToExcel(List<City> cities) {

	    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
	      Sheet sheet = workbook.createSheet(SHEET);

	      // Header
	      Row headerRow = sheet.createRow(0);

	      for (int col = 0; col < HEADERs.length; col++) {
	        Cell cell = headerRow.createCell(col);
	        cell.setCellValue(HEADERs[col]);
	      }

	      int rowIdx = 1;
	      for (City city : cities) {
	        Row row = sheet.createRow(rowIdx++);

	        row.createCell(0).setCellValue(city.getId());
	        row.createCell(1).setCellValue(city.getCityName());
	        row.createCell(2).setCellValue(city.getCityCode());
	        if(city.getDistrict() == null) {
	        	row.createCell(3).setCellValue("");
		        row.createCell(4).setCellValue("");
	        }else {
		        row.createCell(3).setCellValue(city.getDistrict().getDistrictName());
		        row.createCell(4).setCellValue(city.getDistrict().getId());
	        }
	        
	      }

	      workbook.write(out);
	      return new ByteArrayInputStream(out.toByteArray());
	    } catch (IOException e) {
	      throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
	    }
	  }
}
