package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.DistributorCode;

public interface DistributorCodeRepository extends JpaRepository<DistributorCode, Long>{
	@Query("select d.region_code from Region d where d.id = ?1")
	String findByRegionName(@Param("dId") Long dName);
}
