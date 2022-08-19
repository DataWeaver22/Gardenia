package com.example.demo.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Distributor;
import com.example.demo.repository.DistRepository;
import com.example.demo.service.DistributorService;

@Service
public class DistServiceImpl implements DistributorService{
	
	private DistRepository distRepository;
	
	public DistServiceImpl(DistRepository distRepository) {
		super();
		this.distRepository = distRepository;
	}
	
	@Override
	public List<Distributor> getAllDistributor(){
		return distRepository.findAll();
	}
	
	@Override
	public Distributor saveDistributor(Distributor distributor) {
		return distRepository.save(distributor);
	}
	
	@Override
	public Distributor getDistributor(Long id) {
		return distRepository.findById(id).get();
	}

	@Override
	public Distributor editDistributor(Distributor distributor) {
		return distRepository.save(distributor);
	}

	@Override
	public void deleteDistributorById(Long id) {
		// TODO Auto-generated method stub
		distRepository.deleteById(id);
	}
	
	
public Distributor getFile(Long fileId) {
    return distRepository.findById(fileId)
        .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
}

@Override
public Distributor getFile(String fileName) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Distributor storeFile(MultipartFile file) {
	// TODO Auto-generated method stub
	return null;
}

	
}
