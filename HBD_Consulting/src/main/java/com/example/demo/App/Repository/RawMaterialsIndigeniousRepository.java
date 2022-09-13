package com.example.demo.App.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.App.Entity.RawMaterialsIndigenious;

public interface RawMaterialsIndigeniousRepository extends JpaRepository<RawMaterialsIndigenious, Long>{
	
}
