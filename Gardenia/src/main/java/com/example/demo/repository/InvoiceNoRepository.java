package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.InvoiceNo;


public interface InvoiceNoRepository extends JpaRepository<InvoiceNo, Long>{


	@Query(value = "select * from invoiceNo where invoiceId=?1",nativeQuery = true)
	List<InvoiceNo> findByid(@Param("invoiceId") Long id);
}
