package com.example.demo.service.impl;

import java.util.List;

import com.example.demo.entity.CurrentBusinessAssociation;
import com.example.demo.repository.CurrentBusinessAssociationRepository;
import com.example.demo.repository.RegionAssociatedToHqRepository;
import com.example.demo.service.CurrentBusinessAssociationService;

public class CurrentBusinessAssociationServiceImpl implements CurrentBusinessAssociationService{

	private CurrentBusinessAssociationRepository currentBusinessAssociationRepository;

	public CurrentBusinessAssociationServiceImpl(CurrentBusinessAssociationRepository currentBusinessAssociationRepository) {
		super();
		this.currentBusinessAssociationRepository = currentBusinessAssociationRepository;
	}

	@Override
	public List<CurrentBusinessAssociation> getAllCurrentBusinessAssociations() {
		// TODO Auto-generated method stub
		return currentBusinessAssociationRepository.findAll();
	}

	@Override
	public CurrentBusinessAssociation saveCurrentBusinessAssociation(
			CurrentBusinessAssociation currentBusinessAssociation) {
		// TODO Auto-generated method stub
		return currentBusinessAssociationRepository.save(currentBusinessAssociation);
	}

	@Override
	public CurrentBusinessAssociation getCurrentBusinessAssociationById(Long id) {
		// TODO Auto-generated method stub
		return currentBusinessAssociationRepository.findById(id).get();
	}

	@Override
	public CurrentBusinessAssociation editCurrentBusinessAssociation(
			CurrentBusinessAssociation currentBusinessAssociation) {
		// TODO Auto-generated method stub
		return currentBusinessAssociationRepository.save(currentBusinessAssociation);
	}

	@Override
	public void deleteCurrentBusinessAssociationById(Long id) {
		// TODO Auto-generated method stub
		currentBusinessAssociationRepository.deleteById(id);
	}
}
