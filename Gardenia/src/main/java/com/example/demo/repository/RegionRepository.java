package com.example.demo.repository;
 
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Region;
import com.example.demo.entity.State;
 
public interface RegionRepository extends JpaRepository<Region, Long> {
	@Query("select s.stateName from State s where s.id = ?1")
	String findByStateName(@Param("sId") Long sName);
	
	@Query("select s.id from State s where s.stateName = ?1")
	Long findByState(@Param("sName") String stateName);
	
	@Query("select dc.id from DistributorCode dc where dc.regionDCId.id = ?1")
	Long findDCByRegionId(@Param("rId") Long regionId);
	
	@Query("select r from Region r where r.state.id=?1")
	List<Region> filterByState(@Param("stateId")Optional<Long> stateId);
}