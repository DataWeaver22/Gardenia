package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Family;
import com.example.demo.repository.FamilyRepository;
import com.example.demo.service.FamilyService;

@Service
public class FamilyServiceImpl implements FamilyService{
	
	@Autowired
	private FamilyRepository familyRepository;

	@Override
	public Family saveFamily(Family family) {
		// TODO Auto-generated method stub
		return familyRepository.save(family);
	}

	@Override
	public Family getFamilyById(Long id) {
		// TODO Auto-generated method stub
		return familyRepository.findById(id).get();
	}

	@Override
	public Family editFamily(Family family) {
		// TODO Auto-generated method stub
		return familyRepository.save(family);
	}

	@Override
	public void deleteFamilyById(Long id) {
		// TODO Auto-generated method stub
		familyRepository.deleteById(id);
	}

	@Override
	public List<Family> getAllFamilies() {
		// TODO Auto-generated method stub
		return familyRepository.findAll();
	}

}
