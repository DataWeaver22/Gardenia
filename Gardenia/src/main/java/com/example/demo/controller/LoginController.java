package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Login;
 

 
@RestController
@RequestMapping("/test")
public class LoginController {



//@PostMapping
//public String login(@ModelAttribute("user") Login user, @RequestParam(value = "invalid-session", defaultValue="false") boolean invalidSession, Model model ) {
//
//if(invalidSession) {
//	model.addAttribute("invalid-session","Session expired,please-reLogin");
//}	
//
//Login oauthUser = loginService.login(user.getUsername(), user.getPassword());
//List<Login> roleLogin = loginService.getAllLogin();
//for(int i =0;i<roleLogin.size();i++)
//{
//	//if(String.valueOf(user.getUsername())==roleLogin[i]["username"])
//}
//System.out.print(roleLogin);
//if(Objects.nonNull(oauthUser))
//{
//
//return "redirect:/";
//
//
//} else {
//return "redirect:/login";
//
//
//}
//
//}
//@Autowired
//private AuthenticationManager authenticationManager;
//
//@PostMapping
//public ResponseEntity<String> authenticateUser(@RequestBody Login loginDto){
//    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//            loginDto.getUsername(), loginDto.getPassword()));
//
//    SecurityContextHolder.getContext().setAuthentication(authentication);
//    return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
//}

}