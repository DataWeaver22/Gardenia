package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
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

import com.example.demo.Export.DistributorExportExcel;
import com.example.demo.Import.DistributorImportHelper;
import com.example.demo.Import.DistributorImportService;
import com.example.demo.entity.City;
import com.example.demo.entity.DistNew;
import com.example.demo.entity.Distributor;
import com.example.demo.entity.DistributorCode;
import com.example.demo.entity.Region;
import com.example.demo.entity.State;
import com.example.demo.entity.User;
import com.example.demo.repository.DistRepository;
import com.example.demo.repository.DistributorCodeRepository;
import com.example.demo.repository.HqUserRepository;
import com.example.demo.service.AreaService;
import com.example.demo.service.CityService;
import com.example.demo.service.DistributorCodeService;
import com.example.demo.service.DistributorService;
import com.example.demo.service.RegionService;
import com.example.demo.service.StateService;

@Controller
public class DistController {
	private DistributorService distributorService;
	private Service service;
	@Autowired
	private StateService stateService;
	
	@Autowired
	private RegionService regionService;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private HqUserRepository hqUserRepository;
	
	@Autowired
	private DistRepository distRepository;
	
	@Autowired
	private DistributorCodeRepository distributorCodeRepository;
	
	@Autowired
	private DistributorCodeService distributorCodeService;
	
	@Autowired
	private DistributorImportService distributorImportService;

	public DistController(DistributorService distributorService) {
		super();
		this.distributorService = distributorService;
	}
	
	@GetMapping("/distributor/upload")
    public String index() {
        return "distributorUploadPage";
    }
	
	@RequestMapping(value="/distributor/upload/import", method=RequestMethod.POST)
	public String upload(@RequestParam("file")MultipartFile file){
		if(DistributorImportHelper.checkExcelFormat(file)) {
			this.distributorImportService.save(file);
			
			return "redirect:/user";
		}
		return "success";
	}

	@GetMapping("/distributor")
	public String listUser(Model model){
		model.addAttribute("distributortable",distributorService.getAllDistributor());
		return "distributor";
	}
	
	@GetMapping("/distributor/new")
	public String CreateNewForm(Model model){
		//Create student object to hold student form data
		Distributor distributor = new Distributor();
		List<State> state_code = stateService.getAllState();
		List<State> state_name = stateService.getAllState();
		model.addAttribute("distributor",distributor);
		model.addAttribute("state_code",state_code);
		model.addAttribute("state_name",state_name);
		
		List<Region> region_code = regionService.getAllRegion();
		List<Region> region_name = regionService.getAllRegion();
		model.addAttribute("distributor",distributor);
		model.addAttribute("region_code",region_code);
		model.addAttribute("region_name",region_name);
		
		List<City> city_code = cityService.getAllCity();
		List<City> city_name = cityService.getAllCity();
		model.addAttribute("distributor",distributor);
		model.addAttribute("city_code",city_code);
		model.addAttribute("city_name",city_name);
		
		List<String> status = new ArrayList<String>();
		status.add("Active");
		status.add("Inactive");
	    model.addAttribute("status", status);
	    
	    List<DistributorCode> region_code1 = distributorCodeService.getAllDistributorCode();
		model.addAttribute("distributor",distributor);
	    model.addAttribute("regioncode1", region_code1);
		
//		List<String> options = new ArrayList<String>();
//	    options.add("Serviced");
//	    options.add("Unserviced");
//	    model.addAttribute("options", options);
		return "create_distributor";
	}
	
	@PostMapping("/distributor")
	public String saveUser(@ModelAttribute("distributor") Distributor distributor,@ModelAttribute("disttso") DistNew distNew,
			DistributorCode distributorCode, Model model,@RequestParam("rCodeUpdateString") String rCodeUpdateString, @RequestParam("rCodeUpdateNum") String rCodeUpdateNum,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		//Region Data Update
		System.out.println("Region Code:" + rCodeUpdateString);
		System.out.println("Code Number:" + rCodeUpdateNum);
		
		distributorCodeRepository.updateByRegionCode(rCodeUpdateNum, rCodeUpdateString);
		
		LocalDateTime createDateTime = LocalDateTime.now();
		String sId = distributor.getState_id();
		System.out.println(sId);
		String name = hqUserRepository.findByStateName(Long.parseLong(sId));
		System.out.println(name);
		distributor.setState_name(name);
		
		String rId = distributor.getRegion_id();
		System.out.println(rId);
		String rname = hqUserRepository.findByRegionName(Long.parseLong(rId));
		System.out.println(rname);
		distributor.setRegion_name(rname);
		
		String ctyId = distributor.getCity_id();
		System.out.println(ctyId);
		String cname = distRepository.findByCityName(Long.parseLong(ctyId));
		System.out.println(cname);
		distributor.setCity_name(cname);
		
		String regionCodeString = distributorCode.getRegion_code();
//		String codeNumberInteger = "000";
//		distributorCode.setRegion_name(codeNumberInteger);
		distributorCode.setRegion_code(regionCodeString);
		//distributor.setCreate_date(createDateTime);
		System.out.println(distributorCode);
		
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        distributor.setDocuments(fileName);
         
        Distributor savedUser = distRepository.save(distributor);
 
        String uploadDir = "user-photos/" + savedUser.getId();
 
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
         
      
		model.addAttribute("distTso",distNew);
		distributorService.saveDistributor(distributor);
		return "redirect:/distributor";
	}
	
