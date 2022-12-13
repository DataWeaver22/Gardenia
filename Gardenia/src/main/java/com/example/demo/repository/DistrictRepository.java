package com.example.demo.repository;
 
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.District;
import com.example.demo.entity.State;
 
public interface DistrictRepository extends JpaRepository<District, Long> {
	@Query("select r.regionName from Region r where r.id = ?1")
	String findByRegionName(@Param("rId") Long rName);
	
	@Query("select r.id from Region r where r.regionName = ?1")
	Long findByRegion(@Param("rName") String regionName);
	
	@Query("select d from District d where d.region.id=?1")
	List<District> filterByRegion(@Param("regionId")Optional<Long> regionId);
}