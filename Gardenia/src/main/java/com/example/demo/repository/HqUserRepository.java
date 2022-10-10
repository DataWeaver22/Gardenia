package com.example.demo.repository;

import java.util.List;

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
	
	@Query("select u.fullName from User u where u.id = ?1")
	String findRSMByID(@Param("rsmID") Long rsmID);
	
	@Query("select u.fullName from User u where u.id = ?1")
	String findASMByID(@Param("asmID") Long asmID);
	
	@Query("select u.fullName from User u where u.id = ?1")
	String findASEByID(@Param("aseID") Long aseID);
	
	@Query(value="select * from User u where u.roles = 'Regional Sales Manager'",nativeQuery=true)
	List<User> findByRSM();
	
	@Query(value="select * from User u where u.roles = 'Area Sales Manager'",nativeQuery=true)
	List<User> findByASM();
	
	@Query(value="select * from User u where u.roles = 'Area Sales Executive'",nativeQuery=true)
	List<User> findByASE();
	
	@Transactional
	@Modifying
	@Query("update User u set u.approval_status=:approved where u.id=:uID")
	void updateByStatus(@Param("approved") String approved,@Param("uID") Long uID);
}
