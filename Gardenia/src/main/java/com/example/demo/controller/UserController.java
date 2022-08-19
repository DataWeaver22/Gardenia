package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Enum.Roles;
import com.example.demo.Enum.Status;
import com.example.demo.Export.RegionExportExcel;
import com.example.demo.Export.UserExportExcel;
import com.example.demo.entity.Area;
import com.example.demo.entity.City;
import com.example.demo.entity.Distributor;
import com.example.demo.entity.HqMaster;
import com.example.demo.entity.Region;
import com.example.demo.entity.State;
import com.example.demo.entity.User;
import com.example.demo.repository.HqUserRepository;
import com.example.demo.service.AreaService;
import com.example.demo.service.HqService;
import com.example.demo.service.RegionService;
import com.example.demo.service.StateService;
import com.example.demo.service.UserService;

@Controller
public class UserController {
	private UserService userService;
	Status status;
	Roles roles;
	
	@Autowired
	private StateService stateService;
	
	@Autowired
	private RegionService regionService;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private HqService hqService;
	
	@Autowired
	private HqUserRepository hqUserRepository;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@GetMapping("/user")
	public String listUser(Model model){
		model.addAttribute("user",userService.getAllUser());
		return "user";
	}
	
	@GetMapping("/user/new")
	public String CreateNewForm(Model model){
		//Create student object to hold student form data
		User user = new User();
		List<State> state_code = stateService.getAllState();
		List<State> state_name = stateService.getAllState();
		model.addAttribute("user",user);
		model.addAttribute("state_code",state_code);
		model.addAttribute("state_name",state_name);
		
		List<Region> region_code = regionService.getAllRegion();
		List<Region> region_name = regionService.getAllRegion();
		model.addAttribute("user",user);
		model.addAttribute("region_code",region_code);
		model.addAttribute("region_name",region_name);
		
		List<HqMaster> hq_code = hqService.getAllHq();
		List<HqMaster> hq_name = hqService.getAllHq();
		model.addAttribute("user",user);
		model.addAttribute("hq_code",hq_code);
		model.addAttribute("hq_name",hq_name);
		
		List<Area> area_code = areaService.getAllArea();
		List<Area> area_name = areaService.getAllArea();
		model.addAttribute("user",user);
		model.addAttribute("area_code",area_code);
		model.addAttribute("area_name",area_name);
		
		List<String> status = new ArrayList<String>();
		status.add("Active");
		status.add("Inactive");
	    model.addAttribute("status", status);
		
		List<String> options = new ArrayList<String>();
	    options.add("Territory Sales Officer");
	    options.add("Area Sales Executive");
	    options.add("Area Sales Manager");
	    options.add("Regional Sales Manager");
	    model.addAttribute("options", options);
		return "create_user";
	}
	
	
	
	@PostMapping("/user")
	public String saveUser(@ModelAttribute("user") User user, Model model,@RequestParam("image") MultipartFile multipartFile) throws IOException {
		LocalDateTime createDateTime = LocalDateTime.now();
		String sId = user.getState_id();
		System.out.println(sId);
		String name = hqUserRepository.findByStateName(Long.parseLong(sId));
		System.out.println(name);
		user.setState_name(name);
		
		String rId = user.getRegion_id();
		System.out.println(rId);
		String rname = hqUserRepository.findByRegionName(Long.parseLong(rId));
		System.out.println(rname);
		user.setRegion_name(rname);
		
		String aId = user.getArea_id();
		System.out.println(aId);
		String aname = hqUserRepository.findByAreaName(Long.parseLong(aId));
		System.out.println(aname);
		user.setArea_name(aname);
		
		String hId = user.getHq_id();
		System.out.println(hId);
		String hname = hqUserRepository.findByHqName(Long.parseLong(hId));
		System.out.println(hname);
		user.setHq_name(hname);
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setDocuments(fileName);
         
        User savedUser = hqUserRepository.save(user);
 
        String uploadDir = "user-photos/" + savedUser.getId();
 
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		
		user.setCreate_date(createDateTime);
		userService.saveUser(user);
		return "redirect:/user";
	}
	
