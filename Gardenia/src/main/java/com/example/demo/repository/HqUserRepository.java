package com.example.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.User;

public interface HqUserRepository extends JpaRepository<User, Long> {
	@Query("select s.state_name from State s where s.id = ?1")
	String findByStateName(@Param("sId") Long sName);
	
	@Query("select r.region_name from Region r where r.id = ?1")
	String findByRegionName(@Param("rId") Long rName);
	
	@Query("select h.hq_name from HqMaster h where h.id = ?1")
	String findByHqName(@Param("hId") Long hName);
	
	@Query("select a.area_name from Area a where a.id = ?1")
	String findByAreaName(@Param("aId") Long aName);
	
	@Query("select s.id from State s where s.state_name = ?1")
	String findByState(@Param("sName") String stateName);
	
	@Query("select r.id from Region r where r.region_name = ?1")
	String findByRegion(@Param("rName") String regionName);
	
	@Query("select h.id from HqMaster h where h.hq_name = ?1")
	String findByHq(@Param("hqName") String hqName);
	
	@Query("select a.id from Area a where a.area_name = ?1")
	String findByArea(@Param("aName") String areaName);
	
	@Transactional
	@Modifying
	@Query("update User u set u.approval_status=:approved where u.id=:uID")
	void updateByStatus(@Param("approved") String approved,@Param("uID") Long uID);
}
