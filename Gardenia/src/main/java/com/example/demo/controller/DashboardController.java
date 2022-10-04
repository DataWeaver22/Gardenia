package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class DashboardController {



	
@RequestMapping("/country")
public String getCountry() {

return "country";
}

@RequestMapping("/state")
public String getState() {
return "state";
}

@RequestMapping("/region")
public String getRegion() {
return "region";
}

@RequestMapping("/district")
public String getDistrict() {
return "district";
}

@RequestMapping("/city")
public String getCity() {
return "city";
}

@RequestMapping("/area")
public String getArea() {
return "area";
}

@GetMapping(value = {"/logout"})
public String logoutDo(HttpServletRequest request,HttpServletResponse response)
{


    return "redirect:/login";
}
}