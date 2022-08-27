package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.repository.StateRepository;
import com.example.demo.service.IFileUploaderService;
import com.example.demo.service.StateExcelDataService;
import com.example.demo.entity.State;

@Controller
public class StateImportExcelController {
	
	@Autowired
	IFileUploaderService fileService;
	
	@Autowired
	StateExcelDataService excelservice;
	
	@Autowired
	StateRepository repo;
	
	@GetMapping("/uploadFile")
    public String index() {
        return "uploadPage";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        fileService.uploadFile(file);

        redirectAttributes.addFlashAttribute("message",
            "You have successfully uploaded '"+ file.getOriginalFilename()+"' !");
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "redirect:/state";
    }
    
    @GetMapping("/saveData")
    public String saveExcelData(Model model) {
    	
    	List<State> excelDataAsList = excelservice.getExcelDataAsList();
    	int noOfRecords = excelservice.saveExcelData(excelDataAsList);
    	model.addAttribute("noOfRecords",noOfRecords);
    	return "success";
    }
}
