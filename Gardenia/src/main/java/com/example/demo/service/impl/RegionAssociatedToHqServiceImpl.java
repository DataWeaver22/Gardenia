package com.example.demo.service.impl;

import java.util.List;

import com.example.demo.entity.RegionAssociatedToHq;
import com.example.demo.repository.RegionAssociatedToHqRepository;
import com.example.demo.service.RegionAssociatedToHqService;

public class RegionAssociatedToHqServiceImpl implements RegionAssociatedToHqService {

	private RegionAssociatedToHqRepository regionAssociatedToHqRepository;

	public RegionAssociatedToHqServiceImpl(RegionAssociatedToHqRepository regionAssociatedToHqRepository) {
		super();
		this.regionAssociatedToHqRepository = regionAssociatedToHqRepository;
	}

	@Override
	public List<RegionAssociatedToHq> getAllRegionAssociatedToHqs() {
		// TODO Auto-generated method stub
		return regionAssociatedToHqRepository.findAll();
	}

	@Override
	public RegionAssociatedToHq saveRegionAssociatedToHq(RegionAssociatedToHq regionAssociatedToHq) {
		// TODO Auto-generated method stub
		return regionAssociatedToHqRepository.save(regionAssociatedToHq);
	}

	@Override
	public RegionAssociatedToHq getRegionAssociatedToHqById(Long id) {
		// TODO Auto-generated method stub
		return regionAssociatedToHqRepository.findById(id).get();
	}

	@Override
	public RegionAssociatedToHq editRegionAssociatedToHq(RegionAssociatedToHq regionAssociatedToHq) {
		// TODO Auto-generated method stub
		return regionAssociatedToHqRepository.save(regionAssociatedToHq);
	}

	@Override
	public void deleteRegionAssociatedToHqById(Long id) {
		// TODO Auto-generated method stub
		regionAssociatedToHqRepository.deleteById(id);
	}

}
