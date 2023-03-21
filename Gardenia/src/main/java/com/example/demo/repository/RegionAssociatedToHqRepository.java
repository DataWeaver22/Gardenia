package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.RegionAssociatedToHq;
import com.example.demo.entity.StatesAssociatedToRegion;

@Repository
public interface RegionAssociatedToHqRepository extends JpaRepository<RegionAssociatedToHq, Long>{
	@Query(value="select * from regionAssociatedToHq where hqId=?1",nativeQuery = true)
	List<RegionAssociatedToHq> findByHq(@Param("hqId") Long hqId);
	
	@Query(value = "delete from regionAssociatedToHq where hqId=?1",nativeQuery = true)
	void deleteByHq(@Param("hqId") Long hqId);
	
	@Transactional
	@Modifying
	@Query(value = "delete from regionAssociatedToHq where hqId=?1 and id=?2",nativeQuery = true)
	void deleteByHqAndId(@Param("hqId") Long hqId,@Param("bId") Long bId);
}
