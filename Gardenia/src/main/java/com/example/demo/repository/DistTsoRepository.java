package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.DistNew;
public interface DistTsoRepository extends JpaRepository<DistNew, Long>{
	
}