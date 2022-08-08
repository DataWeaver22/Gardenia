package com.example.demo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.HqMaster;

public interface HqRepository extends JpaRepository<HqMaster, Long>{
	@Query("SELECT hq FROM HqMaster hq WHERE CONCAT(hq.hq_name, hq.hq_designation) LIKE %?1%")
	public List<HqMaster> search(String keyword);
}

