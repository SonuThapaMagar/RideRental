//package com.example.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import com.example.model.PasswordResetToken;
//import com.example.model.User;
//import com.example.repository.PasswordResetTokenRepository;
//import com.example.repository.userRepository;
//
//
//@Service
//public class UserService {
//
//	@Autowired
//	private userRepository userRepo;
//
//	@Autowired
//	PasswordResetTokenRepository tokenRepository;
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	
//	  public User findByEmail(String email) {
//	        return userRepo.findByEmail(email); 
//	    }
//	
//	public void createPasswordResetTokenForUser(User user, String token) {
//		PasswordResetToken resetToken = new PasswordResetToken(token, user);
//		tokenRepository.save(resetToken);
//	}
//
//	public PasswordResetToken getPasswordResetToken(String token) {
//		return tokenRepository.findByToken(token);
//	}
//
//	public void resetUserPassword(User user, String password) {
//		user.setPassword(passwordEncoder.encode(password));
//		userRepo.save(user);
//	}
//
//}
