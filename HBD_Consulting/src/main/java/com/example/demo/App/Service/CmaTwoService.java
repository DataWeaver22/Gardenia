package com.example.demo.App.Service;

import java.util.List;


import com.example.demo.App.Entity.CmaTwo;

public interface CmaTwoService {

	List<CmaTwo> getAllCmaTwo();

	CmaTwo saveCmaTwo(CmaTwo cmaTwo);

	CmaTwo getCmaTwoById(Long id);

	CmaTwo editCmaTwo(CmaTwo cmaTwo);

	void deleteCmaTwoById(Long id);

}
