package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
	@Query("select c.countryName from Country c where c.id = ?1")
	String findByCountryName(@Param("cId") Long cName);

	@Query("select c.id from Country c where c.countryName = ?1")
	Long findByCountry(@Param("cName") String counName);

	@Query("select s from State s where s.country.id=?1")
	List<State> filterByCountry(@Param("countryId") Optional<Long> countryId);

	@Query("select s from State s where (:stateCode is null or s.stateCode Like %:stateCode%) and (:stateName is null or s.stateName LIKE %:stateName%) and (:countryName is null or s.country.countryName LIKE %:countryName%)")
	Page<State> findByFilterParam(@Param("stateCode") Optional<String> stateCode,
			@Param("stateName") Optional<String> stateName, @Param("countryName") Optional<String> countryName,
			Pageable pageable);

}