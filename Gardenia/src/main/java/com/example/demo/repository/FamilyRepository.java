package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Category;
import com.example.demo.entity.Family;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Long>{

	@Query("select f from Family f where f.category.id=?1")
	List<Family> filterByCategories(@Param("categoryId")Optional<Long> categoryId);
	
	@Query(value = "select id from category where categoryName=?1 and brandId=?2",nativeQuery = true)
	Long findByCategory(@Param("cName") String cName, @Param("brandId")Long brandId);
	
	@Query(value = "select id from brand where brandName=?1",nativeQuery = true)
	Long findByBrand(@Param("brandName")String brandName);
	
	@Query("select f from Family f where (:familyName is null or f.familyName Like %:familyName%) and (:categoryName is null or f.category.categoryName Like %:categoryName%"
			+ " or f.category.brand.brandName LIKE %:categoryName%)")
	Page<Family> findByFilterParam(@Param("familyName") Optional<String> familyName,@Param("categoryName") Optional<String> categoryName, Pageable pageable);
}
