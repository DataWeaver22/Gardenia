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
	
	@Query(value = "select count(*) from distributorTable where gstin=?1",nativeQuery=true)
	Long isDistributorPresent(@Param("gstin") String gstin);
	
	@Query(value = "select count(*) from distributorTable where distributorName=?1",nativeQuery=true)
	Long isDistributorNamePresent(@Param("distributorName") String distributorName);
	
	@Query(value="select * from distributorTable where approvalStatus=?1 and regionId in ?2",nativeQuery = true)
	Page<Distributor> findByDistributorStatus(@Param("distributorStatus")Optional<String> distributorStatus,@Param("regionId")List<Long> regionId,Pageable pageable);
	
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
	
	@Query(value = "select id from hqmaster where hqName=?1",nativeQuery = true)
	Long findByHq(@Param("hqName")String hqName);
	
	@Transactional
	@Modifying
	@Query("update Distributor d set d.approvalStatus=:approved,d.distributorCode=:dCode where d.id=:distID")
	void updateStatusAndCode(@Param("approved") String approved,@Param("dCode")String dCode,@Param("distID") Long distID);
	
	@Transactional
	@Modifying
	@Query("update Distributor d set d.approvalStatus=:approved where d.id=:distID")
	void updateByStatus(@Param("approved") String approved,@Param("distID") Long distID);
	
	@Transactional
	@Modifying
	@Query("update Distributor d set d.approvalStatus=:approved,d.rejectReason=:rejectReason where d.id=:distID")
	void updateByRejectStatus(@Param("approved") String approved,@Param("rejectReason")String rejectReason,@Param("distID") Long distID);
}
