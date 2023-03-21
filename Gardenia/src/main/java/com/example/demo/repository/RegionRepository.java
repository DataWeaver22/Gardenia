package com.example.demo.repository;
 
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Distributor;
import com.example.demo.entity.Region;
import com.example.demo.entity.State;
import com.example.demo.entity.StatesAssociatedToRegion;
 
public interface RegionRepository extends JpaRepository<Region, Long> {
	@Query("select s.stateName from State s where s.id = ?1")
	String findByStateName(@Param("sId") Long sName);
	
	@Query("select s.id from State s where s.stateName = ?1")
	Long findByState(@Param("sName") String stateName);
	
	@Query("select hq.id from HqMaster hq where hq.hqName = ?1")
	Long findByHq(@Param("hqName") String hqName);
	
	@Query("select r.id from Region r where r.regionName = ?1")
	Long findByRegionName(@Param("rName") String regionName);
	
	@Query("select dc.id from DistributorCode dc where dc.regionDCId.id = ?1")
	Long findDCByRegionId(@Param("rId") Long regionId);
	
	@Query("select r.id from Region r where r.regionName = ?1")
	Optional<Long> findByRegion(@Param("rName") Optional<String> regionName);
	
	@Query("select sr from StatesAssociatedToRegion sr where sr.state.id=?1")
	List<StatesAssociatedToRegion> filterByState(@Param("stateId")Optional<Long> stateId);
	
	@Query(value = "select * from statesAssociatedToRegion",nativeQuery = true)
	List<StatesAssociatedToRegion> getDistinctStateAssociated();
	
	@Query("select s from StatesAssociatedToRegion s where s.state.id=?1")
	List<StatesAssociatedToRegion> filterByStateId(@Param("stateId")Optional<Long> stateId);
	
	@Query(value="select count(id) from region where regionCode=?1",nativeQuery = true)
	Long regionCount(@Param("regionCode")String regionCode);
	
	@Query(value="select * from region where id=?1",nativeQuery = true)
	List<Region> findWithoutTransientColumns(@Param("dId")Long dId);
	
	@Query("select r from Region r where (:regionCode is null or r.regionCode Like %:regionCode%) and (:regionName is null or r.regionName LIKE %:regionName%)")
	Page<Region> findByFilterParam(@Param("regionCode") Optional<String> regionCode, @Param("regionName") Optional<String> regionName,
			Pageable pageable);
}