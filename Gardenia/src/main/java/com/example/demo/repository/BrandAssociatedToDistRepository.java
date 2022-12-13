package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.BrandAssociatedToDist;

public interface BrandAssociatedToDistRepository extends JpaRepository<BrandAssociatedToDist, Long>{

	@Query(value="select * from brand_associated_to_distributor where distributorId=?1",nativeQuery = true)
	List<BrandAssociatedToDist> findByDistributor(@Param("distributorId") Long distributorId);
	
	@Query(value = "select count(brandId) from brand_associated_to_distributor where brandId=?1 and distributorId=?2",nativeQuery = true)
	Long checkBrandExistsInDist(@Param("brandId") Long brandId,@Param("distributorId")Long distributorId);
	
	@Query(value = "delete from brand_associated_to_distributor where distributorId=?1",nativeQuery = true)
	void deleteByDistributor(@Param("distributorId") Long distributorId);
	
	@Transactional
	@Modifying
	@Query(value = "delete from brand_associated_to_distributor where distributorId=?1 and id=?2",nativeQuery = true)
	void deleteByDistributorAndId(@Param("distributorId") Long distributorId,@Param("bId") Long bId);
}
