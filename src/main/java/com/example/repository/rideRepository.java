package com.example.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Ride;
import java.util.List;

@Repository
public interface rideRepository extends JpaRepository<Ride, Integer> {

	Ride findByRideId(int rideId);

	boolean existsByRideId(int rideId);

	List<Ride> findByAboutContainingIgnoreCase(String about);

    Page<Ride> findByAboutContainingIgnoreCase(String keyword, Pageable pageable);

}
