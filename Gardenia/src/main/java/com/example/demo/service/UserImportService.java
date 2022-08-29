package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Import.UserImportHelper;
import com.example.demo.entity.User;
import com.example.demo.repository.HqUserRepository;


@Service
public class UserImportService {

	@Autowired
	private HqUserRepository userRepository;
	
	public void save(MultipartFile file) {
		try {
			List<User> users = UserImportHelper.convertToUsers(file.getInputStream());
			this.userRepository.saveAll(users);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<User> getAllUsers(){
		return this.userRepository.findAll();
	}
}
