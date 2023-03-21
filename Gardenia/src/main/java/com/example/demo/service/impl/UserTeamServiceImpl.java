package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.UserTeam;
import com.example.demo.repository.UserTeamRepository;
import com.example.demo.service.UserTeamService;

@Service
public class UserTeamServiceImpl implements UserTeamService{

private UserTeamRepository userTeamRepository;
	
	public UserTeamServiceImpl(UserTeamRepository userTeamRepository) {
		super();
		this.userTeamRepository = userTeamRepository;
	}

	@Override
	public List<UserTeam> getAllUserTeams() {
		// TODO Auto-generated method stub
		return userTeamRepository.findAll();
	}

	@Override
	public UserTeam saveUserTeam(UserTeam userTeam) {
		// TODO Auto-generated method stub
		return userTeamRepository.save(userTeam);
	}

	@Override
	public UserTeam getUserTeam(Long id) {
		// TODO Auto-generated method stub
		return userTeamRepository.findById(id).get();
	}

	@Override
	public UserTeam editUserTeam(UserTeam userTeam) {
		// TODO Auto-generated method stub
		return userTeamRepository.save(userTeam);
	}

	@Override
	public void deleteUserTeamById(Long id) {
		// TODO Auto-generated method stub
		userTeamRepository.deleteById(id);
	}
}
