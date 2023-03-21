package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.BrandAssociatedToDist;
import com.example.demo.entity.StatesAssociatedToRegion;

@Repository
public interface StatesAssociatedToRegionRepository extends JpaRepository<StatesAssociatedToRegion, Long>{

	@Query(value="select * from statesAssociatedToRegion where regionId=?1",nativeQuery = true)
	List<StatesAssociatedToRegion> findByRegion(@Param("regionId") Long regionId);
	
	@Query(value = "delete from statesAssociatedToRegion where regionId=?1",nativeQuery = true)
	void deleteByRegion(@Param("regionId") Long regionId);
	
	@Transactional
	@Modifying
	@Query(value = "delete from statesAssociatedToRegion where regionId=?1 and id=?2",nativeQuery = true)
	void deleteByRegionAndId(@Param("regionId") Long distributorId,@Param("bId") Long bId);
}
