package com.example.demo.App.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.App.Entity.CmaTwo;

public interface CmaTwoRepository extends JpaRepository<CmaTwo, Long> {
	
	
}
