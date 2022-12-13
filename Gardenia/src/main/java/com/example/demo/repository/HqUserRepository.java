package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Distributor;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;

public interface HqUserRepository extends JpaRepository<User, Long> {
	@Query("select s.stateName from State s where s.id = ?1")
	String findByStateName(@Param("sId") Long sName);
	
	@Query("select r.regionName from Region r where r.id = ?1")
	String findByRegionName(@Param("rId") Long rName);
	
	@Query("select h.hqName from HqMaster h where h.id = ?1")
	String findByHqName(@Param("hId") Long hName);
	
	@Query("select a.areaName from Area a where a.id = ?1")
	String findByAreaName(@Param("aId") Long aName);
	
	@Query("select s.id from State s where s.stateName = ?1")
	Long findByState(@Param("sName") String stateName);
	
	@Query("select r.id from Region r where r.regionName = ?1")
	Long findByRegion(@Param("rName") String regionName);
	
	@Query("select d.id from District d where d.districtName = ?1")
	Long findByDistrict(@Param("dName") String districtName);
	
	@Query("select c.id from City c where c.cityName = ?1")
	Long findByCity(@Param("cName") String cityName);
	
	@Query("select h.id from HqMaster h where h.hqName = ?1")
	Long findByHq(@Param("hqName") String hqName);
	
	@Query("select u.id from User u where u.empCode = ?1")
	Long findByEmpCode(@Param("empCode") String empCode);
	
	@Query("select a.id from Area a where a.areaName = ?1")
	Long findByArea(@Param("aName") String areaName);
	
	@Query("select u.fullName from User u where u.id = ?1")
	String findRSMByID(@Param("rsmID") Long rsmID);
	
	@Query("select u.fullName from User u where u.id = ?1")
	String findASMByID(@Param("asmID") Long asmID);
	
	@Query("select u.fullName from User u where u.id = ?1")
	String findASEByID(@Param("aseID") Long aseID);
	
	@Query(value="select * from user where role = 'Regional Sales Manager'",nativeQuery=true)
	List<User> findByRSM();
	
	@Query(value="select * from user where role = 'Area Sales Manager'",nativeQuery=true)
	List<User> findByASM();
	
	@Query(value="select * from user where role = 'Area Sales Executive'",nativeQuery=true)
	List<User> findByASE();
	
	@Query(value="select * from user where role = 'Territory Sales Officer'",nativeQuery=true)
	List<User> findByTSO();
	
	@Query(value="select * from user where approvalStatus=?1",nativeQuery=true)
	Page<User> findByUserStatus(@Param("userStatus")Optional<String> userStatus,Pageable pageable);
	
	@Query(value="select * from user where id=?1",nativeQuery = true)
	List<User> findWithoutTransientColumns(@Param("uId")Long uId);
	
	@Transactional
	@Modifying
	@Query("update User u set u.approvalStatus=:approved where u.id=:uID")
	void updateByStatus(@Param("approved") String approved,@Param("uID") Long uID);
}
