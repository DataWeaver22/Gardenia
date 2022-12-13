package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Category;
import com.example.demo.entity.HqMaster;
import com.example.demo.entity.Region;
import com.example.demo.entity.User;
import com.example.demo.repository.HqRepository;
import com.example.demo.repository.HqUserRepository;

@RestController
public class DropdownController {

	@GetMapping("/uom/dropdown")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	List<Map<String, Object>> uomDropDown() {
		// Create student object to hold student form data
		List<Map<String, Object>> uomList = new ArrayList<Map<String, Object>>();
		Map<String, Object> pageUnit = new HashMap<>();
		pageUnit.put("label", "Unit(s)");
		pageUnit.put("value", "Units");
		uomList.add(pageUnit);
		Map<String, Object> pageCarton = new HashMap<>();
		pageCarton.put("label", "Carton");
		pageCarton.put("value", "Carton");
		uomList.add(pageCarton);
		return uomList;
	}

	@GetMapping("/status/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_USER','ROLE_RSM')")
	List<Map<String, Object>> statusDropDown() {
		// Create student object to hold student form data
		List<Map<String, Object>> statusList = new ArrayList<Map<String, Object>>();
		Map<String, Object> pageActive = new HashMap<>();
		pageActive.put("label", "Active");
		pageActive.put("value", "Active");
		statusList.add(pageActive);
		Map<String, Object> pageInactive = new HashMap<>();
		pageInactive.put("label", "Inactive");
		pageInactive.put("value", "Inactive");
		statusList.add(pageInactive);
		return statusList;
	}

	@GetMapping("/serviceStatus/dropdown")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	List<Map<String, Object>> serviceStatusDropDown() {
		// Create student object to hold student form data
		List<Map<String, Object>> serviceStatusList = new ArrayList<Map<String, Object>>();
		Map<String, Object> pageServiced = new HashMap<>();
		pageServiced.put("label", "Serviced");
		pageServiced.put("value", "Serviced");
		serviceStatusList.add(pageServiced);
		Map<String, Object> pageNotServiced = new HashMap<>();
		pageNotServiced.put("label", "Unserviced");
		pageNotServiced.put("value", "Unserviced");
		serviceStatusList.add(pageNotServiced);
		return serviceStatusList;
	}
	
	@GetMapping("/title/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM')")
	List<Map<String, Object>> titleDropDown() {
		// Create student object to hold student form data
		List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
		Map<String, Object> pageMr = new HashMap<>();
		pageMr.put("label", "Mr.");
		pageMr.put("value", "Mr.");
		titleList.add(pageMr);
		Map<String, Object> pageMs = new HashMap<>();
		pageMs.put("label", "Ms.");
		pageMs.put("value", "Ms.");
		titleList.add(pageMs);
		Map<String, Object> pageMrs = new HashMap<>();
		pageMrs.put("label", "Mrs.");
		pageMrs.put("value", "Mrs.");
		titleList.add(pageMrs);
		return titleList;
	}
	
	@GetMapping("/gender/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM')")
	List<Map<String, Object>> genderDropDown() {
		// Create student object to hold student form data
		List<Map<String, Object>> genderList = new ArrayList<Map<String, Object>>();
		Map<String, Object> pageMale = new HashMap<>();
		pageMale.put("label", "Male");
		pageMale.put("value", "Male");
		genderList.add(pageMale);
		Map<String, Object> pageFemale = new HashMap<>();
		pageFemale.put("label", "Female");
		pageFemale.put("value", "Female");
		genderList.add(pageFemale);
		Map<String, Object> pageOthers = new HashMap<>();
		pageOthers.put("label", "Other");
		pageOthers.put("value", "Other");
		genderList.add(pageOthers);
		return genderList;
	}
	
	@GetMapping("/maritalStatus/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM')")
	List<Map<String, Object>> maritalStatusDropDown() {
		// Create student object to hold student form data
		List<Map<String, Object>> maritalStatusList = new ArrayList<Map<String, Object>>();
		Map<String, Object> pageUnmarried = new HashMap<>();
		pageUnmarried.put("label", "UnMarried");
		pageUnmarried.put("value", "UnMarried");
		maritalStatusList.add(pageUnmarried);
		Map<String, Object> pageMarried = new HashMap<>();
		pageMarried.put("label", "Married");
		pageMarried.put("value", "Married");
		maritalStatusList.add(pageMarried);
		Map<String, Object> pageDivorcee = new HashMap<>();
		pageDivorcee.put("label", "Divorcee");
		pageDivorcee.put("value", "Divorcee");
		maritalStatusList.add(pageDivorcee);
		Map<String, Object> pageWidow = new HashMap<>();
		pageWidow.put("label", "Widow");
		pageWidow.put("value", "Widow");
		maritalStatusList.add(pageWidow);
		Map<String, Object> pageNA = new HashMap<>();
		pageNA.put("label", "NA");
		pageNA.put("value", "NA");
		maritalStatusList.add(pageNA);
		return maritalStatusList;
	}
	
	@GetMapping("/paymentMode/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM')")
	List<Map<String, Object>> paymentModeDropDown() {
		// Create student object to hold student form data
		List<Map<String, Object>> paymentModeList = new ArrayList<Map<String, Object>>();
		Map<String, Object> pageBank = new HashMap<>();
		pageBank.put("label", "Bank");
		pageBank.put("value", "Bank");
		paymentModeList.add(pageBank);
		Map<String, Object> pageCash = new HashMap<>();
		pageCash.put("label", "Cash");
		pageCash.put("value", "Cash");
		paymentModeList.add(pageCash);
		Map<String, Object> pageCheque = new HashMap<>();
		pageCheque.put("label", "Cheque");
		pageCheque.put("value", "Cheque");
		paymentModeList.add(pageCheque);
		Map<String, Object> pageDD = new HashMap<>();
		pageDD.put("label", "DD");
		pageDD.put("value", "DD");
		paymentModeList.add(pageDD);
		return paymentModeList;
	}

