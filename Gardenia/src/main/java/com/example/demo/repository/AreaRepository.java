package com.example.demo.repository;
 
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Area;
import com.example.demo.entity.City;
import com.example.demo.entity.State;
 
public interface AreaRepository extends JpaRepository<Area, Long> {
	@Query("select cy.cityName from City cy where cy.id = ?1")
	String findByCityName(@Param("dId") Long cyName);
	
	@Query("select cy.id from City cy where cy.cityCode = ?1")
	Long findByCity(@Param("cName") String cityName);
	
	@Query("select a from Area a where a.city.id=?1")
	List<Area> filterByCity(@Param("cityId")Optional<Long> cityId);
	
	@Query("select a from Area a where (:areaCode is null or a.areaCode Like %:areaCode%) and (:cityName is null or a.city.cityName LIKE %:cityName%) and (:areaName is null or a.areaName LIKE %:areaName%)")
	Page<Area> findByFilterParam(@Param("areaCode") Optional<String> areaCode,
			@Param("cityName") Optional<String> cityName, @Param("areaName") Optional<String> areaName,
			Pageable pageable);
}