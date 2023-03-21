package com.example.demo.repository;
 
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Login;
 
public interface LoginRepository extends JpaRepository<Login, Long> {

	Login findByUsernameAndPassword(String username, String password);
	Optional<Login> findByUsername(String username);
	
	@Query(value="select * from login where username = ?1",nativeQuery = true)
	Login findByUsernames(@Param("username") String username);
}