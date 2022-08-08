package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Area;

public interface AreaService {

	List<Area> getAllArea();

	Area saveArea(Area area);

	Area getAreaById(Long id);

	Area editArea(Area area);

	void deleteAreaById(Long id);

	

}
