package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.RSMAssociatedRegion;

@Repository
public interface RSMAssociatedRegionRepository extends JpaRepository<RSMAssociatedRegion, Long>{

	@Query(value = "select * from rsmAssociatedRegion where loginId=?1",nativeQuery = true)
	List<RSMAssociatedRegion> findByLoginId(@Param("loginId") Long loginId);
}
