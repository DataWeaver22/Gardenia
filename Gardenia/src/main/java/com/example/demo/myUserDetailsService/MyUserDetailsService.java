package com.example.demo.myUserDetailsService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.demo.entity.Login;
import com.example.demo.repository.LoginRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	LoginRepository loginRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Login> login = loginRepository.findByUsername(username);
		
		if(login.isPresent()) {
			List<SimpleGrantedAuthority> authoritiesList = new ArrayList<>();
			Arrays.asList(login.get().getRole().split(",")).stream().forEach(authority ->{
				authoritiesList.add(new SimpleGrantedAuthority(authority));
			});
			return new User(login.get().getUsername(),login.get().getPassword(),authoritiesList);
		}else {
			throw new UsernameNotFoundException("User " + username + " does not exist!");
		}
	}

}