//package com.example.controller;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import jakarta.servlet.http.HttpSession;
//
//
//@Controller
//public class ReviewController {
//
//	
//	@GetMapping("/review")
//	public String reviewForm() {
//		
//		return "dashboard";
//	}
//	
//	@PostMapping("/submitReview")
//	public String submitReview(@RequestBody String review,HttpSession session,Model model) {
//
//		// Retrieve the logged-in user's ID from the session
//		Integer loggedInUserId = (Integer) session.getAttribute("loggedInUserId");
//		// Check if the user is logged in
//		if (session.getAttribute("activeUser") == null) {
//			String errorMessage = "Please login first!";
//			model.addAttribute("errorMessage", errorMessage);
//			return "login"; // Redirect to the login page
//		}
//		
//		return "dashboard";
//	}
//	 @GetMapping("/testimonials")
//	    public String testimonials(Model model) {
//	        model.addAttribute("reviews", reviews); // Pass the list of reviews to the Thymeleaf model
//	        return "testimonials"; // Return the testimonials HTML template
//	    }
//	
//}
