package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Area;
import com.example.demo.entity.StatesAssociatedToRegion;
import com.example.demo.repository.AreaRepository;
import com.example.demo.repository.StatesAssociatedToRegionRepository;
import com.example.demo.service.StatesAssociatedToRegionService;

@Service
public class StatesAssociatedToRegionServiceImpl implements StatesAssociatedToRegionService{

	private StatesAssociatedToRegionRepository statesAssociatedToRegionRepository;

	public StatesAssociatedToRegionServiceImpl(StatesAssociatedToRegionRepository statesAssociatedToRegionRepository) {
		super();
		this.statesAssociatedToRegionRepository = statesAssociatedToRegionRepository;
	}
	
	@Override
	public List<StatesAssociatedToRegion> getAllStatesAssociatedToRegions() {
		// TODO Auto-generated method stub
		return statesAssociatedToRegionRepository.findAll();
	}

	@Override
	public StatesAssociatedToRegion saveStatesAssociatedToRegion(StatesAssociatedToRegion statesAssociatedToRegion) {
		// TODO Auto-generated method stub
		return statesAssociatedToRegionRepository.save(statesAssociatedToRegion);
	}

	@Override
	public StatesAssociatedToRegion getStatesAssociatedToRegionById(Long id) {
		// TODO Auto-generated method stub
		return statesAssociatedToRegionRepository.findById(id).get();
	}

	@Override
	public StatesAssociatedToRegion editStatesAssociatedToRegion(StatesAssociatedToRegion statesAssociatedToRegion) {
		// TODO Auto-generated method stub
		return statesAssociatedToRegionRepository.save(statesAssociatedToRegion);
	}

	@Override
	public void deleteStatesAssociatedToRegionById(Long id) {
		// TODO Auto-generated method stub
		statesAssociatedToRegionRepository.deleteById(id);
	}

}
