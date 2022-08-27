package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.State;


public interface StateExcelDataService {
List<State> getExcelDataAsList();
	
	int saveExcelData(List<State> invoices);
}
