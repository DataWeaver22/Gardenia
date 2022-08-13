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

import com.example.demo.entity.Product;

public class ProductExportExcel {
	private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Product> listUsers;
     
    public ProductExportExcel(List<Product> listUsers) {
        this.listUsers = listUsers;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Product");
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
        createCell(row, 3, "Brand", style);
        createCell(row, 4, "Category", style);
        createCell(row, 5, "Family", style);
        createCell(row, 6, "Variant", style);
        createCell(row, 7, "Group Name", style);
        createCell(row, 8, "UOM", style);
        createCell(row, 9, "PTD", style);
        createCell(row, 10, "PTR", style);
        createCell(row, 11, "Status", style);
        createCell(row, 12, "Description", style);
        createCell(row, 13, "Create Date", style);
        createCell(row, 14, "Inactive Date", style);
        createCell(row, 15, "Sales Diary Code", style);
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
                 
        for (Product user : listUsers) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, user.getId(), style);
            createCell(row, columnCount++, user.getPname(), style);
            createCell(row, columnCount++, user.getCode(), style);
            createCell(row, columnCount++, user.getBrand(), style);
            createCell(row, columnCount++, user.getCategory(), style);
            createCell(row, columnCount++, user.getFamily(), style);
            createCell(row, columnCount++, user.getVariant(), style);
            createCell(row, columnCount++, user.getGroup_name(), style);
            createCell(row, columnCount++, user.getUom(), style);
            createCell(row, columnCount++, user.getPtd(), style);
            createCell(row, columnCount++, user.getPtr(), style);
            createCell(row, columnCount++, user.getStatus(), style);
            createCell(row, columnCount++, user.getDescription(), style);
            createCell(row, columnCount++, user.getCreate_date(), style);
            createCell(row, columnCount++, user.getInactive_date(), style);
            createCell(row, columnCount++, user.getSales_diary(), style);
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
