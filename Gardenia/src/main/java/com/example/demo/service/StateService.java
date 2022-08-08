package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Country;
import com.example.demo.entity.State;

public interface StateService {

	State saveState(State state);

	State getStateById(Long id);

	State editState(State state);

	void deleteStateById(Long id);

	List<State> getAllState();
	
	State getCountryById(List<Country> id);

}
