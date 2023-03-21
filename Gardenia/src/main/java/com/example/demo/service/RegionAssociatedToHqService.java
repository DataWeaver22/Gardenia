package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.RegionAssociatedToHq;

public interface RegionAssociatedToHqService {

	List<RegionAssociatedToHq> getAllRegionAssociatedToHqs();

	RegionAssociatedToHq saveRegionAssociatedToHq(RegionAssociatedToHq regionAssociatedToHq);

	RegionAssociatedToHq getRegionAssociatedToHqById(Long id);

	RegionAssociatedToHq editRegionAssociatedToHq(RegionAssociatedToHq regionAssociatedToHq);

	void deleteRegionAssociatedToHqById(Long id);
}
