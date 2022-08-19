package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.DistNew;
import com.example.demo.repository.DistTsoRepository;
import com.example.demo.service.DistTsoService;
@Service
public class DistTsoServiceImpl implements DistTsoService{
	
	private DistTsoRepository distTsoRepository;
	
	public DistTsoServiceImpl(DistTsoRepository distTsoRepository) {
		super();
		this.distTsoRepository = distTsoRepository;
	}
	
	@Override
	public List<DistNew> getAllDistNew(){
		return distTsoRepository.findAll();
	}
	
	@Override
	public DistNew saveDistNew(DistNew distNew) {
		return distTsoRepository.save(distNew);
	}
	
	@Override
	public DistNew getDistNew(Long id) {
		return distTsoRepository.findById(id).get();
	}

	@Override
	public DistNew editDistNew(DistNew distNew) {
		return distTsoRepository.save(distNew);
	}

	@Override
	public void deleteDistNewById(Long id) {
		// TODO Auto-generated method stub
		distTsoRepository.deleteById(id);
	}
	
	
//public Distributor getFile(Long fileId) {
//    return distRepository.findById(fileId)
//        .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
//}
//
//@Override
//public Distributor getFile(String fileName) {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public Distributor storeFile(MultipartFile file) {
//	// TODO Auto-generated method stub
//	return null;
//}

	
}
