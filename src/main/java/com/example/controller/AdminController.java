	package com.example.controller;
	
	import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.Admin;
import com.example.model.Rent;
import com.example.model.Ride;
import com.example.model.User;
import com.example.repository.rentRepository;
import com.example.repository.rideRepository;
import com.example.repository.userRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
	
	@Controller
	public class AdminController {
	
		@Autowired
		private rideRepository rideRepo;
	
		@Autowired
		private userRepository uRepo;
	
		@Autowired
		private rentRepository rentRepo;
	
		// static credential for admin
		private static final String adminEmail = "admin2024@gmail.com";
		// Password hashing
		private static final String adminPassword = "admin@2024";
	
		@GetMapping("/admin")
		public String admin(@RequestParam(name = "error", required = false) String error, Model model) {
			if (error != null && !error.isEmpty()) {
				model.addAttribute("errorMessage", error);
			}
			return "adminlogin";
		}
	
		// Admin login via static credential
		@PostMapping("/adminLogin")
		public String adminLogin(@Valid @ModelAttribute Admin a, BindingResult bindingResult, Model model,
				HttpSession session) {
//	
//			if (bindingResult.hasErrors()) {
//				model.addAttribute("emailError", bindingResult.getFieldError("email").getDefaultMessage());
//				model.addAttribute("passwordError", bindingResult.getFieldError("password").getDefaultMessage());
//				return "adminlogin";
//			}
	
			// Checking the credential
			if (a.getEmail().equals(adminEmail) && a.getPassword().equals(adminPassword)) {
				session.setAttribute("activeUser", a.getEmail());
				session.setMaxInactiveInterval(30);
	
				model.addAttribute("success", "Login successful!"); // Optional
				return "admindash";
			}
			model.addAttribute("errorMessage", "Invalid username or password !!"); // Set error message
			return "adminlogin";
		}
	
		@GetMapping("/logout")
		public String logout(HttpSession session) {
			session.invalidate();
			return "adminlogin";
		}
	
		@GetMapping("/adminDash")
		public String adminDash(HttpSession session, Model model,Rent rent,User user, Ride ride) {
			if (session.getAttribute("activeUser") == null) {
				String errorMessage = "Please login first!";
				model.addAttribute("errorMessage", errorMessage);
				return "adminlogin";
			} 
				List<Rent> rentList = rentRepo.findAll();
				model.addAttribute("rentList", rentList);
				return "admindash";
			
		}
	
		@GetMapping("/add")
		public String add(HttpSession session, Model model) {
	
			if (session.getAttribute("activeUser") == null) {
				String errorMessage = "Please login first!";
				model.addAttribute("errorMessage", errorMessage);
				return "adminlogin";
			} else {
				return "add";
			}
	
		}
		@GetMapping("/searchUser")
		public String searchUser(@RequestParam(required = false) String fullName,Model model, User user) {
			
			List<User> uList;
			if (fullName != null && !fullName.isEmpty()) {
				uList = uRepo.findByFullName(fullName); // Search by name and model
																		// (case-insensitively)
			} else {
				uList = uRepo.findAll(); // Retrieve all rides if no keyword provided
			}
			model.addAttribute("uList", uList);
	
			return "manageUser";
		}
		@GetMapping("/searchRent")
		public String searchRent(@RequestParam(required = false) String fullName, Rent rent,Model model) {
			List<Rent> rentList;
			if (fullName != null && !fullName.isEmpty()) {
				rentList = rentRepo.findByFullName(fullName); // Search by name and model
																				// (case-insensitively)
			} else {
				rentList = rentRepo.findAll(); // Retrieve all rides if no keyword provided
			}
		
			model.addAttribute("rentList", rentList);
			return "rental";
	
		}
		
		@GetMapping("/searchRide")
		public String searchRide(@RequestParam(required = false) String keyword,Model model, Ride ride) {
			
			List<Ride> rideList;
	
			if (keyword != null && !keyword.isEmpty()) {
				rideList = rideRepo.findByAboutContainingIgnoreCase(keyword); // Search by name and model
																				// (case-insensitively)
			} else {
				rideList = rideRepo.findAll(); // Retrieve all rides if no keyword provided
			}
			model.addAttribute("rideList", rideList);
	
	
			return "ridebooking";
		}
	
	
	
		// add ride
		@PostMapping("/add")
		public String addRide(@ModelAttribute Ride ride, @RequestParam MultipartFile rideImage, Model model)
				throws IOException {
	
			try {
				// Validate if the uploaded file is empty
				if (rideImage.isEmpty()) {
					model.addAttribute("error", "Please select a file to upload");
					return "add";
				}
	
				Ride newRide = new Ride();
				newRide.setType(ride.getType());
				newRide.setPlateNo(ride.getPlateNo());
				newRide.setStatus(ride.getStatus());
				newRide.setAbout(ride.getAbout());
				newRide.setPrice(ride.getPrice());

	
				newRide.setRideImg(rideImage.getOriginalFilename());
				Ride savedRide = rideRepo.save(newRide);
	
				if (savedRide != null) {
					try {
	
						File saveDir = new ClassPathResource("static/assets").getFile();
	
						Path imagePath = Paths
								.get(saveDir.getAbsolutePath() + File.separator + rideImage.getOriginalFilename());
	
						try (InputStream inputStream = rideImage.getInputStream()) {
	
							Files.copy(rideImage.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
	
						}
						System.out.println(imagePath);
	
						List<User> userList = uRepo.findAll();
						model.addAttribute("uList", userList);
	
						List<Ride> rideList = rideRepo.findAll();
						model.addAttribute("rideList", rideList);
	
						List<Rent> rentList = rentRepo.findAll();
						model.addAttribute("rList", rentList);
	
						return "admindash";
	
					} catch (Exception e) {
	
						e.printStackTrace();
						model.addAttribute("error", "Failed to upload image");
						return "add";
					}
	
				}
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("error", "Failed to save ride details");
	
			}
			return "add";
		}
	
		@GetMapping("/admindash")
		public String admindash(HttpSession session, Model model) {
	
			return "admindash";
	
		}
	
		@GetMapping("/ridebooking")
		public String rideDetails(Model model, HttpSession session) {
	
			List<Ride> rideList = rideRepo.findAll();
			model.addAttribute("rideList", rideList);
			return "ridebooking";
	
		}
	
		@GetMapping("/manageUser")
		public String manageUser(Model model, HttpSession session) {
	
			List<User> uList = uRepo.findAll();
			model.addAttribute("uList", uList);
			return "manageUser";
	
		}
	
		@GetMapping("/rental")
		public String rental(Model model, User user, Ride ride) {
	
			List<Rent> rentList = rentRepo.findAll();
			model.addAttribute("rentList", rentList);
			return "rental";
	
		}
		@GetMapping("/adminProfile")
		public String adminProfile(Model model) {
			
			
			return "adminProfile";
		}
	
		@GetMapping("/editRide/{rideId}")
		public String editRide(@PathVariable int rideId, Model model, HttpSession session) {
	
			Ride ride = rideRepo.findById(rideId).orElse(null);
			if (ride == null) {
				return "editRide"; // Redirect to an error page
			}
			model.addAttribute("rideObject", ride);
			return "editRide";
		}
	
		@PostMapping("/editRide")
		public String updateRide(@ModelAttribute Ride ride, Model model, HttpSession session) throws IOException {
	
			try {
	
				// Handle file upload for profile image
				MultipartFile newRideImg = ride.getNewRideImg();
				if (newRideImg != null && !newRideImg.isEmpty()) {
					// Save the profile image file to a location on the server
	
					File saveDir = new ClassPathResource("static/assets").getFile();
	
					Path imagePath = Paths
							.get(saveDir.getAbsolutePath() + File.separator + newRideImg.getOriginalFilename());
	
					try (InputStream inputStream = newRideImg.getInputStream()) {
	
						Files.copy(newRideImg.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
	
					}
					// Update the user's profile image path in the database
					String imagePathString = "/" + newRideImg.getOriginalFilename();
					ride.setRideImg(imagePathString);
	
					rideRepo.save(ride);
					model.addAttribute("rideList", rideRepo.findAll());
					return "ridebooking";
	
				}
			}
	
			catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("error", "Failed to save ride details");
				return "editRide";
			}
			return "editRide";
		}
	
		// Edit User
		@GetMapping("/editUser/{userId}")
		public String editUser(@PathVariable int userId, Model model, HttpSession session) {
			if(session.getAttribute("activeUser")==null)
			{
				session.setAttribute("error", "Please login first!");
				return "adminlogin";
			}
			User user=uRepo.getById(userId);
//			User user = uRepo.findById(userId).orElse(null);
//			if (user == null) {
//				return "editRide"; // Redirect to an error page
//			}
	
			model.addAttribute("userObject", user);
			return "editUser";
		}
	
		@PostMapping("/editUser")
		public String editUser(@ModelAttribute User user, Model model) throws IOException {
	
			try {
	
				// Handle file upload for profile image
				MultipartFile newLicenseFile = user.getNewLicenseFile();
				if (newLicenseFile != null && !newLicenseFile.isEmpty()) {
					// Save the profile image file to a location on the server
	
					File saveDir = new ClassPathResource("static/assets").getFile();
	
					Path imagePath = Paths
							.get(saveDir.getAbsolutePath() + File.separator + newLicenseFile.getOriginalFilename());
	
					try (InputStream inputStream = newLicenseFile.getInputStream()) {
	
						Files.copy(newLicenseFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
	
					}
	
					// Update the user's profile image path in the database
					String imagePathString = "/" + newLicenseFile.getOriginalFilename();
					user.setLicense(imagePathString);
	
					uRepo.save(user);
					model.addAttribute("uList", uRepo.findAll());
					return "manageUser";
	
				}
			}
	
			catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("error", "Failed to save ride details");
				return "manageUser";
			}
			return "editUser";
		}
	
		@GetMapping("/deleteRide/{rideId}")
		public String deleteRide(@PathVariable int rideId, Model model, HttpSession session) {
	
			rideRepo.deleteById(rideId);
			model.addAttribute("rideList", rideRepo.findAll());
			return "ridebooking";
		}
	
		@GetMapping("/deleteUser/{userId}")
		public String deleteUser(@PathVariable int userId, Model model, HttpSession session) {
	
			uRepo.deleteById(userId);
	
			model.addAttribute("uList", uRepo.findAll());
			return "manageUser";
	
		}
	
	}
