package com.example.demo.Export;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.entity.User;

public class UserExportExcel {
	private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<User> listUsers;
     
    public UserExportExcel(List<User> listUsers) {
        this.listUsers = listUsers;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("User");
    }
 
 
    private void writeHeaderLine() {
        
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "ID", style);      
        createCell(row, 1, "First Name", style);       
        createCell(row, 2, "Last Name", style);    
        createCell(row, 3, "Employee Code", style);
        createCell(row, 4, "Team", style);
        createCell(row, 5, "Role", style);
        createCell(row, 6, "Status", style);
        createCell(row, 7, "Create Date", style);
        createCell(row, 8, "Resign Date", style);
        createCell(row, 9, "Region Name", style);
        createCell(row, 10, "Region Id", style);
        createCell(row, 11, "State Name", style);
        createCell(row, 12, "State Id", style);
        createCell(row, 13, "Area Name", style);
        createCell(row, 14, "Area Id", style);
        createCell(row, 15, "HQ Name", style);
        createCell(row, 16, "HQ Id", style);
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
        }else if (value instanceof LocalDateTime) {
            cell.setCellValue((Calendar) value);;
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
                 
        for (User user : listUsers) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, user.getId(), style);
            createCell(row, columnCount++, user.getFirstName(), style);
            createCell(row, columnCount++, user.getLastName(), style);
            createCell(row, columnCount++, user.getReportingTo(), style);
            createCell(row, columnCount++, user.getRole(), style);
            createCell(row, columnCount++, user.getStatus(), style);
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
