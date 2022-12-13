package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

	@Query("select c from Category c where c.brand.id=?1")
	List<Category> filterByBrands(@Param("brandId")Optional<Long> brandId);
	
	@Query(value = "select id from brand where brandName=?1",nativeQuery = true)
	Long findByBrand(@Param("bName")String bName);
}
