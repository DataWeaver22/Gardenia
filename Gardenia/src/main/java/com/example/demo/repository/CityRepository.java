package com.example.demo.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.City;
 
public interface CityRepository extends JpaRepository<City, Long> {
	@Query("select d.district_name from District d where d.id = ?1")
	String findByDistrictName(@Param("dId") Long dName);
}