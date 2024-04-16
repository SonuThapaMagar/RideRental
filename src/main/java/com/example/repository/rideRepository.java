package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Ride;
import java.util.List;

@Repository
public interface rideRepository extends JpaRepository<Ride, Integer> {

	Ride findByRideId(int rideId);

	boolean existsByRideId(int rideId);

	List<Ride> findByAboutContainingIgnoreCase(String about);

//    int findPriceByTypeAndPackage(String type, String rentPackage);

}
