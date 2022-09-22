package com.example.demo.repository;

import javax.transaction.Transactional;

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
}
