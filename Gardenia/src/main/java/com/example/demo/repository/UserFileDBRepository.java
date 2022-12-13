package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.UserFileDB;

@Repository
public interface UserFileDBRepository extends JpaRepository<UserFileDB, String>{

}
