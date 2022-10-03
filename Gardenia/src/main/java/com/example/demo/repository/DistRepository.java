package com.example.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Distributor;

public interface DistRepository extends JpaRepository<Distributor, Long>{
	@Query("select s.state_name from State s where s.id = ?1")
	String findByStateName(@Param("sId") Long sName);
	
	@Query("select r.region_name from Region r where r.id = ?1")
	String findByRegionName(@Param("rId") Long rName);
	
	@Query("select h.hq_name from HqMaster h where h.id = ?1")
	String findByHqName(@Param("hId") Long hName);
	
	@Query("select cty.city_name from City cty where cty.id = ?1")
	String findByCityName(@Param("ctyId") Long ctyName);
	
	@Query("select s.id from State s where s.state_name = ?1")
	String findByState(@Param("sName") String stateName);
	
	@Query("select r.id from Region r where r.region_name = ?1")
	String findByRegion(@Param("rName") String regionName);
	
	@Query("select h.id from HqMaster h where h.hq_name = ?1")
	String findByHq(@Param("hqName") String hqName);
	
	@Query("select cy.id from City cy where cy.city_name = ?1")
	String findByCity(@Param("cyName") String cityName);
	
	@Query("select br.id from Product br where br.brand = ?1")
	String findByBrand(@Param("brName") String brand);
	
	@Query("select u.id from User u where u.fullName = ?1")
	String findByAssignedTSO(@Param("aTSOName") String aTSOID);
	
	@Transactional
	@Modifying
	@Query("update Distributor d set d.approval_status=:approved where d.id=:distID")
	void updateByStatus(@Param("approved") String approved,@Param("distID") Long distID);
}
