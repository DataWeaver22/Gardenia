package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.CurrentBusinessAssociation;

public interface CurrentBusinessAssociationService {

	List<CurrentBusinessAssociation> getAllCurrentBusinessAssociations();

	CurrentBusinessAssociation saveCurrentBusinessAssociation(CurrentBusinessAssociation currentBusinessAssociation);

	CurrentBusinessAssociation getCurrentBusinessAssociationById(Long id);

	CurrentBusinessAssociation editCurrentBusinessAssociation(CurrentBusinessAssociation currentBusinessAssociation);

	void deleteCurrentBusinessAssociationById(Long id);
}
