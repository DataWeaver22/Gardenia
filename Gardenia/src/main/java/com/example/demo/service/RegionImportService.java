package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Import.RegionImportHelper;
import com.example.demo.entity.Region;
import com.example.demo.repository.RegionRepository;

@Service
public class RegionImportService {

	@Autowired
	private RegionRepository regionRepository;
	
	public void save(MultipartFile file) {
		try {
			List<Region> regions = RegionImportHelper.convertToRegions(file.getInputStream());
			this.regionRepository.saveAll(regions);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Region> getAllRegions(){
		return this.regionRepository.findAll();
	}
}
