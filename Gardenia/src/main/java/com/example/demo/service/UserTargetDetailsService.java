package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.UserTargetDetails;

public interface UserTargetDetailsService {

	List<UserTargetDetails> getAllUserTargetDetails();

	UserTargetDetails saveUserTargetDetails(UserTargetDetails userTargetDetails);

	UserTargetDetails getUserTargetDetails(Long id);

	UserTargetDetails editUserTargetDetails(UserTargetDetails hquserTargetDetails);

	void deleteUserTargetDetailsById(Long id);
}
