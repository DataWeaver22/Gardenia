package com.example.demo.repository;
 

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Country;
import com.example.demo.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
	@Query("select c.country_name from Country c where c.id = ?1")
	String findByCountryName(@Param("cId") Long cName);
	
}