package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Distributor;
import com.example.demo.entity.Login;
import com.example.demo.repository.LoginRepository;
import com.example.demo.service.LoginService;
 

@Service
public class LoginServiceImpl implements LoginService{
@Autowired
private LoginRepository repo;
  
  public Login login(String username, String password) {
  Login user = repo.findByUsernameAndPassword(username, password);
   return user;
  }
 
  @Override
	public List<Login> getAllLogin(){
		return repo.findAll();
	}
}
