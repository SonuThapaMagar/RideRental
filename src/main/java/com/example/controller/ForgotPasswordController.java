//package com.example.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//
//import com.example.model.User;
//import com.example.repository.userRepository;
//
//import jakarta.servlet.http.HttpSession;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class ForgotPasswordController {
//	@Autowired
//	private userRepository uRepo;
//
//	@GetMapping("/forgotPassword")
//	public String loadForgotPassword() {
//		return "newForgot";
//	}
//
//	@PostMapping("/postforgotPassword")
//	public String forgotPassword(@RequestParam String email, HttpSession session, Model model) {
//		List<User> users = uRepo.findByEmail(email);
//
//		String resetEmail = (String) session.getAttribute("resetEmail");
//		if (resetEmail == null) {
//			// If resetEmail is not found in session, redirect back to forgot password page
//			return "newForgot";
//		}
//
//		if (!users.isEmpty()) {
//			if (users.size() == 1) {
//				// If only one user is found, proceed with the password reset
//				session.setAttribute("resetEmail", email); // Store the email in session for security
//				return "newPassword"; // Redirect to the new password page
//			} else {
//				// If multiple users are found, handle the scenario appropriately
//				model.addAttribute("errorMessage",
//						"Multiple accounts found for the provided email address. Please contact support for assistance.");
//				return "newForgot"; // Display an error page or prompt the user to contact support
//			}
//		} else {
//			// Handle the case where no user is found for the provided email address
//			model.addAttribute("errorMessage", "No account found for the provided email address.");
//			return "newForgot"; // Display an error page or prompt the user to verify their email address
//		}
//	}
//
//	// Load new password page
//	@GetMapping("/resetPassword")
//	public String loadNewPassword(HttpSession session, Model model) {
//		Integer resetUserId = (Integer) session.getAttribute("resetUserId");
//		if (resetUserId == null) {
//			// If resetUserId is not found in session, redirect back to forgot password page
//			return "newForgot";
//		}
//		// Pass resetUserId to the newPassword page
//		model.addAttribute("resetUserId", resetUserId);
//		return "newPassword";
//	}
//
//	@PostMapping("/resetPassword")
//	public String resetPassword(@RequestParam Integer userId, @RequestParam String newPassword, HttpSession session,
//			Model model) {
//
//		Integer resetUserId = (Integer) session.getAttribute("resetUserId");
//		if (resetUserId == null || !resetUserId.equals(userId)) {
//			return "newForgot";
//		}
//		User user = uRepo.findById(userId).orElse(null);
//		if (user != null) {
//			user.setPassword(newPassword);
//			uRepo.save(user);
//			session.removeAttribute("resetUserId");
//			return "login";
//		} else {
//			model.addAttribute("errorMessage", "User not found.");
//			return "newPassword";
//		}
//	}
//}