package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Brand;
import com.example.demo.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

	@Query("select c from Category c where c.brand.id=?1")
	List<Category> filterByBrands(@Param("brandId")Optional<Long> brandId);
	
	@Query(value = "select id from brand where brandName=?1",nativeQuery = true)
	Long findByBrand(@Param("bName")String bName);
	
	@Query("select c from Category c where (:brandName is null or c.brand.brandName Like %:brandName%) and (:categoryName is null or c.categoryName Like %:categoryName%)")
	Page<Category> findByFilterParam(@Param("brandName") Optional<String> brandName,@Param("categoryName") Optional<String> categoryName, Pageable pageable);
	
	@Query(value = "select count(*) from category where brandId=?1 and categoryName=?2",nativeQuery = true)
	Long findIfExists(@Param("brandId")Long brandId,@Param("categoryName")String categoryName);
}
