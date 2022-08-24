package com.example.demo.Export;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.entity.Area;
import com.example.demo.entity.District;

public class AreaExportExcel {
	private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Area> listUsers;
     
    public AreaExportExcel(List<Area> listUsers) {
        this.listUsers = listUsers;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Area");
    }
 
 
    private void writeHeaderLine() {
        
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "ID", style);      
        createCell(row, 1, "Area Name", style);   
        createCell(row, 2, "Area Code", style);
        createCell(row, 3, "City Name", style);   
        createCell(row, 4, "City Code", style);
        
    }
     
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (Area user : listUsers) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, user.getId(), style);
            createCell(row, columnCount++, user.getArea_name(), style);
            createCell(row, columnCount++, user.getArea_code(), style);
            createCell(row, columnCount++, user.getCity_name(), style);
            createCell(row, columnCount++, user.getCity_code(), style);
         }
    }
     
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
}