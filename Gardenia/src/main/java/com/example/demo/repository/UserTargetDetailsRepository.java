package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.UserTargetDetails;

@Repository
public interface UserTargetDetailsRepository extends JpaRepository<UserTargetDetails, Long>{

	@Query(value = "select * from userTargetDetails where userId=?1",nativeQuery = true)
	List<UserTargetDetails> findByUser(@Param("userId") Long userId);
}
