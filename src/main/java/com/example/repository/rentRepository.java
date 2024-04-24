package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Rent;
import com.example.model.User;

@Repository
public interface rentRepository extends JpaRepository<Rent, Integer> {

    List<Rent> findByUserFullName(String fullName);

	boolean existsByUserEmail(String email);


	List<Rent> findDistinctByUser(User user);

	List<Rent> findByUser(User loggedInUser);

}
