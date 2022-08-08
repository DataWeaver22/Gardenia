package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Region;
import com.example.demo.repository.RegionRepository;
import com.example.demo.service.RegionService;

@Service
public class RegionServiceImpl implements RegionService{
	private RegionRepository regionRepository;

	public RegionServiceImpl(RegionRepository regionRepository) {
		super();
		this.regionRepository = regionRepository;
	}

	@Override
	public List<Region> getAllRegion() {
		return regionRepository.findAll();
	}
	
	@Override
	public Region saveRegion(Region region) {
		return regionRepository.save(region);
	}
	
	@Override
	public Region getRegionById(Long id) {
		return regionRepository.findById(id).get();
	}
	
	@Override
	public Region editRegion(Region region) {
		return regionRepository.save(region);
	}

	@Override
	public void deleteRegionById(Long id) {
		// TODO Auto-generated method stub
		regionRepository.deleteById(id);
	}

}
