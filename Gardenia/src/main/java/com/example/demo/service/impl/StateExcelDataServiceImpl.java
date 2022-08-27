package com.example.demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.demo.entity.State;
import com.example.demo.repository.StateRepository;
import com.example.demo.service.StateExcelDataService;

@Service
public class StateExcelDataServiceImpl implements StateExcelDataService{
	@Value("${app.upload.file:${user.home}}")
	public String EXCEL_FILE_PATH;

	@Autowired
	StateRepository stateRepository;

	Workbook workbook;

	public List<State> getExcelDataAsList() {

		List<String> list = new ArrayList<String>();

		// Create a DataFormatter to format and get each cell's value as String
		DataFormatter dataFormatter = new DataFormatter();

		// Create the Workbook
		try {
			workbook = WorkbookFactory.create(new File(EXCEL_FILE_PATH));
		} catch (EncryptedDocumentException | IOException e) {
			e.printStackTrace();
		}

		// Retrieving the number of sheets in the Workbook
		System.out.println("-------Workbook has '" + workbook.getNumberOfSheets() + "' Sheets-----");

		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(0);

		// Getting number of columns in the Sheet
		int noOfColumns = sheet.getRow(0).getLastCellNum();
		System.out.println("-------Sheet has '"+noOfColumns+"' columns------");

		// Using for-each loop to iterate over the rows and columns
		for (Row row : sheet) {
			for (Cell cell : row) {
				String cellValue = dataFormatter.formatCellValue(cell);
				list.add(cellValue);
			}
		}

		// filling excel data and creating list as List<Invoice>
		List<State> invList = createList(list, noOfColumns);

		// Closing the workbook
		try {
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return invList;
	}

	private List<State> createList(List<String> excelData, int noOfColumns) {

		ArrayList<State> invList = new ArrayList<State>();

		int i = noOfColumns;
		do {
			State inv = new State();

			inv.setState_name(excelData.get(i));
			inv.setState_code(excelData.get(i + 1));
			inv.setCountry_name(excelData.get(i + 2));
			inv.setCountry_code(excelData.get(i + 3));

			invList.add(inv);
			i = i + (noOfColumns);

		} while (i < excelData.size());
		return invList;
	}

	@Override
	public int saveExcelData(List<State> invoices) {
		invoices = stateRepository.saveAll(invoices);
		return invoices.size();
	}
}
