package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.example.demo.entity.UpdateInvoiceStatus;

public interface UpdateInvoiceStatusRepository extends JpaRepository<UpdateInvoiceStatus, Long>{

	@Query("select a from UpdateInvoiceStatus  a")
	Page<UpdateInvoiceStatus> findByFilterParam(Pageable paging);

	
}
