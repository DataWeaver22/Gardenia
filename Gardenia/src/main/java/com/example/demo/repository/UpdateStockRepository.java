package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.UpdateInvoiceStatus;
import com.example.demo.entity.UpdateStock;

public interface UpdateStockRepository extends JpaRepository<UpdateStock, Long>{



	@Query("select a from updatestock  a")
	Page<UpdateStock> findByFilterParam(Pageable paging);

}
