package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Country;
import com.example.demo.entity.State;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.StateRepository;
import com.example.demo.service.StateService;

@Service
public class StateServiceImpl implements StateService{
	private StateRepository stateRepository;

	public StateServiceImpl(StateRepository stateRepository) {
		super();
		this.stateRepository = stateRepository;
	}
	

	@Override
	public List<State> getAllState() {
		return stateRepository.findAll();
	}
	
	@Override
	public State saveState(State state) {
		return stateRepository.save(state);
	}
	
	@Override
	public State getStateById(Long id) {
		return stateRepository.findById(id).get();
	}
	
	@Override
	public State editState(State state) {
		return stateRepository.save(state);
	}

	@Override
	public void deleteStateById(Long id) {
		// TODO Auto-generated method stub
		stateRepository.deleteById(id);
	}


	@Override
	public State getCountryById(List<Country> id) {
		// TODO Auto-generated method stub
		return null;
	}

}
