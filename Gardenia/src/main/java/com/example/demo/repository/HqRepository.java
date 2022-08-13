package com.example.demo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.HqMaster;

public interface HqRepository extends JpaRepository<HqMaster, Long>, JpaSpecificationExecutor<HqMaster>{
	@Query("SELECT p FROM HqMaster p WHERE CONCAT(p.hq_name, ' ', p.hq_designation) LIKE %?1%")
	public List<HqMaster> search(String keyword);
}

