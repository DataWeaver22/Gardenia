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

public interface DistRepository extends JpaRepository<Distributor, Long>{
	
	@Query("select d from Distributor d where d.approvalStatus=?1")
	Page<Distributor> findByDistributorStatus(@Param("distributorStatus")Optional<String> distributorStatus,Pageable pageable);
	
	@Query(value="select * from distributorTable where id=?1",nativeQuery = true)
	List<Distributor> findWithoutTransientColumns(@Param("dId")Long dId);
	
	@Query("select s.id from State s where s.stateName = ?1")
	Long findByState(@Param("sName") String stateName);
	
	@Query("select r.id from Region r where r.regionName = ?1")
	Long findByRegion(@Param("rName") String regionName);
	
	@Query("select d.id from District d where d.districtName = ?1")
	Long findByDistrict(@Param("dName") String districtName);
	
	@Query("select c.id from City c where c.cityName = ?1")
	Long findByCity(@Param("cName") String cityName);
	
	@Query("select u.id from User u where u.empCode = ?1")
	Long findByEmpCode(@Param("empCode") String empCode);
	
	@Transactional
	@Modifying
	@Query("update Distributor d set d.approvalStatus=:approved where d.id=:distID")
	void updateByStatus(@Param("approved") String approved,@Param("distID") Long distID);
}
