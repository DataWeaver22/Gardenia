package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.example.demo.entity.RegionAssociatedToHq;
import com.example.demo.entity.User;
import com.example.demo.repository.DistRepository;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.repository.HqRepository;
import com.example.demo.repository.HqUserRepository;
import com.example.demo.repository.RegionAssociatedToHqRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.service.CategoryService;

@RestController
public class DropdownController {
	
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/categoryBrand/dropdown")
	@PreAuthorize("hasAuthority('ROLE_MIS')")
	public List<Map<String, Object>> CategorydropDownValues() {
		List<Category> categories;
		categories = categoryService.getAllCategories();
		List<Map<String, Object>> categoriesList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < categories.size(); i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", categories.get(i).getBrand().getBrandName() + " - " +  categories.get(i).getCategoryName() );
			pageContent.put("value", categories.get(i).getId());
			categoriesList.add(pageContent);
		}
		return categoriesList;
	}

	@GetMapping("/uom/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_PRODUCTAPPROVER','ROLE_PRODUCT')")
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
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_USER','ROLE_RSM','ROLE_DISTAPPROVER','ROLE_PRODUCTAPPROVER','ROLE_PRODUCT')")
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
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_RSM','ROLE_DISTAPPROVER')")
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

	@GetMapping("/employeeName/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM')")
	List<Map<String, Object>> employeeNameDropDown(@RequestParam(required = false) Optional<Long> regionId,
			@RequestParam(required = false) Optional<String> role,
			@RequestParam(required = false) Optional<Long> hqId) {
		// Create student object to hold student form data

		if (role.equals(Optional.of("ASM")) || role.equals(Optional.of("Sr ASM"))
				|| role.equals(Optional.of("Dy ASM"))) {
			role = Optional.of("ASM");
		} else if (role.equals(Optional.of("ASE")) || role.equals(Optional.of("Sr ASE"))
				|| role.equals(Optional.of("Dy ASE")) || role.equals(Optional.of("Sr TSO"))) {
			role = Optional.of("ASE");
		} else if (role.equals(Optional.of("RSM")) || role.equals(Optional.of("Sr RSM"))
				|| role.equals(Optional.of("Dy RSM"))) {
			role = Optional.of("RSM");
		} else if (role.equals(Optional.of("TSO")) || role.equals(Optional.of("SR"))) {
			role = Optional.of("TSO");
		} else if (role.equals(Optional.of("ZSM"))) {
			role = Optional.of("ZSM");
		}
		List<Map<String, Object>> usersList = new ArrayList<Map<String, Object>>();
		if (role.equals(Optional.of("ZSM"))) {
			// RSM
			role = Optional.of("RSM");
			List<User> usersRSM;
			usersRSM = hqUserRepository.findByDropdownFilter(regionId, role);
			if (usersRSM.size() > 0) {
				for (int i = 0; i < usersRSM.size(); i++) {
					Map<String, Object> pageContent = new HashMap<>();
					pageContent.put("employee", usersRSM.get(i).getId());
					pageContent.put("employeeName", usersRSM.get(i).getFullName());
					pageContent.put("designation", usersRSM.get(i).getRole());
					pageContent.put("hq", usersRSM.get(i).getHqMaster().getId());
					pageContent.put("hqName", usersRSM.get(i).getHqMaster().getHqName());
					usersList.add(pageContent);
				}
			}
			// ASM
			role = Optional.of("ASM");
			List<User> usersASM;
			usersASM = hqUserRepository.findByDropdownFilter(regionId, role);
			if (usersASM.size() > 0) {
				for (int i = 0; i < usersASM.size(); i++) {
					Map<String, Object> pageContent = new HashMap<>();
					pageContent.put("employee", usersASM.get(i).getId());
					pageContent.put("employeeName", usersASM.get(i).getFullName());
					pageContent.put("designation", usersASM.get(i).getRole());
					pageContent.put("hq", usersASM.get(i).getHqMaster().getId());
					pageContent.put("hqName", usersASM.get(i).getHqMaster().getHqName());
					usersList.add(pageContent);
				}
			}
			// ASE
			role = Optional.of("ASE");
			List<User> usersASE;
			usersASE = hqUserRepository.findByDropdownFilter(regionId, role);
			if (usersASE.size() > 0) {
				for (int i = 0; i < usersASE.size(); i++) {
					Map<String, Object> pageContent = new HashMap<>();
					pageContent.put("employee", usersASE.get(i).getId());
					pageContent.put("employeeName", usersASE.get(i).getFullName());
					pageContent.put("designation", usersASE.get(i).getRole());
					pageContent.put("hq", usersASE.get(i).getHqMaster().getId());
					pageContent.put("hqName", usersASE.get(i).getHqMaster().getHqName());
					usersList.add(pageContent);
				}
			}
			// TSO
			role = Optional.of("TSO");
			List<User> usersTSO;
			usersTSO = hqUserRepository.findByDropdownFilter(regionId, role);
			if (usersTSO.size() > 0) {
				for (int i = 0; i < usersTSO.size(); i++) {
					Map<String, Object> pageContent = new HashMap<>();
					pageContent.put("employee", usersTSO.get(i).getId());
					pageContent.put("employeeName", usersTSO.get(i).getFullName());
					pageContent.put("designation", usersTSO.get(i).getRole());
					pageContent.put("hq", usersTSO.get(i).getHqMaster().getId());
					pageContent.put("hqName", usersTSO.get(i).getHqMaster().getHqName());
					usersList.add(pageContent);
				}
			}
		} else if (role.equals(Optional.of("RSM"))) {
			// ASM
			role = Optional.of("ASM");
			System.out.println(role);
			List<User> usersASM;
			usersASM = hqUserRepository.findByDropdownFilter(regionId, role);
			if (usersASM.size() > 0) {
				for (int i = 0; i < usersASM.size(); i++) {
					Map<String, Object> pageContent = new HashMap<>();
					pageContent.put("employee", usersASM.get(i).getId());
					pageContent.put("employeeName", usersASM.get(i).getFullName());
					pageContent.put("designation", usersASM.get(i).getRole());
					pageContent.put("hq", usersASM.get(i).getHqMaster().getId());
					pageContent.put("hqName", usersASM.get(i).getHqMaster().getHqName());
					usersList.add(pageContent);
				}
			}
			// ASE
			role = Optional.of("ASE");
			List<User> usersASE;
			usersASE = hqUserRepository.findByDropdownFilter(regionId, role);
			if (usersASE.size() > 0) {
				for (int i = 0; i < usersASE.size(); i++) {
					Map<String, Object> pageContent = new HashMap<>();
					pageContent.put("employee", usersASE.get(i).getId());
					pageContent.put("employeeName", usersASE.get(i).getFullName());
					pageContent.put("designation", usersASE.get(i).getRole());
					pageContent.put("hq", usersASE.get(i).getHqMaster().getId());
					pageContent.put("hqName", usersASE.get(i).getHqMaster().getHqName());
					usersList.add(pageContent);
				}
			}
			// TSO
			role = Optional.of("TSO");
			List<User> usersTSO;
			usersTSO = hqUserRepository.findByDropdownFilter(regionId, role);
			if (usersTSO.size() > 0) {
				for (int i = 0; i < usersTSO.size(); i++) {
					Map<String, Object> pageContent = new HashMap<>();
					pageContent.put("employee", usersTSO.get(i).getId());
					pageContent.put("employeeName", usersTSO.get(i).getFullName());
					pageContent.put("designation", usersTSO.get(i).getRole());
					pageContent.put("hq", usersTSO.get(i).getHqMaster().getId());
					pageContent.put("hqName", usersTSO.get(i).getHqMaster().getHqName());
					usersList.add(pageContent);
				}
			}
		} else if (role.equals(Optional.of("ASM"))) {
			// ASE
			role = Optional.of("ASE");
			List<User> usersASE;
			usersASE = hqUserRepository.findByDropdownFilter(regionId, role);
			if (usersASE.size() > 0) {
				for (int i = 0; i < usersASE.size(); i++) {
					Map<String, Object> pageContent = new HashMap<>();
					pageContent.put("employee", usersASE.get(i).getId());
					pageContent.put("employeeName", usersASE.get(i).getFullName());
					pageContent.put("designation", usersASE.get(i).getRole());
					pageContent.put("hq", usersASE.get(i).getHqMaster().getId());
					pageContent.put("hqName", usersASE.get(i).getHqMaster().getHqName());
					usersList.add(pageContent);
				}
			}
			// TSO
			role = Optional.of("TSO");
			List<User> usersTSO;
			usersTSO = hqUserRepository.findByDropdownFilter(regionId, role);
			if (usersTSO.size() > 0) {
				for (int i = 0; i < usersTSO.size(); i++) {
					Map<String, Object> pageContent = new HashMap<>();
					pageContent.put("employee", usersTSO.get(i).getId());
					pageContent.put("employeeName", usersTSO.get(i).getFullName());
					pageContent.put("designation", usersTSO.get(i).getRole());
					pageContent.put("hq", usersTSO.get(i).getHqMaster().getId());
					pageContent.put("hqName", usersTSO.get(i).getHqMaster().getHqName());
					usersList.add(pageContent);
				}
			}
		} else if (role.equals(Optional.of("ASE"))) {
			// TSO
			role = Optional.of("TSO");
			List<User> usersTSO;
			usersTSO = hqUserRepository.findByDropdownFilter(regionId, role);
			if (usersTSO.size() > 0) {
				for (int i = 0; i < usersTSO.size(); i++) {
					Map<String, Object> pageContent = new HashMap<>();
					pageContent.put("employee", usersTSO.get(i).getId());
					pageContent.put("employeeName", usersTSO.get(i).getFullName());
					pageContent.put("designation", usersTSO.get(i).getRole());
					pageContent.put("hq", usersTSO.get(i).getHqMaster().getId());
					pageContent.put("hqName", usersTSO.get(i).getHqMaster().getHqName());
					usersList.add(pageContent);
				}
			}
		} else {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", "Vacant");
			pageContent.put("value", "Vacant");
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
		Map<String, Object> pageZSM = new HashMap<>();
		pageZSM.put("label", "ZSM");
		pageZSM.put("value", "ZSM");
		roleList.add(pageZSM);
		Map<String, Object> pageSrRSM = new HashMap<>();
		pageSrRSM.put("label", "Sr RSM");
		pageSrRSM.put("value", "Sr RSM");
		roleList.add(pageSrRSM);
		Map<String, Object> pageRSM = new HashMap<>();
		pageRSM.put("label", "RSM");
		pageRSM.put("value", "RSM");
		roleList.add(pageRSM);
		Map<String, Object> pageDyRSM = new HashMap<>();
		pageDyRSM.put("label", "Dy RSM");
		pageDyRSM.put("value", "Dy RSM");
		roleList.add(pageDyRSM);
		Map<String, Object> pageSrASM = new HashMap<>();
		pageSrASM.put("label", "Sr ASM");
		pageSrASM.put("value", "Sr ASM");
		roleList.add(pageSrASM);
		Map<String, Object> pageASM = new HashMap<>();
		pageASM.put("label", "ASM");
		pageASM.put("value", "ASM");
		roleList.add(pageASM);
		Map<String, Object> pageDyASM = new HashMap<>();
		pageDyASM.put("label", "Dy ASM");
		pageDyASM.put("value", "Dy ASM");
		roleList.add(pageDyASM);
		Map<String, Object> pageSrASE = new HashMap<>();
		pageSrASE.put("label", "Sr ASE");
		pageSrASE.put("value", "Sr ASE");
		roleList.add(pageSrASE);
		Map<String, Object> pageASE = new HashMap<>();
		pageASE.put("label", "ASE");
		pageASE.put("value", "ASE");
		roleList.add(pageASE);
		Map<String, Object> pageDyASE = new HashMap<>();
		pageDyASE.put("label", "Dy ASE");
		pageDyASE.put("value", "Dy ASE");
		roleList.add(pageDyASE);
		Map<String, Object> pageSrTSO = new HashMap<>();
		pageSrTSO.put("label", "Sr TSO");
		pageSrTSO.put("value", "Sr TSO");
		roleList.add(pageSrTSO);
		Map<String, Object> pageTSO = new HashMap<>();
		pageTSO.put("label", "TSO");
		pageTSO.put("value", "TSO");
		roleList.add(pageTSO);
		Map<String, Object> pageSR = new HashMap<>();
		pageSR.put("label", "SR");
		pageSR.put("value", "SR");
		roleList.add(pageSR);
		return roleList;
	}

	@GetMapping("/typeOfDistributor/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM','ROLE_MIS','ROLE_DISTAPPROVER')")
	List<Map<String, Object>> typeOfDistDropDown() {
		// Create student object to hold student form data
		List<Map<String, Object>> typeOfDistList = new ArrayList<Map<String, Object>>();
		Map<String, Object> pageCSA = new HashMap<>();
		pageCSA.put("label", "CSA");
		pageCSA.put("value", "CSA");
		typeOfDistList.add(pageCSA);
		Map<String, Object> pageRetail = new HashMap<>();
		pageRetail.put("label", "Retail");
		pageRetail.put("value", "Retail");
		typeOfDistList.add(pageRetail);
		Map<String, Object> pageSS = new HashMap<>();
		pageSS.put("label", "SS");
		pageSS.put("value", "SS");
		typeOfDistList.add(pageSS);
		Map<String, Object> pageCAndF = new HashMap<>();
		pageCAndF.put("label", "C&F");
		pageCAndF.put("value", "C&F");
		typeOfDistList.add(pageCAndF);
		Map<String, Object> pageWholesale = new HashMap<>();
		pageWholesale.put("label", "Wholesale");
		pageWholesale.put("value", "Wholesale");
		typeOfDistList.add(pageWholesale);
		Map<String, Object> pageMT = new HashMap<>();
		pageMT.put("label", "Modern Trade");
		pageMT.put("value", "Modern Trade");
		typeOfDistList.add(pageMT);
		Map<String, Object> pageEComm = new HashMap<>();
		pageEComm.put("label", "E-Commerce");
		pageEComm.put("value", "E-Commerce");
		typeOfDistList.add(pageEComm);
		Map<String, Object> pageInstitution = new HashMap<>();
		pageInstitution.put("label", "Instituion");
		pageInstitution.put("value", "Institution");
		typeOfDistList.add(pageInstitution);
		Map<String, Object> pageCost = new HashMap<>();
		pageCost.put("label", "Cost");
		pageCost.put("value", "Cost");
		typeOfDistList.add(pageCost);
		Map<String, Object> pageExport = new HashMap<>();
		pageExport.put("label", "Export");
		pageExport.put("value", "Export");
		typeOfDistList.add(pageExport);
		return typeOfDistList;
	}

	@Autowired
	private HqRepository hqRepository;

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private RegionAssociatedToHqRepository regionAssociatedToHqRepository;

	@GetMapping("/hq/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM','ROLE_MIS','ROLE_DISTAPPROVER')")
	public List<Map<String, Object>> hqDropDownValues(@RequestParam(required = false) Optional<Long> regionId,
			@RequestParam(required = false) Optional<String> role) {
		// Create student object to hold student form data

		if (role.equals(Optional.of("ASM")) || role.equals(Optional.of("Sr ASM"))
				|| role.equals(Optional.of("Dy ASM"))) {
			role = Optional.of("ASM");
		} else if (role.equals(Optional.of("ASE")) || role.equals(Optional.of("Sr ASE"))
				|| role.equals(Optional.of("Dy ASE")) || role.equals(Optional.of("Sr TSO"))) {
			role = Optional.of("ASE");
		} else if (role.equals(Optional.of("RSM")) || role.equals(Optional.of("Sr RSM"))
				|| role.equals(Optional.of("Dy RSM"))) {
			role = Optional.of("RSM");
		} else if (role.equals(Optional.of("TSO")) || role.equals(Optional.of("SR"))) {
			role = Optional.of("TSO");
		} else if (role.equals(Optional.of("ZSM"))) {
			role = Optional.of("ZSM");
		}
		List<HqMaster> hqMasters;
		List<Map<String, Object>> hqList = new ArrayList<Map<String, Object>>();
		List<RegionAssociatedToHq> regionAssociatedToHqs;
		regionAssociatedToHqs = hqRepository.findByRegion(regionId);
		for (int i = 0; i < regionAssociatedToHqs.size(); i++) {
			Long hqId = regionAssociatedToHqs.get(i).getHqMaster().getId();
			hqMasters = hqRepository.findByDropdownFilter(hqId, role);

			for (int j = 0; j < hqMasters.size(); j++) {
				Map<String, Object> pageContent = new HashMap<>();
				pageContent.put("label", hqMasters.get(j).getHqName());
				pageContent.put("value", hqMasters.get(j).getId());
				hqList.add(pageContent);
			}
		}
		return hqList;
	}

	@GetMapping("/parentHq/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM','ROLE_MIS')")
	public List<Map<String, Object>> hqParentDropDownValues(@RequestParam(required = false) Optional<String> role) {
		// Create student object to hold student form data

		if (role.equals(Optional.of("ASM")) || role.equals(Optional.of("Sr ASM"))
				|| role.equals(Optional.of("Dy ASM"))) {
			role = Optional.of("RSM");
		} else if (role.equals(Optional.of("ASE")) || role.equals(Optional.of("Sr ASE"))
				|| role.equals(Optional.of("Dy ASE")) || role.equals(Optional.of("Sr TSO"))) {
			role = Optional.of("ASM");
		} else if (role.equals(Optional.of("TSO")) || role.equals(Optional.of("SR"))) {
			role = Optional.of("ASE");
		}

		List<HqMaster> hqMasters;
		List<Map<String, Object>> hqList = new ArrayList<Map<String, Object>>();
		hqMasters = hqRepository.findByDropdownParentHqFilter(role);

		for (int j = 0; j < hqMasters.size(); j++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", hqMasters.get(j).getHqName());
			pageContent.put("value", hqMasters.get(j).getId());
			hqList.add(pageContent);
		}
		return hqList;
	}

	@GetMapping("/hqDistributor/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM','ROLE_MIS','ROLE_DISTAPPROVER')")
	public List<Map<String, Object>> hqDropDownValuesDistributor(
			@RequestParam(required = false) Optional<Long> regionId) {
		// Create student object to hold student form data

		List<HqMaster> hqMasters;
		List<Map<String, Object>> hqList = new ArrayList<Map<String, Object>>();
		List<RegionAssociatedToHq> regionAssociatedToHqs;
		regionAssociatedToHqs = hqRepository.findByRegion(regionId);
		for (int i = 0; i < regionAssociatedToHqs.size(); i++) {
			Long hqId = regionAssociatedToHqs.get(i).getHqMaster().getId();
			hqMasters = hqRepository.findByDropdownFilterDistributor(hqId);

			for (int j = 0; j < hqMasters.size(); j++) {
				Map<String, Object> pageContent = new HashMap<>();
				pageContent.put("label", hqMasters.get(j).getHqName());
				pageContent.put("value", hqMasters.get(j).getId());
				hqList.add(pageContent);
			}
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
		for (int i = 0; i < users.size(); i++) {
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
		for (int i = 0; i < users.size(); i++) {
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
		for (int i = 0; i < users.size(); i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", users.get(i).getFullName());
			pageContent.put("value", users.get(i).getId());
			userList.add(pageContent);
		}
		return userList;
	}

	@GetMapping("/branch/dropdown")
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM')")
	public List<Map<String, Object>> listBranch() {
		List<Map<String, Object>> branchList = new ArrayList<Map<String, Object>>();
		List<String> arrBranch = Arrays.asList("Mumbai", "Raigad", "Guwahati", "Ghaziabad", "Vellore", "New Delhi",
				"South Kolkata", "Kashipur", "Sultanpur", "Sitapur", "Porbandar", "Ongole", "Thiyagaraya Nagar",
				"Tambaram", "North Delhi", "Kottakkal", "North Kolkata", "Nalgonda", "Alappuzha", "Nalbari",
				"Nizamabad", "Sahibabad", "Mancherial", "Barpeta Road", "Latur", "Satara", "Nagercoil", "Malegaon",
				"Pali", "Khandwa", "Ayodhya", "Surendranagar", "Abohar", "Ajmer", "Thanjavur", "Kurnool", "Kadapa",
				"Himatnagar", "Shimla", "Valsad", "Godhra", "Amreli", "Dindigul", "Akola", "Chandrapur", "Srikakulam",
				"Visakhapatnam", "Gulbarga_2", "Aluva_2", "Katihar", "Bangalore1", "Bhubaneshwar", "Krishnagiri",
				"Jodhpur_2", "Nedumangad", "Kharagpur", "Pathanamthitta", "Ratlam", "Serampore", "Erode_2", "Darbhanga",
				"Kasaragod_2", "Angamaly", "Jorhat_2", "Sonarpur", "Sirsa_2", "Kanchipuram_2", "Rourkela", "Shahibabad",
				"kurukshetra", "Faizabad", "Bhuj_2", "Vellore_2", "Samastipur", "Gangtok", "Dhanbad_2", "Jamshedpur",
				"Bardhaman", "Raigad_2", "Imphal_2", "Cochin", "Kumbakonam_2", "Muzaffarnagar_2", "Yamunanagar",
				"Hamirpur", "Aligarh_2", "Durgapur", "Noida_2", "Silchar_2", "Hissar", "Faridabad_2", "Bilaspur_2",
				"Kottayam_2", "Panchakula", "Fazilka", "Bangalore_2", "Delhi", "Alibaug (Raigad)_2", "Parwanoo",
				"Patna_2", "Muzzafarpur", "South Delhi", "East Delhi", "Secunderabad", "Rohini", "Gurgaon_2", "Purnea",
				"Bhubuneshwar_2", "Lucknow_2", "Banglore", "Kankavli_2", "New Delhi_2", "West Delhi", "Tohana",
				"Karnal", "Panipat_2", "Sohna", "Cuttack", "Tirupathi", "Tezpur_2", "Tatanagar", "Ranchi_2",
				"Tata Nagar", "Rohtak", "Ambala_2", "Siliguri_2", "Hugali", "Howrah_2", "Malda", "Guwahati_2",
				"Agartala_2", "Jhodhpur", "Solan", "Mandi", "Kangra_2", "Kolkata_2", "W. Bengal", "Jaipur_2", "Kota_2",
				"Hanumangarh", "Udaipur_2", "Bikaner_2", "Alwar", "Amritsar_2", "Mohali", "Bhatinda_2", "Jalandhar_2",
				"Moga", "Chandigarh_2", "Moradabad_2", "Jammu_2", "Srinagar_2", "Ludhiana_2", "Firojpur", "Patiala_2",
				"Varanasi_2", "Haldwani_2", "Ghaziabad_2", "Allahabad_2", "Gorakhpur_2", "Dehradun_2", "Kanpur_2",
				"West U.P&Uttaranchal", "Agra_2", "Saharanpur", "Barielly_2", "Meerut_2", "Rajahmundry", "Nellore",
				"Vijayawada_2", "Warangal_2", "Anantapur_2", "Azamgarh", "Mangalore_2", "Shimoga_2", "Mysore_2",
				"Hyderabad_2", "Guntur", "Vizag_2", "EKM", "Palakkad_2", "Malappuram_2", "Hubli_2", "Belgaum_2",
				"Davangere", "Ernakulam_2", "Thrissur/Palakkad", "Kannur_2", "Trivandrum_2", "Kollam_2", "Alleppy_2",
				"Junagadh_2", "Jamnagar_2", "Anand_2", "Baroda_2", "Kerala", "Calicut_2", "Jabalpur_2", "Bhavnagar_2",
				"Ahmedabad_2", "Rajkot_2", "Surat_2", "Mehsana_2", "Salem_2", "Coimbatore_2", "Trichy_2", "Indore_2",
				"Bhopal_2", "Gwalior_2", "Sangli_2", "Nashik_2", "Raipur_2", "Chennai_2", "Pondicherry_2", "Madurai_2",
				"Pune_2", "Goa_2", "PCMC_2", "Ahmednagar_2", "Kolhapur_2", "Solapur_2", "Nagpur_2", "Jalgaon_2",
				"Amravati_2", "Dhule_2", "Nanded_2", "Aurangabad_2", "Bhagalpur", "Ujjain", "Mumbai_2", "New Mumbai",
				"Modern Trade_2", "Kudal (Sindhudurg)_2", "Chengalpattu", "Perambur", "Vadapalani", "Bilaspur",
				"Midnapore", "Ratnagiri", "Kottayam", "Cuddalore", "North Chennai", "Viluppuram", "Anna Nagar",
				"Bhavnagar", "Kanchipuram", "Erode", "Wayanad", "Kumbakonam", "Mandya", "Tiruvottiyur", "Muzaffarpur",
				"Shimoga", "Shillong", "Rudrapur", "Warangal", "Dharmapuri", "Mehsana", "Bhuj", "Bhopal", "Bikaner",
				"Gulbarga", "Belgaum", "Mangalore", "Vijayawada", "Indore", "Bohpal", "Surat", "Jamnagar", "Trivandrum",
				"Thrissur", "Kasaragod", "Kollam", "Kannur", "Vizag", "Kudal (Sindhudurg)", "Modern Trade", "Nanded",
				"Salem", "Malappuram", "Ernakulam", "Nashik", "PCMC", "Pune", "Sangli", "Solapur", "Kankavli",
				"Alibaug (Raigad)", "Amravati", "Dhule", "Goa", "Kolhapur", "Nagpur", "Allahabad", "Noida", "Barielly",
				"Gorakhpur", "Dehradun", "Ahmednagar", "Amritsar", "Jalandhar", "Bathinda", "Udaipur", "Jaipur",
				"Muzaffarnagar", "Tezpur", "Jorhat", "Silchar", "Jammu", "Srinagar", "Patiala", "Bhubaneswar", "Ranchi",
				"Kangra", "Howrah", "Hooghly", "Imphal", "Kolkata", "Haldwani", "Gurgaon", "Sirsa", "Faridabad",
				"Patna", "Kanpur", "Jhansi", "Ludhiana", "Aligarh", "Jalgaon", "Aizawl", "Madurai", "Meerut", "Jodhpur",
				"Tirupati", "Firozpur", "Siliguri", "24 Parganas", "Panipat", "Anantapur", "Kota", "Moradabad",
				"Tirunelveli", "Coimbatore", "Lucknow", "Pondicherry", "Hyderabad", "Trichy", "Agartala",
				"Bhubuneshwar", "Aluva", "Calicut", "Palakkad", "Chandigarh", "Chennai", "Hubli", "Mysore", "Agra",
				"Gwalior", "Jabalpur", "Raipur", "Alleppy", "Ambala", "Bhatinda", "Anand", "Aurangabad", "Bangalore",
				"Dhanbad", "Ahmedabad", "Baroda", "Junagadh", "Rajkot", "Varanasi");
		for (int i = 0; i < arrBranch.size(); i++) {
			Map<String, Object> pageContent = new HashMap<>();
			pageContent.put("label", arrBranch.get(i));
			pageContent.put("value", arrBranch.get(i));
			branchList.add(pageContent);
		}
		return branchList;
	}
}
