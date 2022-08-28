package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Import.StateHelper;
import com.example.demo.entity.State;
import com.example.demo.repository.StateRepository;

@Service
public class StateImportService {
	
	@Autowired
	private StateRepository stateRepository;
	
	public void save(MultipartFile file) {
		try {
			List<State> states = StateHelper.convertToStates(file.getInputStream());
			this.stateRepository.saveAll(states);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<State> getAllStates(){
		return this.stateRepository.findAll();
	}
}
