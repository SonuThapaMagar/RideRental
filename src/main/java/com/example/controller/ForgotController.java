//package com.example.controller;
//
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import com.example.model.User;
//import com.example.service.EmailService;
//import com.example.service.UserService;
//
//@Controller
//public class ForgotController {
//
//	@Autowired
//	private EmailService emailService;
//
//	@Autowired
//	private UserService userService;
//
//	
//	@GetMapping("/forgotPassword")
//	public String showForgotPasswordForm() {
//
//		return "newForgot";
//	}
//
//	@PostMapping("/forgotPassword")
//	public String ForgotPasswordProcess(@RequestParam("email") String email,Model model) {
//		User user = userService.findByEmail(email);
//		if (user != null) {
//			String token = UUID.randomUUID().toString();
//			userService.createPasswordResetTokenForUser(user, token);
//			emailService.sendPasswordResetEmail(user, token);
//		}
//		return "";
//	}
//
//}
