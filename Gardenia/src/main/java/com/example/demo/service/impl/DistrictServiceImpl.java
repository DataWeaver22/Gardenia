package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.District;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.service.DistrictService;

@Service
public class DistrictServiceImpl implements DistrictService{
	private DistrictRepository districtRepository;

	public DistrictServiceImpl(DistrictRepository districtRepository) {
		super();
		this.districtRepository = districtRepository;
	}

	@Override
	public List<District> getAllDistrict() {
		return districtRepository.findAll();
	}
	
	@Override
	public District saveDistrict(District district) {
		return districtRepository.save(district);
	}
	
	@Override
	public District getDistrictById(Long id) {
		return districtRepository.findById(id).get();
	}
	
	@Override
	public District editDistrict(District district) {
		return districtRepository.save(district);
	}

	@Override
	public void deleteDistrictById(Long id) {
		// TODO Auto-generated method stub
		districtRepository.deleteById(id);
	}

}
