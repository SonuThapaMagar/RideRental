package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Rent;
@Repository
public interface rentRepository extends JpaRepository<Rent, Integer> {

	List<Rent> findByFullName(String keyword);
	
	
boolean existsByUserEmail(String email);

}
