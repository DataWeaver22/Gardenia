package com.example.demo.Export;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.entity.Distributor;

public class DistributorExportExcel {
	private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Distributor> listUsers;
     
    public DistributorExportExcel(List<Distributor> listUsers) {
        this.listUsers = listUsers;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Distributor");
    }
 
 
    private void writeHeaderLine() {
        
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "ID", style);      
        createCell(row, 1, "Name", style);   
        createCell(row, 2, "Code", style);
        createCell(row, 3, "Type of Distributor", style);
        createCell(row, 4, "GSTIN", style);      
        createCell(row, 5, "PAN", style);   
        createCell(row, 6, "Contact Person ", style);
        createCell(row, 7, "Mobile", style);
        createCell(row, 8, "Phone", style);      
        createCell(row, 9, "Email", style);   
        createCell(row, 10, "Address", style);
        createCell(row, 11, "Region Name", style);
        createCell(row, 12, "Region Id", style);      
        createCell(row, 13, "State Name", style);   
        createCell(row, 14, "State Id", style);
        createCell(row, 15, "City Name", style);
        createCell(row, 16, "City Id", style);
        createCell(row, 17, "Supplier Name", style);
        createCell(row, 18, "Supplier Id", style);
        createCell(row, 19, "Status", style);
        createCell(row, 20, "Create Date", style);
        createCell(row, 21, "Inactive Date", style);
        createCell(row, 22, "Serviced Status", style);
        createCell(row, 23, "Brand List", style);
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
        }else if (value instanceof Long) {
            cell.setCellValue((Date) value);
        }
        else {
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
                 
        for (Distributor user : listUsers) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, user.getId(), style);
            createCell(row, columnCount++, user.getDistributor_name(), style);
            createCell(row, columnCount++, user.getDistributor_code(), style);
            createCell(row, columnCount++, user.getDistributor_type(), style);
            createCell(row, columnCount++, user.getGstin(), style);
            createCell(row, columnCount++, user.getPan(), style);
            createCell(row, columnCount++, user.getContact(), style);
            createCell(row, columnCount++, user.getMobile(), style);
            createCell(row, columnCount++, user.getPhone(), style);
            createCell(row, columnCount++, user.getEmail(), style);
            createCell(row, columnCount++, user.getAddress(), style);
            createCell(row, columnCount++, user.getRegion_name(), style);
            createCell(row, columnCount++, user.getRegion_id(), style);
            createCell(row, columnCount++, user.getState_name(), style);
            createCell(row, columnCount++, user.getState_id(), style);
            createCell(row, columnCount++, user.getCity_name(), style);
            createCell(row, columnCount++, user.getCity_id(), style);
            createCell(row, columnCount++, user.getSupp_name(), style);
            createCell(row, columnCount++, user.getSupp_code(), style);
            createCell(row, columnCount++, user.getStatus(), style);
            createCell(row, columnCount++, user.getCreate_date(), style);
            createCell(row, columnCount++, user.getInactive_date(), style);
            createCell(row, columnCount++, user.getService_status(), style);
            createCell(row, columnCount++, user.getBrand_list(), style);
            
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
