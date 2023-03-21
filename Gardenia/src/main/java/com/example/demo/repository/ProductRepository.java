package com.example.demo.repository;

import java.math.BigDecimal;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Area;
import com.example.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	@Transactional
	@Modifying
	@Query("update Product d set d.approval_status=:approved,d.rejectReason=:rejectReason where d.id=:pID")
	void updateByStatus(@Param("approved") String approved, @Param("rejectReason") String rejectReason,
			@Param("pID") Long pID);

	@Transactional
	@Modifying
	@Query("update Product d set d.approval_status=:approved where d.id=:pID")
	void updateByApprovedStatus(@Param("approved") String approved, @Param("pID") Long pID);

	@Query("select p.brand from Product p where p.id=?1")
	String findBrandByID(@Param("pID") Long pID);

	@Query(value = "select id from brand where brandName=?1", nativeQuery = true)
	Long findByBrand(@Param("bName") String bName);

	@Query(value = "select id from category where categoryName=?1 and brandId=?2", nativeQuery = true)
	Long findByCategory(@Param("cName") String cName, @Param("brandId") Long brandId);

	@Query(value = "select id from family where familyName=?1 and categoryId=?2", nativeQuery = true)
	Long findByFamily(@Param("fName") String fName, @Param("categoryId") Long categoryId);

	@Query("select p from Product p where approval_status=?1")
	Page<Product> findByProductStatus(@Param("productStatus") Optional<String> productStatus, Pageable pageable);

	@Query("select p from Product p where (:productCode is null or p.code Like %:productCode%) and (:productName is null or p.pname LIKE %:productName%) "
			+ "and (:brandName is null or p.brand.brandName LIKE %:brandName%) and (:categoryName is null or p.category.categoryName Like %:categoryName%) "
			+ "and (:familyName is null or p.family.familyName Like %:familyName%) and (:variant is null or p.variant Like %:variant%) "
			+ "and (:salesDiaryCode is null or p.salesDiaryCode LIKE %:salesDiaryCode%) and (:mrp is null or p.mrp=:mrp) "
			+ "and (:productStatus is null or p.approval_status=:productStatus)")
	Page<Product> findByFilterParam(@Param("productCode") Optional<String> productCode,
			@Param("productName") Optional<String> productName, @Param("brandName") Optional<String> brandName,
			@Param("categoryName") Optional<String> categoryName, @Param("familyName") Optional<String> familyName,
			@Param("variant") Optional<String> variant, @Param("salesDiaryCode") Optional<String> salesDiaryCode,
			@Param("mrp") Optional<BigDecimal> mrp, @Param("productStatus") Optional<String> productStatus,
			Pageable pageable);
}
