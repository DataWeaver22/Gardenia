package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Area;
import com.example.demo.entity.StatesAssociatedToRegion;

public interface StatesAssociatedToRegionService {

	List<StatesAssociatedToRegion> getAllStatesAssociatedToRegions();

	StatesAssociatedToRegion saveStatesAssociatedToRegion(StatesAssociatedToRegion statesAssociatedToRegion);

	StatesAssociatedToRegion getStatesAssociatedToRegionById(Long id);

	StatesAssociatedToRegion editStatesAssociatedToRegion(StatesAssociatedToRegion statesAssociatedToRegion);

	void deleteStatesAssociatedToRegionById(Long id);
}