	@GetMapping("/user/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
		/* DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss"); 
        String currentDateTime = dateFormatter.format(new Date());*/
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=user.xlsx";
        response.setHeader(headerKey, headerValue);
         
        List<User> listUsers = userService.getAllUser();
         
        UserExportExcel excelExporter = new UserExportExcel(listUsers);
         
        excelExporter.export(response);    
    }  
	
	@GetMapping("/user/edit/{id}")
	public String editUser(@PathVariable Long id,User user, Model model) {
		List<State> state_id = stateService.getAllState();
		List<State> state_name = stateService.getAllState();
		model.addAttribute("state_code",state_id);
		model.addAttribute("state_name",state_name);
		User user1 = userService.getUser(id);
		String sIdString = user1.getState_id();
		model.addAttribute("state_code_ID", sIdString);
		
		List<Region> region_id = regionService.getAllRegion();
		List<Region> region_name = regionService.getAllRegion();
		model.addAttribute("region_code",region_id);
		model.addAttribute("region_name",region_name);
		User user2 = userService.getUser(id);
		String rIdString = user2.getRegion_id();
		model.addAttribute("region_code_ID", rIdString);
		
		List<Area> area_id = areaService.getAllArea();
		List<Area> area_name = areaService.getAllArea();
		model.addAttribute("area_id",area_id);
		model.addAttribute("area_name",area_name);
		User user3 = userService.getUser(id);
		String aIdString = user3.getArea_id();
		model.addAttribute("area_code_ID", aIdString);
		
		List<HqMaster> hq_id = hqService.getAllHq();
		List<HqMaster> hq_name = hqService.getAllHq();
		model.addAttribute("hq_id",hq_id);
		model.addAttribute("hq_name",hq_name);
		User user4 = userService.getUser(id);
		String hIdString = user4.getHq_id();
		model.addAttribute("hq_code_ID", hIdString);

//		List<String> status = new ArrayList<String>();
//		status.add("Active");
//		status.add("Inactive");
//	    model.addAttribute("status", status);
		
		List<String> options = new ArrayList<String>();
	    options.add("Territory Sales Officer");
	    options.add("Area Sales Executive");
	    options.add("Area Sales Manager");
	    options.add("Regional Sales Manager");
	    model.addAttribute("options", options);
	    
		model.addAttribute("user", userService.getUser(id));
		return "edit_user";
	}
	
	@PostMapping(value = "/User") 
	public String populateList(Model model) {
	    List<String> options = new ArrayList<String>();
	    options.add("option 1");
	    options.add("option 2");
	    options.add("option 3");
	    model.addAttribute("options", options);
	    return "user";
	}
	
	@PostMapping("/user/{id}")
	public String updateUser(@PathVariable Long id,
			@ModelAttribute("hquser") User user,
			Model model) {
		
		//Get Existing User
		User existingUser = userService.getUser(id);
		
		String sId = user.getState_id();
		System.out.println(sId);
		String name = hqUserRepository.findByStateName(Long.parseLong(sId));
		System.out.println(name);
		user.setState_name(name);
		
		String rId = user.getRegion_id();
		System.out.println(rId);
		String rname = hqUserRepository.findByRegionName(Long.parseLong(rId));
		System.out.println(rname);
		user.setRegion_name(rname);
		
		String aId = user.getArea_id();
		System.out.println(aId);
		String aname = hqUserRepository.findByAreaName(Long.parseLong(aId));
		System.out.println(aname);
		user.setArea_name(aname);
		
		String hId = user.getHq_id();
		System.out.println(hId);
		String hname = hqUserRepository.findByAreaName(Long.parseLong(hId));
		System.out.println(hname);
		user.setHq_name(hname);
		
		//Save User
		userService.editUser(user);
		return "redirect:/user";
	}
	
	@GetMapping("/user/{id}")
	public String deleteUser(@PathVariable Long id) {
		userService.deleteUserById(id);
		return "redirect:/user";
	}
}
