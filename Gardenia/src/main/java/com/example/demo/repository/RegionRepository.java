package com.example.demo.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Region;
 
public interface RegionRepository extends JpaRepository<Region, Long> {
	@Query("select s.state_name from State s where s.id = ?1")
	String findByStateName(@Param("sId") Long sName);
}