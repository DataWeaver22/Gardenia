package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Import.DistrictImportHelper;
import com.example.demo.entity.District;
import com.example.demo.repository.DistrictRepository;


@Service
public class DistrictImportService {

	@Autowired
	private DistrictRepository districtRepository;
	
	public void save(MultipartFile file) {
		try {
			List<District> districts = DistrictImportHelper.convertToDistricts(file.getInputStream());
			this.districtRepository.saveAll(districts);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<District> getAllDistricts(){
		return this.districtRepository.findAll();
	}
}
