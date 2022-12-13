package com.example.demo.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	@Transactional
	@Modifying
	@Query("update Product d set d.approval_status=:approved where d.id=:pID")
	void updateByStatus(@Param("approved") String approved,@Param("pID") Long pID);
	
	@Query("select p.brand from Product p where p.id=?1")
	String findBrandByID(@Param("pID") Long pID);
	
	@Query(value = "select id from brand where brandName=?1",nativeQuery = true)
	Long findByBrand(@Param("bName") String bName);

	@Query(value = "select id from category where categoryName=?1 and brandId=?2",nativeQuery = true)
	Long findByCategory(@Param("cName") String cName,@Param("brandId") Long brandId);
	
	@Query(value = "select id from family where familyName=?1 and categoryId=?2",nativeQuery = true)
	Long findByFamily(@Param("fName") String fName,@Param("categoryId")Long categoryId);
	
	@Query("select p from Product p where approval_status=?1")
	Page<Product> findByProductStatus(@Param("productStatus")Optional<String> productStatus,Pageable pageable);
}
