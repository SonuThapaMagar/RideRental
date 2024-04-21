package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.Review;
import com.example.model.User;
import com.example.repository.reviewRepository;
import com.example.repository.userRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReviewController {

	@Autowired
	private reviewRepository reviewRepo;
	
	@Autowired
	private userRepository uRepo;

	// -----------------------------Review------------------------------
	@GetMapping("/review")
	public String reviewForm() {

		return "dashboard";
	}

	@PostMapping("/indexTest")
	public String submitReview(@RequestParam String review, HttpSession session, Model model) {

		Integer loggedInUserId = (Integer) session.getAttribute("loggedInUserId");
		// Check if the user is logged in
		if (session.getAttribute("activeUser") == null) {
			String errorMessage = "Please login first!";
			model.addAttribute("errorMessage", errorMessage);
			return "login"; // Redirect to the login page
		}

		  // Check if the review text exceeds the character limit
	    if (review.length() > 1000) {
	        String errorMessage = "Review text should not exceed 1000 characters.";
	        model.addAttribute("errorMessage", errorMessage);
	        return "dashboard"; 
	        }

		Review newReview = new Review();
		newReview.setReviews(review);
		
		User loggedInUser = uRepo.findById(loggedInUserId).get();
	    newReview.setUser(loggedInUser); 

		reviewRepo.save(newReview);
//		session.removeAttribute("review");

		session.setAttribute("review", review);
		
		List<Review> reviewList = reviewRepo.findAll();
		model.addAttribute("reviewList", reviewList);
		return "test";
	}

	@GetMapping("/testimonials")
	public String testimonials(Model model, HttpSession session) {

		String review = (String) session.getAttribute("review");

		List<Review> reviewList = reviewRepo.findAll();
		model.addAttribute("reviewList", reviewList);

		return "test"; // Return the testimonials HTML template
	}

}
