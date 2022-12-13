package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.BrandAssociatedToDist;

public class BrandAssociatedList {
	
	private List<BrandAssociatedToDist> brandAssociatedToDists = new ArrayList<BrandAssociatedToDist>();


	public BrandAssociatedList(ArrayList arrayList) {
		// TODO Auto-generated constructor stub
	}

	public List<BrandAssociatedToDist> getBrandAssociatedToDists() {
		return brandAssociatedToDists;
	}

	public void setBrandAssociatedToDists(List<BrandAssociatedToDist> brandAssociatedToDists) {
		this.brandAssociatedToDists = brandAssociatedToDists;
	}
	
	
}
