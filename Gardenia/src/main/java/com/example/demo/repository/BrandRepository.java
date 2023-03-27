package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Area;
import com.example.demo.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

	@Query("select b from Brand b where (:brandName is null or b.brandName Like %:brandName%)")
	Page<Brand> findByFilterParam(@Param("brandName") Optional<String> brandName, Pageable pageable);
	
	@Query(value = "select count(*) from brand where brandName=?1",nativeQuery = true)
	Long findIfExists(@Param("brandName")String brandName);
}
