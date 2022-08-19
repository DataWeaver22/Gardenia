package com.example.demo.myUserDetailsService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Login;
import com.example.demo.repository.LoginRepository;
import com.example.demo.myUserDetails.MyUserDetails;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	LoginRepository loginRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Login> login = loginRepository.findByUsername(username);
		
		login.orElseThrow(() -> new UsernameNotFoundException("User Not Found with Name : " + username));
		System.out.println(login.map(MyUserDetails::new).get());
		return login.map(MyUserDetails::new).get();
	}

}