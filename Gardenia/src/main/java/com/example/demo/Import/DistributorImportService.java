package com.example.demo.Import;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Distributor;
import com.example.demo.repository.DistRepository;

@Service
public class DistributorImportService {

	@Autowired
	private DistRepository distRepository;
	
	public void save(MultipartFile file) {
		try {
			List<Distributor> distributors = DistributorImportHelper.convertToDistributors(file.getInputStream());
			this.distRepository.saveAll(distributors);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Distributor> getAllDistributors(){
		return this.distRepository.findAll();
	}
}
