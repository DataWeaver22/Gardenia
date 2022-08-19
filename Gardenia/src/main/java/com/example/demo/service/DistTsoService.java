package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.DistNew;
import com.example.demo.entity.Distributor;

public interface DistTsoService {

	List<DistNew> getAllDistNew();

	DistNew saveDistNew(DistNew distNew);

	DistNew getDistNew(Long id);

	void deleteDistNewById(Long id);

	DistNew editDistNew(DistNew distnew);

	
}
