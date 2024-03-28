package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Ride;
@Repository
public interface rideRepository extends JpaRepository<Ride, Integer>{

}
