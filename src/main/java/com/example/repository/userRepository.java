package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.User;
import java.util.List;

@Repository
public interface userRepository extends JpaRepository<User, Integer>{
	
	
	User findByUserId(int userId);
	
	boolean existsByEmailAndPassword(String email, String password);

}
