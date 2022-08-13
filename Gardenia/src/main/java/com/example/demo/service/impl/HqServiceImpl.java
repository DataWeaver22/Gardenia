package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.HqMaster;
import com.example.demo.repository.HqRepository;
import com.example.demo.service.HqService;

@Service
public class HqServiceImpl implements HqService{

		private HqRepository hqRepository;

		public HqServiceImpl(HqRepository hqRepository) {
			super();
			this.hqRepository = hqRepository;
		}
		
		@Override
		public List<HqMaster> getAllHq(){
			return hqRepository.findAll();
		}
		
		@Override
		public HqMaster saveHqMaster(HqMaster hqmaster) {
			return hqRepository.save(hqmaster);
		}
		
		@Override
		public HqMaster getHqMaster(Long id) {
			return hqRepository.findById(id).get();
		}

		@Override
		public HqMaster editHqMaster(HqMaster hqmaster) {
			return hqRepository.save(hqmaster);
		}

		@Override
		public void deleteHqMasterById(Long id) {
			// TODO Auto-generated method stub
			hqRepository.deleteById(id);
		}
		
		
		public List<HqMaster> listAll(String keyword) {
	        if (keyword != null) {
	            return hqRepository.search(keyword);
	        }
	        return hqRepository.findAll();
	    }
		
}
