package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.User;

public interface UserService {

	List<User> getAllUser();

	User saveUser(User user);

	User getUser(Long id);

	User editUser(User hquser);

	void deleteUserById(Long id);

}
