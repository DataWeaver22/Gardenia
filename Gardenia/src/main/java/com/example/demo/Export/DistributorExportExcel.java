package com.example.demo.Export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
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

import com.example.demo.entity.Distributor;
import com.example.demo.entity.Product;

public class DistributorExportExcel {
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "Id", "Distributor Name", "Code", "Type of Distributor", "GSTIN", "PAN",
			"Contact Person", "Mobile", "Phone", "Email", "Address", "Supp Name", "Supp Code", "HQ ID",
			"HQ", "Status", "Service Status", "Approved Status", "Region ID", "Region Name", "State ID",
			"State Name", "City ID", "City Name" };
	static String SHEET = "Distributors";

	public static ByteArrayInputStream distributorToExcel(List<Distributor> distributors) {

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet(SHEET);

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < HEADERs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(HEADERs[col]);
			}

			int rowIdx = 1;
			for (Distributor distributor : distributors) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(distributor.getId());
				row.createCell(1).setCellValue(distributor.getDistributorName());
				row.createCell(2).setCellValue(distributor.getDistributorCode());
				row.createCell(3).setCellValue(distributor.getDistributorType());
				row.createCell(4).setCellValue(distributor.getGstin());
				row.createCell(5).setCellValue(distributor.getPan());
				row.createCell(6).setCellValue(distributor.getContact());
				row.createCell(7).setCellValue(distributor.getMobile());
				row.createCell(8).setCellValue(distributor.getPhone());
				row.createCell(9).setCellValue(distributor.getEmail());
				row.createCell(10).setCellValue(distributor.getBillingAddress());
				row.createCell(11).setCellValue(distributor.getSuppName());
				row.createCell(12).setCellValue(distributor.getSuppCode());
				if (distributor.getHqMaster() == null) {
					row.createCell(13).setCellValue("");
					row.createCell(14).setCellValue("");
				} else {
					row.createCell(13).setCellValue(distributor.getHqMaster().getId());
					row.createCell(14).setCellValue(distributor.getHqMaster().getHqName());
				}
				row.createCell(15).setCellValue(distributor.getStatus());
				row.createCell(16).setCellValue(distributor.getServiceStatus());
				row.createCell(17).setCellValue(distributor.getApprovalStatus());
				if (distributor.getRegion() == null) {
					row.createCell(18).setCellValue("");
					row.createCell(19).setCellValue("");
				} else {
					row.createCell(18).setCellValue(distributor.getRegion().getId());
					row.createCell(19).setCellValue(distributor.getRegion().getRegionName());
				}
				if (distributor.getState() == null) {
					row.createCell(20).setCellValue("");
					row.createCell(21).setCellValue("");
				} else {
					row.createCell(20).setCellValue(distributor.getState().getId());
					row.createCell(21).setCellValue(distributor.getState().getStateName());
				}
				if (distributor.getCity() == null) {
					row.createCell(22).setCellValue("");
					row.createCell(23).setCellValue("");
				} else {
					row.createCell(22).setCellValue(distributor.getCity().getId());
					row.createCell(23).setCellValue(distributor.getCity().getCityName());
				}

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}
}
