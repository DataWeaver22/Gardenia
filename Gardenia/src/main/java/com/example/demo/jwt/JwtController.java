package com.example.demo.jwt;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.TokenBlacklist;
import com.example.demo.myUserDetailsService.MyUserDetailsService;
import com.example.demo.repository.TokenBlacklistRepository;

@RestController
public class JwtController {

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
		System.out.println(jwtRequest);
		try {
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
		} catch (UsernameNotFoundException e) {
			e.printStackTrace();
			throw new Exception("Username Not Found");
		}catch (BadCredentialsException e) {
			throw new Exception("Bad Credentials");
		}
		UserDetails userDetails=this.myUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
		
		String tokenString = this.jwtUtil.generateToken(userDetails);
		String message = "You have logged in Successfully";
		String role = "";
		if(userDetails.getAuthorities().toString().equals("[ROLE_USER]")) {
			role = "USER";
		}else if(userDetails.getAuthorities().toString().equals("[ROLE_MIS]")) {
			role = "MIS";
		}else if(userDetails.getAuthorities().toString().equals("[ROLE_RSM]")) {
			role = "RSM";
		}
		
		System.out.println("JWT Token:"+tokenString);
		
		return ResponseEntity.ok(new JwtResponse(tokenString,message,role));
	}
	
	@Autowired
	TokenBlacklistRepository tokenBlacklistRepository;
	
	@RequestMapping(value = "/signout", method = RequestMethod.POST)
	public String signOutAndInvalidateToken(@RequestBody TokenBlacklist tokenBlacklist) throws Exception{
		
		tokenBlacklistRepository.save(tokenBlacklist);
		
		return "Signed Out Successfully";
	}
}
