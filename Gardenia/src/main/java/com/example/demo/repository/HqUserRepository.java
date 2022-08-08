package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
