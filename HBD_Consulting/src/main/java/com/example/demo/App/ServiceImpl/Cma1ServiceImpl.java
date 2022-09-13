package com.example.demo.App.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.App.Entity.Cma1;
import com.example.demo.App.Repository.Cma1Repository;
import com.example.demo.App.Service.Cma1Service;

@Service
public class Cma1ServiceImpl implements Cma1Service{
	
	private Cma1Repository cma1Repository;

	public Cma1ServiceImpl(Cma1Repository cma1Repository) {
		super();
		this.cma1Repository = cma1Repository;
	}
	
	
	public List<Cma1> getAllCma1(){
		return cma1Repository.findAll();
	}

	public Cma1 saveCma1(Cma1 cma1) {
		return cma1Repository.save(cma1);
	}
	
	public Cma1 getCma1ById(Long id) {
		return cma1Repository.findById(id).get();
	}
	
	public Cma1 editCma1(Cma1 cma1) {
		return cma1Repository.save(cma1);
	}

	public void deleteCma1ById(Long id) {
		// TODO Auto-generated method stub
		cma1Repository.deleteById(id);
	}
}
