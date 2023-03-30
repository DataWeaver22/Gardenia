package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.InvoiceNo;


public interface InvoiceNoRepository extends JpaRepository<InvoiceNo, Long>{


	@Query(value = "select * from invoiceNo where updateInvoiceId=?1",nativeQuery = true)
	List<InvoiceNo> findByid(@Param("updateInvoiceId") Long id);

	
	@Transactional
	@Modifying
	@Query(value = "delete from invoiceno where updateInvoiceId=?1 and id=?2" ,nativeQuery = true)
	void deleteByUserAndId(@Param("updateInvoiceId") long updateInvoiceId ,@Param("id") long id);
	
	@Transactional
	@Modifying
	@Query(value = "delete from invoiceno where updateInvoiceId=?1" , nativeQuery = true)
	void deleteByUser(@Param("updateInvoiceId") Long updateInvoiceId);
}
