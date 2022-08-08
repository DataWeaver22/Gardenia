package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.District;

public interface DistrictService {

	List<District> getAllDistrict();

	District saveDistrict(District district);

	District getDistrictById(Long id);

	District editDistrict(District district);

	void deleteDistrictById(Long id);

}
