package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileUploaderService {
	public void uploadFile(MultipartFile file);
}
