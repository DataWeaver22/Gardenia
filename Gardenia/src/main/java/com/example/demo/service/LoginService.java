package com.example.demo.service;


import java.util.List;

import com.example.demo.entity.District;
import com.example.demo.entity.Login;

public interface LoginService {

	Login login(String username, String password);
	List<Login> getAllLogin();
}
