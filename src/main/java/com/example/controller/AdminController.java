package com.example.controller;

import java.awt.print.Pageable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import com.example.model.Review;
import com.example.model.Ride;
import com.example.model.User;
import com.example.repository.rentRepository;
import com.example.repository.reviewRepository;
import com.example.repository.rideRepository;
import com.example.repository.userRepository;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AdminController {
	// -------------------Repositories---------------------

	@Autowired
	private rideRepository rideRepo;

	@Autowired
	private userRepository uRepo;

	@Autowired
	private rentRepository rentRepo;

	@Autowired
	private reviewRepository reviewRepo;

	// -------------------Admin Login--------------------

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
	// -------------------Admin Login login via static
	// credential--------------------

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

			long totalRides = rideRepo.count(); // Assuming you have a method in your repository to count rides
			long totalUsers = uRepo.count();
			long totalRents = rentRepo.count();
			model.addAttribute("totalRents", totalRents);
			model.addAttribute("totalRides", totalRides);
			model.addAttribute("totalUsers", totalUsers);
			List<Ride> rideList = rideRepo.findAll();
			model.addAttribute("rideList", rideList);
			model.addAttribute("success", "Login successful!"); // Optional
			return "admindash";
		}
		model.addAttribute("errorMessage", "Invalid username or password !!"); // Set error message
		return "adminlogin";
	}
	// -------------------Admin Logout--------------------

	@GetMapping("/logout")
	public String logout(HttpSession session, HttpServletResponse response) {

		session.invalidate();
		if (session == null){
			return "adminlogin";

		}
		// Set cache control headers to prevent caching of pages
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setHeader("Expires", "0"); // Proxies
		return "adminlogin";
	}
	// -------------------Admin Dashboard--------------------

	@GetMapping("/adminDash")
	public String adminDash(HttpSession session, Model model, Rent rent, User user, Ride ride) {

		String activeUser = (String) session.getAttribute("activeUser");

		if (activeUser == null || !activeUser.equals(adminEmail)) {

			String errorMessage = "Please login as admin to access this page!";

			model.addAttribute("errorMessage", errorMessage);
			return "adminlogin";
		}
		long totalRides = rideRepo.count(); // Assuming you have a method in your repository to count rides
		long totalUsers = uRepo.count();
		long totalRents = rentRepo.count();
		model.addAttribute("totalRents", totalRents);


		model.addAttribute("totalRides", totalRides);
		model.addAttribute("totalUsers", totalUsers);


		List<Ride> rideList = rideRepo.findAll();
		model.addAttribute("rideList", rideList);

		return "admindash";

	}

	// -------------------Admin Search User--------------------

	@GetMapping("/searchUser")
	public String searchUser(@RequestParam(required = false) String fullName, Model model, User user,
			HttpSession session) {
		String activeUser = (String) session.getAttribute("activeUser");
		if (activeUser == null || !activeUser.equals(adminEmail)) {
			String errorMessage = "Please login as admin to access this page!";
			model.addAttribute("errorMessage", errorMessage);
			return "adminlogin";
		}
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
	// -------------------Admin Search Rent--------------------

	@GetMapping("/searchRent")
	public String searchRent(@RequestParam(required = false) String fullName, Rent rent, Model model) {
		List<Rent> rentList;
		if (fullName != null && !fullName.isEmpty()) {
			rentList = rentRepo.findByUserFullName(fullName); // Search by name and model
																// (case-insensitively)
		} else {
			rentList = rentRepo.findAll(); // Retrieve all rides if no keyword provided
		}

		model.addAttribute("rentList", rentList);
		return "rental";

	}

	@GetMapping("/searchRide")
	public String searchRide(@RequestParam(required = false) String keyword, Model model, Ride ride) {

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
	
	@GetMapping("searchReview")
	public String searchReview(@RequestParam(required = false) String keyword, Model model) {
		
		List<Review>reviewList;
		if (keyword != null && !keyword.isEmpty()) {
			reviewList = reviewRepo.findByUserFullNameContainingIgnoreCase(keyword); // Search by name and model
																			// (case-insensitively)
		} else {
			reviewList = reviewRepo.findAll(); // Retrieve all rides if no keyword provided
		}
		model.addAttribute("reviewList",reviewList);
		return "adminReview";
	}

	// -------------------Admin Add Ride--------------------

	@GetMapping("/add")
	public String add(HttpSession session, Model model) {

		String activeUser = (String) session.getAttribute("activeUser");
		if (activeUser == null || !activeUser.equals(adminEmail)) {
			String errorMessage = "Please login as admin to access this page!";
			model.addAttribute("errorMessage", errorMessage);
			return "adminlogin";
		}
		return "add";

	}

	@PostMapping("/added")
	public String addRide(@Valid @ModelAttribute Ride ride, @RequestParam MultipartFile rideImage, Model model,
			HttpSession session) throws IOException {

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
			newRide.setPricePerHour(ride.getPricePerHour());
			newRide.setPricePer3Hours(ride.getPricePer3Hours());
			newRide.setPricePerHalfDay(ride.getPricePerHalfDay());
			newRide.setPricePerFullDay(ride.getPricePerFullDay());

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

					List<Ride> rideList = rideRepo.findAll();
					model.addAttribute("rideList", rideList);

					return "ridebooking";

				} catch (Exception e) {

					e.printStackTrace();
					model.addAttribute("error", "Failed to upload image");
					return "add";
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Failed to save ride details");
			return "add";

		}
		return "add";
	}

	@GetMapping("/admindash")
	public String admindash(HttpSession session, Model model) {

		String activeUser = (String) session.getAttribute("activeUser");
		if (activeUser == null || !activeUser.equals(adminEmail)) {
			String errorMessage = "Please login as admin to access this page!";
			model.addAttribute("errorMessage", errorMessage);
			return "adminlogin";
		}
		long totalRides = rideRepo.count(); // Assuming you have a method in your repository to count rides
		long totalUsers = uRepo.count();
		long totalRents = rentRepo.count();
		model.addAttribute("totalRents", totalRents);

		model.addAttribute("totalRides", totalRides);
		model.addAttribute("totalUsers", totalUsers);

		List<Ride> rideList = rideRepo.findAll();
		model.addAttribute("rideList", rideList);
		return "admindash";

	}

	@GetMapping("/ridebooking")
	public String rideDetails(Model model, @RequestParam(defaultValue = "0") int page, HttpSession session) {
		String activeUser = (String) session.getAttribute("activeUser");
		if (activeUser == null || !activeUser.equals(adminEmail)) {
			String errorMessage = "Please login as admin to access this page!";
			model.addAttribute("errorMessage", errorMessage);
			return "adminlogin";
		}
		// Pagination
		int pageSize = 5;
		page = Math.max(page, 1); // Ensure page is not less than 1
		Page<Ride> ridePage = rideRepo.findAll(PageRequest.of(page - 1, pageSize));
		int totalPages = ridePage.getTotalPages();
		List<Ride> rideList = ridePage.getContent();

		model.addAttribute("totalPages", totalPages);
		model.addAttribute("rideList", rideList);
		return "ridebooking";

	}

	@GetMapping("/manageUser")
	public String manageUser(Model model, @RequestParam(defaultValue = "0") int page, HttpSession session) {
		String activeUser = (String) session.getAttribute("activeUser");
		if (activeUser == null || !activeUser.equals(adminEmail)) {
			String errorMessage = "Please login as admin to access this page!";
			model.addAttribute("errorMessage", errorMessage);
			return "adminlogin";
		}
		// Pagination
		int pageSize = 5;
		page = Math.max(page, 1); // Ensure page is not less than 1
		Page<User> uPage = uRepo.findAll(PageRequest.of(page - 1, pageSize));
		int totalPages = uPage.getTotalPages();
		List<User> uList = uPage.getContent();

		model.addAttribute("totalPages", totalPages);
		model.addAttribute("uList", uList);
		return "manageUser";

	}

	@GetMapping("/adminReview")
	public String adminReview(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page) {

		String activeUser = (String) session.getAttribute("activeUser");
		if (activeUser == null || !activeUser.equals(adminEmail)) {
			String errorMessage = "Please login as admin to access this page!";
			model.addAttribute("errorMessage", errorMessage);
			return "adminlogin";
		}
		// Pagination
		int pageSize = 5;
		page = Math.max(page, 1); // Ensure page is not less than 1
		Page<Review> reviewPage = reviewRepo.findAll(PageRequest.of(page - 1, pageSize));
		int totalPages = reviewPage.getTotalPages();
		List<Review> reviewList = reviewPage.getContent();

		model.addAttribute("totalPages", totalPages);
		model.addAttribute("reviewList", reviewList);

		return "adminReview";
	}

	@GetMapping("/deleteReview/{reviewId}")
	public String deleteReview(@PathVariable int reviewId,Model model,HttpSession session) {
		
		reviewRepo.deleteById(reviewId);
		model.addAttribute("reviewList",reviewRepo.findAll());
		
		return "adminReview";
	}
	
	
	@GetMapping("/rental")
	public String rental(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page) {
		String activeUser = (String) session.getAttribute("activeUser");
		if (activeUser == null || !activeUser.equals(adminEmail)) {
			String errorMessage = "Please login as admin to access this page!";
			model.addAttribute("errorMessage", errorMessage);
			return "adminlogin";
		}
		// Pagination
		int pageSize = 5;
		page = Math.max(page, 1); // Ensure page is not less than 1
		Page<Rent> rentPage = rentRepo.findAll(PageRequest.of(page - 1, pageSize));
		int totalPages = rentPage.getTotalPages();
		List<Rent> rentList = rentPage.getContent();

		model.addAttribute("totalPages", totalPages);
		model.addAttribute("rentList", rentList);

		return "rental";
	}

	@GetMapping("/editRide/{rideId}")
	public String editRide(@PathVariable int rideId, Model model, HttpSession session) {
		String activeUser = (String) session.getAttribute("activeUser");
		if (activeUser == null || !activeUser.equals(adminEmail)) {
			String errorMessage = "Please login as admin to access this page!";
			model.addAttribute("errorMessage", errorMessage);
			return "adminlogin";
		}
		Ride ride = rideRepo.findById(rideId).orElse(null);
		if (ride == null) {
			return "editRide"; // Redirect to an error page
		}
		model.addAttribute("rideObject", ride);
		model.addAttribute("imageURL", "/assets/" + ride.getRideImg());
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

		String activeUser = (String) session.getAttribute("activeUser");

		if (activeUser == null || !activeUser.equals(adminEmail)) {
			// If the user is not logged in as admin, redirect to the admin login page
			session.setAttribute("error", "Please login as admin to access this page!");
			return "adminlogin";
		}
		Optional<User> optionalUser = uRepo.findById(userId);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			model.addAttribute("userObject", user); // Add userObject to the model
			return "editUser";
		} else {
			// Handle case where user is not found
			return "manageUser"; // Redirect to an error page
		}
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
		String activeUser = (String) session.getAttribute("activeUser");
		if (activeUser == null || !activeUser.equals(adminEmail)) {
			String errorMessage = "Please login as admin to access this page!";
			model.addAttribute("errorMessage", errorMessage);
			return "adminlogin";
		}
		rideRepo.deleteById(rideId);
		model.addAttribute("rideList", rideRepo.findAll());
		return "ridebooking";
	}

	@GetMapping("/deleteUser/{userId}")
	public String deleteUser(@PathVariable int userId, Model model, HttpSession session) {
		String activeUser = (String) session.getAttribute("activeUser");
		if (activeUser == null || !activeUser.equals(adminEmail)) {
			String errorMessage = "Please login as admin to access this page!";
			model.addAttribute("errorMessage", errorMessage);
			return "adminlogin";
		}
		uRepo.deleteById(userId);

		model.addAttribute("uList", uRepo.findAll());
		return "manageUser";

	}

}
