package com.example.demo.repository;
 
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.City;
import com.example.demo.entity.State;
 
public interface CityRepository extends JpaRepository<City, Long> {
	@Query("select d.districtName from District d where d.id = ?1")
	String findByDistrictName(@Param("dId") Long dName);
	
	@Query("select d.id from District d where d.districtCode = ?1")
	Long findByDistrict(@Param("cName") String districtName);
	
	@Query("select c from City c where c.district.id=?1")
	List<City> filterByDistrict(@Param("districtId")Optional<Long> districtId);
}