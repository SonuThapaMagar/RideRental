package com.example.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.Rent;
import com.example.model.Ride;
import com.example.model.User;
import com.example.repository.rentRepository;
import com.example.repository.rideRepository;
import com.example.repository.userRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@Controller

public class UserController {
	@Autowired
	private userRepository uRepo;

	@Autowired
	private rideRepository rideRepo;

	@Autowired
	private rentRepository rentRepo;

	@GetMapping("/")
	public String landingPage() {
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/userLogin")
	public String userLogin(Model model) {
		List<Ride> rideList = rideRepo.findAll();
		model.addAttribute("rideList", rideList);
		return "dashboard";
	}

	@PostMapping("/userLogin")
	public String userLogin(@ModelAttribute User user, Model model, HttpSession session) {
		// Hash the password entered by the user
		String hashedPassword = DigestUtils.shaHex(user.getPassword());

		if (uRepo.existsByEmailAndPassword(user.getEmail(), hashedPassword)) {

			session.setAttribute("activeUser", user.getEmail());
			session.setAttribute("activeUser", user.getFullName());
			session.setMaxInactiveInterval(30);

			List<User> uList = uRepo.findAll();
			model.addAttribute("uList", uList);

			List<Ride> rideList = rideRepo.findAll();
			model.addAttribute("rideList", rideList);

			model.addAttribute("loggedInUserEmail", user.getEmail());
			model.addAttribute("loggedInUserFullName", user.getFullName());
			return "dashboard";

		}
		return "login";

	}

	// Add User
	@PostMapping("/register")
	public String registerUser(@ModelAttribute User user, @RequestParam MultipartFile licensePhoto, Model model)
			throws IOException {

		try {
			// Hash the password
			String hashedPassword = DigestUtils.shaHex(user.getPassword());

			User newUser = new User();
			newUser.setFullName(user.getFullName());
			newUser.setEmail(user.getEmail());
			newUser.setDob(user.getDob());
			newUser.setPhone(user.getPhone());
			newUser.setPassword(hashedPassword);

			newUser.setLicense(licensePhoto.getOriginalFilename());
			// Save the user object
			User savedEvent = uRepo.save(newUser);

			if (savedEvent != null) {
				try {

					File saveDir = new ClassPathResource("static/assets").getFile();

					Path imagePath = Paths
							.get(saveDir.getAbsolutePath() + File.separator + licensePhoto.getOriginalFilename());

					Files.copy(licensePhoto.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

					System.out.println(imagePath);

					return "login";

				} catch (Exception e) {

					e.printStackTrace();
					model.addAttribute("error", "Failed to upload image");
					return "login";
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("error", "Failed to save register details");
		}

		return "login";
	}

	@GetMapping("/index")
	public String index(Model model) {
		List<Ride> rideList = rideRepo.findAll();
		model.addAttribute("rideList", rideList);
		return "dashboard";
	}

	@GetMapping("/product")
	public String product() {
		return "product";
	}
	
	@GetMapping("/editProfile")
	public String editProfile(Model model,HttpSession session) {
		
		  String activeUserEmail = (String) session.getAttribute("activeUser");
		    if (activeUserEmail != null) {
		        // Retrieve the user from the database based on the email
		        User userObject = uRepo.findByEmail(activeUserEmail);
		        if (userObject != null) {
		            // Add the userObject to the model
		            model.addAttribute("userObject", userObject);
		            return "profile"; // Assuming your profile page is named "profile.html"
		        }
		    }
		return "profile";
	}

	@GetMapping("/search")
	public String searchRides(@RequestParam(required = false) String keyword, Model model) {
		List<Ride> rideList;
		if (keyword != null && !keyword.isEmpty()) {
			rideList = rideRepo.findByAboutContainingIgnoreCase(keyword); // Search by name and model
																			// (case-insensitively)
		} else {
			rideList = rideRepo.findAll(); // Retrieve all rides if no keyword provided
		}

		model.addAttribute("rideList", rideList);
		return "dashboard"; // View name to display search results
	}

	@GetMapping("/renttable")
	public String renttable() {
		return "renttable";
	}

	@GetMapping("/Services")
	public String Services() {
		return "Services";
	}

	@GetMapping("/test")
	public String test() {
		return "test";
	}

	@GetMapping("/view")
	public String view() {
		return "view";
	}

	@GetMapping("/rentRide")
	public String rentRide(Model model, HttpSession session) {

		return "rent";
	}

	@GetMapping("/orderDetails")
	public String order(Model model, HttpSession session) {
		if (session.getAttribute("activeUser") == null) {
			String errorMessage = "Please login first!";
			model.addAttribute("errorMessage", errorMessage);
			return "login";
		}
		List<Rent> rentList = rentRepo.findAll();
		model.addAttribute("rentList", rentList);
		return "orderDetails";
	}

	@PostMapping("/orderDetails")
	public String orderDetails(@ModelAttribute Rent rent, Model model) {

		Rent savedRent = rentRepo.save(rent);
		List<Rent> rentList = rentRepo.findAll();
		model.addAttribute("rentList", rentList);

		return "orderDetails";
	}
	
	

}