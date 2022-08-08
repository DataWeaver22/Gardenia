package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.DistributorCode;

public interface DistributorCodeService {

	List<DistributorCode> getAllDistributorCode();

	DistributorCode saveDistributorCode(DistributorCode distributorCode);

	DistributorCode getDistributorCodeById(Long id);

	DistributorCode editDistributorCode(DistributorCode distributorCode);

	void deleteDistributorCodeById(Long id);

}
