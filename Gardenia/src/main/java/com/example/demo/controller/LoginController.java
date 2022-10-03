package com.example.demo.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Login;
import com.example.demo.service.LoginService;
 

 
@Controller
public class LoginController {

@Autowired
private LoginService loginService;

@GetMapping("/login")
public ModelAndView login() {
ModelAndView mav = new ModelAndView("login");
  mav.addObject("user", new Login());
  return mav;
}

@PostMapping("/login")
public String login(@ModelAttribute("user") Login user, @RequestParam(value = "invalid-session", defaultValue="false") boolean invalidSession, Model model ) {

if(invalidSession) {
	model.addAttribute("invalid-session","Session expired,please-reLogin");
}	

Login oauthUser = loginService.login(user.getUsername(), user.getPassword());
List<Login> roleLogin = loginService.getAllLogin();
for(int i =0;i<roleLogin.size();i++)
{
	//if(String.valueOf(user.getUsername())==roleLogin[i]["username"])
}
System.out.print(roleLogin);
if(Objects.nonNull(oauthUser))
{

return "redirect:/";


} else {
return "redirect:/login";


}

}

@RequestMapping(value = {"/logout"}, method = RequestMethod.POST)
public String logoutDo(HttpServletRequest request,HttpServletResponse response)
{


  return "redirect:/login";
}

}