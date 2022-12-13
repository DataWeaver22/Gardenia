package com.example.demo.service;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.UserFileDB;
import com.example.demo.repository.UserFileDBRepository;

@Service
public class UserFileStorageService {

	@Autowired
	  private UserFileDBRepository userFileDBRepository;

	  public UserFileDB store(UserFileDB userFileDB){
	    return userFileDBRepository.save(userFileDB);
	  }
	  
	  public UserFileDB edit(UserFileDB userFileDB){
		    return userFileDBRepository.save(userFileDB);
		  }

	  public UserFileDB getFile(String id) {
	    return userFileDBRepository.findById(id).get();
	  }
	  
	  public Stream<UserFileDB> getAllFiles() {
	    return userFileDBRepository.findAll().stream();
	  }
}
