package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.UserTargetDetails;
import com.example.demo.entity.UserTeam;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, Long>{

	@Query(value = "select * from userTeam where userId=?1",nativeQuery = true)
	List<UserTeam> findByUser(@Param("userId") Long userId);
	
	@Transactional
	@Modifying
	@Query(value = "delete from userTeam where userId=?1 and id=?2",nativeQuery = true)
	void deleteByUserAndId(@Param("userId") Long userId,@Param("bId") Long bId);
	
	@Query(value = "delete from userTeam where userId=?1",nativeQuery = true)
	void deleteByUser(@Param("userId") Long userId);
}
