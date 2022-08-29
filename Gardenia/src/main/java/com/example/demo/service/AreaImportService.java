package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Import.AreaImportHelper;
import com.example.demo.entity.Area;
import com.example.demo.repository.AreaRepository;


@Service
public class AreaImportService {

	@Autowired
	private AreaRepository areaRepository;
	
	public void save(MultipartFile file) {
		try {
			List<Area> areas = AreaImportHelper.convertToAreas(file.getInputStream());
			this.areaRepository.saveAll(areas);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Area> getAllAreas(){
		return this.areaRepository.findAll();
	}
}
