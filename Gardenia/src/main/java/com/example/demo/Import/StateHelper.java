package com.example.demo.Import;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Country;
import com.example.demo.entity.State;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.StateRepository;
import com.example.demo.service.BrandService;
import com.example.demo.service.CountryService;

@Component
public class StateHelper {

	private static StateRepository stateRepository;

	private static CountryService countryService;

	@Autowired
	public StateHelper(StateRepository stateRepository, CountryService countryService) {
		super();
		StateHelper.stateRepository = stateRepository;
		StateHelper.countryService = countryService;
	}

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "State Code", "State Name", "Country Name" };
	static String SHEET = "Sheet1";

	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	public static List<State> excelToStates(InputStream is) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(is);

			XSSFSheet sheet = workbook.getSheet(SHEET);
			Iterator<Row> rows = sheet.iterator();

			List<State> states = new ArrayList<State>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();

				State state = new State();

				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();

					switch (cellIdx) {
					case 0:
						state.setStateCode(currentCell.getStringCellValue());
						break;

					case 1:
						state.setStateName(currentCell.getStringCellValue());
						break;

					case 2:
						if (currentCell.getStringCellValue() != null) {
							String cName = currentCell.getStringCellValue();
							Long cId = stateRepository.findByCountry(cName);
							Country country = countryService.getCountryById(cId);
							state.setCountry(country);
						}
						break;

					default:
						break;
					}

					cellIdx++;
				}

				states.add(state);
				System.out.println(states.size());
			}

			workbook.close();

			return states;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}

}
