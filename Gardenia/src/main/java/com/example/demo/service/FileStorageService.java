package com.example.demo.service;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.FileDB;
import com.example.demo.repository.FileDBRepository;

@Service
public class FileStorageService {

	@Autowired
	  private FileDBRepository fileDBRepository;

	  public FileDB store(FileDB fileDB){
	    return fileDBRepository.save(fileDB);
	  }
	  
	  public FileDB edit(FileDB fileDB){
		    return fileDBRepository.save(fileDB);
		  }

	  public FileDB getFile(String id) {
	    return fileDBRepository.findById(id).get();
	  }
	  
	  public Stream<FileDB> getAllFiles() {
	    return fileDBRepository.findAll().stream();
	  }
}
