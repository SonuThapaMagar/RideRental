package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Admin;

@Repository
public interface adminRepository extends JpaRepository<Admin, Integer>{

}