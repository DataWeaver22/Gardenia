package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Import.StateImportExcel;
import com.example.demo.entity.State;
import com.example.demo.repository.StateRepository;

@Service
public class StateImportExcelService {
	@Autowired
	  StateRepository repository;
	  public void save(MultipartFile file) {
	    try {
	      List<State> tutorials = StateImportExcel.excelToTutorials(file.getInputStream());
	      repository.saveAll(tutorials);
	    } catch (IOException e) {
	      throw new RuntimeException("fail to store excel data: " + e.getMessage());
	    }
	  }
	  public List<State> getAllStates() {
	    return repository.findAll();
	  }
}
