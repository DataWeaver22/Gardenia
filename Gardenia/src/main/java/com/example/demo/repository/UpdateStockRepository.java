package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.UpdateStock;

public interface UpdateStockRepository extends JpaRepository<UpdateStock, Long>{

}
