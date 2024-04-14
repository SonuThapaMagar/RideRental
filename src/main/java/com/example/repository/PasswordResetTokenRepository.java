package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.PasswordResetToken;


@Repository
public interface PasswordResetTokenRepository  extends JpaRepository<PasswordResetToken, Long>{

	PasswordResetToken findByToken(String token);

}
