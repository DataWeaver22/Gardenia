package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.UserTargetDetails;
import com.example.demo.repository.UserTargetDetailsRepository;
import com.example.demo.service.UserTargetDetailsService;

@Service
public class UserTargetDetailsServiceImpl implements UserTargetDetailsService{

	private UserTargetDetailsRepository userTargetDetailsRepository;
	
	public UserTargetDetailsServiceImpl(UserTargetDetailsRepository userTargetDetailsRepository) {
		super();
		this.userTargetDetailsRepository = userTargetDetailsRepository;
	}

	@Override
	public List<UserTargetDetails> getAllUserTargetDetails(){
		return userTargetDetailsRepository.findAll();
	}

	@Override
	public UserTargetDetails saveUserTargetDetails(UserTargetDetails userTargetDetails) {
		return userTargetDetailsRepository.save(userTargetDetails);
	}

	@Override
	public UserTargetDetails getUserTargetDetails(Long id) {
		return userTargetDetailsRepository.findById(id).get();
	}

	@Override
	public UserTargetDetails editUserTargetDetails(UserTargetDetails hquserTargetDetails) {
		return userTargetDetailsRepository.save(hquserTargetDetails);
	}

	@Override
	public void deleteUserTargetDetailsById(Long id) {
		userTargetDetailsRepository.deleteById(id);
	}
}
