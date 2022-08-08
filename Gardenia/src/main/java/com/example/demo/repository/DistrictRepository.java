package com.example.demo.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.District;
 
public interface DistrictRepository extends JpaRepository<District, Long> {
	@Query("select r.region_name from Region r where r.id = ?1")
	String findByRegionName(@Param("rId") Long rName);
}