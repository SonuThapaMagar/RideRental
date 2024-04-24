package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Review;

@Repository
public interface reviewRepository extends JpaRepository<Review, Integer>{

    List<Review> findByUserFullNameContainingIgnoreCase(String keyword);

	
}
