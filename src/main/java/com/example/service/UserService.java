//package com.example.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.example.model.User;
//import com.example.repository.userRepository;
//
//@Service
//public class UserService {
//
//	@Autowired
//	private userRepository uRepo;
//
//	User findByUserId(int userId) {
//		return uRepo.findByUserId(userId).orElse(null);
//
//	}
//	public void update(User user) {
//        userRepository.save(user);
//    }
//
//}
