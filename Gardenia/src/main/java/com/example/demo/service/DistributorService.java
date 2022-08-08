package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Distributor;

public interface DistributorService {

	List<Distributor> getAllDistributor();

	Distributor saveDistributor(Distributor distributor);

	Distributor getDistributor(Long id);

	Distributor editDistributor(Distributor distributor);

	void deleteDistributorById(Long id);

}
