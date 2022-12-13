package com.example.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.DistributorCode;

public interface DistributorCodeRepository extends JpaRepository<DistributorCode, Long>{
	@Query("select d.regionCode from Region d where d.id = ?1")
	String findByRegionName(@Param("dId") Long dName);
	
	@Query("select dc.id from DistributorCode dc where dc.regionCode=?1")
	String findByRegionCode(@Param("regionCodeString")String regionCodeString);
	
	@Query(value = "select * from distributorcode where regionDCId=?1",nativeQuery=true)
	DistributorCode findByRegionId(@Param("regionDCId") Long regionDCId);
	
//	@Transactional
//	@Modifying
//	@Query("update DistributorCode d set d.codeNumber=:rCodeUpdateNum where d.regionCode=:rCodeUpdateString")
//	void updateByRegionCode(@Param("rCodeUpdateNum") String rCodeUpdateNum,@Param("rCodeUpdateString") String rCodeUpdateString);
}
