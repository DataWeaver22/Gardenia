package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Family;

public interface FamilyService {
	
	Family saveFamily(Family family);

	Family getFamilyById(Long id);

	Family editFamily(Family family);

	void deleteFamilyById(Long id);

	List<Family> getAllFamilies();

}