	@Autowired
	private HqUserRepository hqUserRepository;

	@GetMapping("/assignTso/dropdown")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	List<Map<String, Object>> assignTsoDropDown() {
		// Create student object to hold student form data
		List<User> users;
		users = hqUserRepository.findByTSO();

		List<Map<String, Object>> usersList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < users.size(); i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", users.get(i).getFullName());
			pageContent.put("value", users.get(i).getId());
			usersList.add(pageContent);
		}
		return usersList;
	}

	@GetMapping("/hqdesignation/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM','ROLE_MIS')")
	List<Map<String, Object>> hqDesignationDropDown() {
		// Create student object to hold student form data
		List<Map<String, Object>> hqDesignationList = new ArrayList<Map<String, Object>>();
		Map<String, Object> pageRSM = new HashMap<>();
		pageRSM.put("label", "RSM");
		pageRSM.put("value", "RSM");
		hqDesignationList.add(pageRSM);
		Map<String, Object> pageASM = new HashMap<>();
		pageASM.put("label", "ASM");
		pageASM.put("value", "ASM");
		hqDesignationList.add(pageASM);
		Map<String, Object> pageASE = new HashMap<>();
		pageASE.put("label", "ASE");
		pageASE.put("value", "ASE");
		hqDesignationList.add(pageASE);
		Map<String, Object> pageTSO = new HashMap<>();
		pageTSO.put("label", "TSO");
		pageTSO.put("value", "TSO");
		hqDesignationList.add(pageTSO);
		return hqDesignationList;
	}

	@GetMapping("/role/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM')")
	List<Map<String, Object>> roleDropDown() {
		// Create student object to hold student form data
		List<Map<String, Object>> roleList = new ArrayList<Map<String, Object>>();
		Map<String, Object> pageRSM = new HashMap<>();
		pageRSM.put("label", "Regional Sales Manager");
		pageRSM.put("value", "Regional Sales Manager");
		roleList.add(pageRSM);
		Map<String, Object> pageASM = new HashMap<>();
		pageASM.put("label", "Area Sales Manager");
		pageASM.put("value", "Area Sales Manager");
		roleList.add(pageASM);
		Map<String, Object> pageSrASM = new HashMap<>();
		pageSrASM.put("label", "Sr. Area Sales Manager");
		pageSrASM.put("value", "Sr. Area Sales Manager");
		roleList.add(pageSrASM);
		Map<String, Object> pageASE = new HashMap<>();
		pageASE.put("label", "Area Sales Executive");
		pageASE.put("value", "Area Sales Executive");
		roleList.add(pageASE);
		Map<String, Object> pageSrASE = new HashMap<>();
		pageSrASE.put("label", "Sr. Area Sales Executive");
		pageSrASE.put("value", "Sr. Area Sales Executive");
		roleList.add(pageSrASE);
		Map<String, Object> pageTSO = new HashMap<>();
		pageTSO.put("label", "Territory Sales Officer");
		pageTSO.put("value", "Territory Sales Officer");
		roleList.add(pageTSO);
		Map<String, Object> pageSrTSO = new HashMap<>();
		pageSrTSO.put("label", "Sr. Territory Sales Officer");
		pageSrTSO.put("value", "Sr. Territory Sales Officer");
		roleList.add(pageSrTSO);
		return roleList;
	}
	
	@Autowired
	private HqRepository hqRepository;
	
	@GetMapping("/hq/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM','ROLE_MIS')")
	public List<Map<String, Object>> hqDropDownValues() {
		// Create student object to hold student form data
		List<HqMaster> hqMasters;
		hqMasters = hqRepository.findAll();
		List<Map<String, Object>> hqList = new ArrayList<Map<String, Object>>();
		for(int i=0;i<hqMasters.size();i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", hqMasters.get(i).getHqName());
			pageContent.put("value", hqMasters.get(i).getId());
			hqList.add(pageContent);
		}
		return hqList;
	}
	
	@GetMapping("/rsm/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM')")
	public List<Map<String, Object>> rsmDropDownValues() {
		// Create student object to hold student form data
		List<User> users;
		users = hqUserRepository.findByRSM();
		List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
		for(int i=0;i<users.size();i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", users.get(i).getFullName());
			pageContent.put("value", users.get(i).getId());
			userList.add(pageContent);
		}
		return userList;
	}
	
	@GetMapping("/asm/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM')")
	public List<Map<String, Object>> asmDropDownValues() {
		// Create student object to hold student form data
		List<User> users;
		users = hqUserRepository.findByASM();
		List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
		for(int i=0;i<users.size();i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", users.get(i).getFullName());
			pageContent.put("value", users.get(i).getId());
			userList.add(pageContent);
		}
		return userList;
	}
	
	@GetMapping("/ase/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM')")
	public List<Map<String, Object>> aseDropDownValues() {
		// Create student object to hold student form data
		List<User> users;
		users = hqUserRepository.findByASE();
		List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
		for(int i=0;i<users.size();i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", users.get(i).getFullName());
			pageContent.put("value", users.get(i).getId());
			userList.add(pageContent);
		}
		return userList;
	}
}
