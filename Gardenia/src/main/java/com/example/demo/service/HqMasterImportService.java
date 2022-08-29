package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Import.HqMasterImportHelper;
import com.example.demo.entity.HqMaster;
import com.example.demo.repository.HqRepository;


@Service
public class HqMasterImportService {

	@Autowired
	private HqRepository hqRepository;
	
	public void save(MultipartFile file) {
		try {
			List<HqMaster> hqMasters = HqMasterImportHelper.convertToHqMasters(file.getInputStream());
			this.hqRepository.saveAll(hqMasters);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<HqMaster> getAllHqMasters(){
		return this.hqRepository.findAll();
	}
}
