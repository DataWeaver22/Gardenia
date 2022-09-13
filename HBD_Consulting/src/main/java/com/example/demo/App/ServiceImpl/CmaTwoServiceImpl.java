package com.example.demo.App.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;


import com.example.demo.App.Entity.CmaTwo;
import com.example.demo.App.Repository.Cma1Repository;
import com.example.demo.App.Repository.CmaTwoRepository;
import com.example.demo.App.Service.Cma1Service;
import com.example.demo.App.Service.CmaTwoService;

@Service
public class CmaTwoServiceImpl implements CmaTwoService{
	
	private CmaTwoRepository cmaTwoRepository;

	public CmaTwoServiceImpl(CmaTwoRepository cmaTwoRepository) {
		super();
		this.cmaTwoRepository = cmaTwoRepository;
	}
	
	
	public List<CmaTwo> getAllCmaTwo(){
		return cmaTwoRepository.findAll();
	}
	
	public CmaTwo saveCmaTwo(CmaTwo cmaTwo) {
		return cmaTwoRepository.save(cmaTwo);
	}
	
	public CmaTwo getCmaTwoById(Long id) {
		return cmaTwoRepository.findById(id).get();
	}
	
	public CmaTwo editCmaTwo(CmaTwo cmaTwo) {
		return cmaTwoRepository.save(cmaTwo);
	}
	
	public void deleteCmaTwoById(Long id) {
		// TODO Auto-generated method stub
		cmaTwoRepository.deleteById(id);
	}
}
