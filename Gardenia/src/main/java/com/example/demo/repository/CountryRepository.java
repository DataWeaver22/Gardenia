package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
	
	@Query("select c.countryName from Country c where c.id = ?1")
	String findCountryById(@Param("cId") Long cId);
}