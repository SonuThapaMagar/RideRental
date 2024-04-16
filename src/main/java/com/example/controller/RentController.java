//package com.example.controller;
//
//import java.io.File;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.example.model.User;
//
//import jakarta.servlet.http.HttpSession;
//
//@Controller
//public class RentController {
//
//	// Edit User
//		@GetMapping("/editProfile/{userId}")
//		public String editProfile(@PathVariable int userId, HttpSession session, Model model) {
//
//			if (session.getAttribute("activeUser") == null) {
//				session.setAttribute("error", "Please login first!");
//				return "login"; 		}
//
//			User user = uRepo.getById(userId);
//
//			model.addAttribute("userObject", user);
//			return "profile";
//		}
//
//		@PostMapping("/editProfile")
//		public String editProfile(@ModelAttribute User user, Model model, HttpSession session) {
//
//			try {
//				// Retrieve the logged-in user ID
//				Integer loggedInUserId = (Integer) session.getAttribute("loggedInUserId");
//				if (loggedInUserId == null) {
//					return "login";
//				}
//
//				// Retrieve the user from the database based on the logged-in user ID
//				User existingUser = uRepo.findById(loggedInUserId).orElse(null);
//				if (existingUser == null) {
//					// Handle case where user is not found
//					return "redirect:/dashboard"; // For example, redirect to the dashboard
//				}
//
//				// Update the user's profile data
//				existingUser.setFullName(user.getFullName());
//				existingUser.setEmail(user.getEmail());
//				existingUser.setDob(user.getDob());
//				existingUser.setPhone(user.getPhone());
//
//				// Handle file upload for profile image
//				MultipartFile newUserImg = user.getNewLicenseFile();
//				if (newUserImg != null && !newUserImg.isEmpty()) {
//					// Save the profile image file to a location on the server
//
//					File saveDir = new ClassPathResource("static/assets").getFile();
//
//					Path imagePath = Paths
//							.get(saveDir.getAbsolutePath() + File.separator + newUserImg.getOriginalFilename());
//
//					try (InputStream inputStream = newUserImg.getInputStream()) {
//
//						Files.copy(newUserImg.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
//
//					}
//					// Update the user's profile image path in the database
//					String imagePathString = "/" + newUserImg.getOriginalFilename();
//					existingUser.setLicense(imagePathString);
//
//					uRepo.save(existingUser);
//					model.addAttribute("uList", uRepo.findAll());
//					return "login";
//
//				}
//			}
//
//			catch (Exception e) {
//				e.printStackTrace();
//				model.addAttribute("error", "Failed to save ride details");
//				return "profile";
//			}
//			return "profile";
//		}
//		
//}
