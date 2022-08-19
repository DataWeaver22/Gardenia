package com.example.demo.repository;
 
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Login;
 
public interface LoginRepository extends JpaRepository<Login, Long> {

	Login findByUsernameAndPassword(String username, String password);
	Optional<Login> findByUsername(String username);
}