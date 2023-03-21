package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Area;
import com.example.demo.entity.HqMaster;
import com.example.demo.entity.Region;
import com.example.demo.entity.RegionAssociatedToHq;

public interface HqRepository extends JpaRepository<HqMaster, Long>, JpaSpecificationExecutor<HqMaster> {
	@Query("SELECT p FROM HqMaster p WHERE CONCAT(p.hqName, ' ', p.hqDesignation) LIKE %?1%")
	public List<HqMaster> search(String keyword);
	
	@Query(value="select * from hqmaster where id=?1",nativeQuery = true)
	List<HqMaster> findWithoutTransientColumns(@Param("dId")Long dId);

	@Query("select hq from HqMaster hq where (:hqCode is null or hq.hqCode Like %:hqCode%) and (:hqName is null or hq.hqName LIKE %:hqName%) and (:designation is null or hq.hqDesignation LIKE %:designation%)")
	Page<HqMaster> findByFilterParam(@Param("hqCode") Optional<String> hqCode, @Param("hqName") Optional<String> hqName,
			@Param("designation") Optional<String> designation, Pageable pageable);
	
	@Query("select rh from RegionAssociatedToHq rh where rh.region.id=?1")
	List<RegionAssociatedToHq> findByRegion(@Param("regionId")Optional<Long> regionId);
	
	@Query("select rh from RegionAssociatedToHq rh where rh.region in ?1")
	List<RegionAssociatedToHq> findByRegionList(@Param("regionId")List<Long> regionId);

	@Query("select hq from HqMaster hq where (:hqId is null or hq.id=:hqId) and (:designation is null or hq.hqDesignation LIKE %:designation%)")
	List<HqMaster> findByDropdownFilter(@Param("hqId") Long hqId,
			@Param("designation") Optional<String> designation);
	
	@Query("select hq from HqMaster hq where (:designation is null or hq.hqDesignation LIKE %:designation%)")
	List<HqMaster> findByDropdownParentHqFilter(@Param("designation") Optional<String> designation);
	
	@Query("select hq from HqMaster hq where (:hqId is null or hq.id=:hqId)")
	List<HqMaster> findByDropdownFilterDistributor(@Param("hqId") Long hqId);
}
