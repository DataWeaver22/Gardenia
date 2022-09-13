package com.example.demo.App.Service;

import java.util.List;

import com.example.demo.App.Entity.Cma1;

public interface Cma1Service {

	List<Cma1> getAllCma1();

	Cma1 saveCma1(Cma1 cma1);

	Cma1 getCma1ById(Long id);

	Cma1 editCma1(Cma1 cma1);

	void deleteCma1ById(Long id);

}
