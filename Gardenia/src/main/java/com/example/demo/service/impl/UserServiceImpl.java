package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;


import com.example.demo.entity.User;
import com.example.demo.repository.HqUserRepository;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService{

		private HqUserRepository userRepository;
		
		public UserServiceImpl(HqUserRepository userRepository) {
			super();
			this.userRepository = userRepository;
		}

		@Override
		public List<User> getAllUser(){
			return userRepository.findAll();
		}
		
		@Override
		public User saveUser(User user) {
			return userRepository.save(user);
		}
		
		@Override
		public User getUser(Long id) {
			return userRepository.findById(id).get();
		}

		@Override
		public User editUser(User hquser) {
			return userRepository.save(hquser);
		}

		@Override
		public void deleteUserById(Long id) {
			// TODO Auto-generated method stub
			userRepository.deleteById(id);
		}

}