	@GetMapping("/distributor/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
		/* DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss"); 
        String currentDateTime = dateFormatter.format(new Date());*/
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=distributor.xlsx";
        response.setHeader(headerKey, headerValue);
         
        List<Distributor> listUsers = distributorService.getAllDistributor();
         
        DistributorExportExcel excelExporter = new DistributorExportExcel(listUsers);
         
        excelExporter.export(response);    
    }  
    
	@GetMapping("/distributor/edit/{id}")
	public String editDist(@PathVariable Long id,Distributor distributor, Model model) {
		List<State> state_id = stateService.getAllState();
		List<State> state_name = stateService.getAllState();
		model.addAttribute("state_code",state_id);
		model.addAttribute("state_name",state_name);
		Distributor user1 = distributorService.getDistributor(id);
		String sIdString = user1.getState_id();
		model.addAttribute("state_code_ID", sIdString);
		
		List<Region> region_id = regionService.getAllRegion();
		List<Region> region_name = regionService.getAllRegion();
		model.addAttribute("region_code",region_id);
		model.addAttribute("region_name",region_name);
		Distributor user2 = distributorService.getDistributor(id);
		String rIdString = user2.getRegion_id();
		model.addAttribute("region_code_ID", rIdString);
		
		List<City> city_id = cityService.getAllCity();
		List<City> city_name = cityService.getAllCity();
		model.addAttribute("city_id",city_id);
		model.addAttribute("city_name",city_name);
		Distributor user3 = distributorService.getDistributor(id);
		String cIdString = user3.getCity_id();
		model.addAttribute("city_code_ID", cIdString);

		List<String> status = new ArrayList<String>();
		status.add("Active");
		status.add("Inactive");
	    model.addAttribute("status", status);
		
		List<String> options = new ArrayList<String>();
	    options.add("Services");
	    options.add("Unserviced");
	    model.addAttribute("options", options);
	    
	    Distributor dist6 = distributorService.getDistributor(id);
		String images = dist6.getPhotosImagePath();
		model.addAttribute("images", images);
	    
		model.addAttribute("distributor", distributorService.getDistributor(id));
		return "edit_distributor";
	}
	
	@PostMapping("/distributor/{id}")
	public String updateUser(@PathVariable Long id,
			@ModelAttribute("distributor") Distributor distributor,
			Model model) {
		
		//Get Existing User
		Distributor existingDistributor = distributorService.getDistributor(id);
		
		String sId = distributor.getState_id();
		System.out.println(sId);
		String name = distRepository.findByStateName(Long.parseLong(sId));
		System.out.println(name);
		distributor.setState_name(name);
		
		String rId = distributor.getRegion_id();
		System.out.println(rId);
		String rname = distRepository.findByRegionName(Long.parseLong(rId));
		System.out.println(rname);
		distributor.setRegion_name(rname);
		
		String ctyId = distributor.getCity_id();
		System.out.println(ctyId);
		String ctyname = distRepository.findByCityName(Long.parseLong(ctyId));
		System.out.println(ctyname);
		distributor.setCity_name(ctyname);
		
		
		//Save User
		distributorService.editDistributor(distributor);
		return "redirect:/distributor";
	}
	
	@GetMapping("/distributor/{id}")
	public String deleteDistributor(@PathVariable Long id) {
		distributorService.deleteDistributorById(id);
		return "redirect:/distributor";
	}
	
	@GetMapping("/distributor/approve/{id}")
	public String approveDistributor(@PathVariable Long id) {
		Long distID = id;
		System.out.println(distID);
		String approved = "Approved";
		distRepository.updateByStatus(approved,distID);
		return "redirect:/distributor";
	}
	
	@GetMapping("/distributor/reject/{id}")
	public String rejectDistributor(@PathVariable Long id) {
		Long distID = id;
		System.out.println(distID);
		String approved = "Rejected";
		distRepository.updateByStatus(approved,distID);
		return "redirect:/distributor";
	}
}
