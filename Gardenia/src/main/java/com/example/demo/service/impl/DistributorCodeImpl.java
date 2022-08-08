package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.DistributorCode;
import com.example.demo.repository.DistributorCodeRepository;
import com.example.demo.service.DistributorCodeService;

@Service
public class DistributorCodeImpl implements DistributorCodeService{
	private DistributorCodeRepository distributorCodeRepository;

	public DistributorCodeImpl(DistributorCodeRepository distributorCodeRepository) {
		super();
		this.distributorCodeRepository = distributorCodeRepository;
	}
	
	@Override
	public List<DistributorCode> getAllDistributorCode() {
		return distributorCodeRepository.findAll();
	}
	
	@Override
	public DistributorCode saveDistributorCode(DistributorCode distributorCode) {
		return distributorCodeRepository.save(distributorCode);
	}
	
	@Override
	public DistributorCode getDistributorCodeById(Long id) {
		return distributorCodeRepository.findById(id).get();
	}
	
	@Override
	public DistributorCode editDistributorCode(DistributorCode distributorCode) {
		return distributorCodeRepository.save(distributorCode);
	}

	@Override
	public void deleteDistributorCodeById(Long id) {
		// TODO Auto-generated method stub
		distributorCodeRepository.deleteById(id);
	}
}
