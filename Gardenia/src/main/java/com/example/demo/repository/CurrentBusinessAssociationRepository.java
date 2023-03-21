package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.CurrentBusinessAssociation;

public interface CurrentBusinessAssociationRepository extends JpaRepository<CurrentBusinessAssociation, Long>{

	@Query(value="select * from currentBusinessAssociation where distributorId=?1",nativeQuery = true)
	List<CurrentBusinessAssociation> findByDistributor(@Param("distributorId") Long distributorId);
	
	@Query(value = "delete from currentBusinessAssociation where distributorId=?1",nativeQuery = true)
	void deleteByDistributor(@Param("distributorId") Long distributorId);
	
	@Transactional
	@Modifying
	@Query(value = "delete from currentBusinessAssociation where distributorId=?1 and id=?2",nativeQuery = true)
	void deleteByDistributorAndId(@Param("distributorId") Long distributorId,@Param("bId") Long bId);
}
