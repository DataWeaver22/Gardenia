package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Region;
public interface RegionService {

	List<Region> getAllRegion();

	Region saveRegion(Region region);

	Region getRegionById(Long id);

	Region editRegion(Region region);

	void deleteRegionById(Long id);

}
