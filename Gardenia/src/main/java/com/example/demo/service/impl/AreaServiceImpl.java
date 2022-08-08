package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Area;
import com.example.demo.repository.AreaRepository;
import com.example.demo.service.AreaService;
import com.example.demo.service.CityService;



@Service
public class AreaServiceImpl implements AreaService{
	
	private AreaRepository areaRepository;

	public AreaServiceImpl(AreaRepository areaRepository) {
		super();
		this.areaRepository = areaRepository;
	}

	@Override
	public List<Area> getAllArea() {
		return areaRepository.findAll();
	}
	
	@Override
	public Area saveArea(Area area) {
		return areaRepository.save(area);
	}
	
	@Override
	public Area getAreaById(Long id) {
		return areaRepository.findById(id).get();
	}
	
	@Override
	public Area editArea(Area area) {
		return areaRepository.save(area);
	}

	@Override
	public void deleteAreaById(Long id) {
		// TODO Auto-generated method stub
		areaRepository.deleteById(id);
	}

}
