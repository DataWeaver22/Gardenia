package com.example.demo.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Area;
 
public interface AreaRepository extends JpaRepository<Area, Long> {
	@Query("select cy.city_name from City cy where cy.id = ?1")
	String findByCityName(@Param("dId") Long cyName);
	
	@Query("select cy.id from City cy where cy.city_name = ?1")
	String findByCity(@Param("cName") String cityName);
}