package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.entity.UserFileDB;
import com.example.demo.message.ResponseFile;
import com.example.demo.message.ResponseMessage;
import com.example.demo.service.UserFileStorageService;

@RestController
public class UserFileController {

	@Autowired
	  private UserFileStorageService storageService;
	  

//	  @PostMapping("/upload")
//	  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
//	    String message = "";
//	    try {
//	    	String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//		    UserFileDB FileDB = new UserFileDB(fileName, file.getContentType(), file.getBytes());
////		    FileDB.set
//		    storageService.store(FileDB);
//	      message = "Uploaded the file successfully: " + file.getOriginalFilename();
//	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
//	    } catch (Exception e) {
//	      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
//	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
//	    }
//	  }

	  @GetMapping("/userFiles")
	  @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM')")
	  public ResponseEntity<List<ResponseFile>> getListFiles() {
	    List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
	      String fileDownloadUri = ServletUriComponentsBuilder
	          .fromCurrentContextPath()
	          .path("/userFiles/")
	          .path(dbFile.getId())
	          .toUriString();

	      return new ResponseFile(
	          dbFile.getName(),
	          fileDownloadUri,
	          dbFile.getType(),
	          dbFile.getData().length);
	    }).collect(Collectors.toList());

	    return ResponseEntity.status(HttpStatus.OK).body(files);
	  }

	  @GetMapping("/userFiles/{id}")
	  @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_RSM')")
	  public ResponseEntity<byte[]> getFile(@PathVariable String id) {
	    UserFileDB fileDB = storageService.getFile(id);

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
	        .body(fileDB.getData());
	  }
}
