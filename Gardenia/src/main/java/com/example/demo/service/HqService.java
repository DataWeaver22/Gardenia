package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.HqMaster;

public interface HqService {

	List<HqMaster> getAllHq();

	HqMaster saveHqMaster(HqMaster hqmaster);

	HqMaster getHqMaster(Long id);

	HqMaster editHqMaster(HqMaster hqmaster);

	void deleteHqMasterById(Long id);

	List<HqMaster> listAll(String keyword);

}
