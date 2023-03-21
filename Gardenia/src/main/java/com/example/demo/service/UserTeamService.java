package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.UserTeam;

public interface UserTeamService {
	
	List<UserTeam> getAllUserTeams();

	UserTeam saveUserTeam(UserTeam userTeam);

	UserTeam getUserTeam(Long id);

	UserTeam editUserTeam(UserTeam userTeam);

	void deleteUserTeamById(Long id);

}
