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

	@GetMapping("/indexServices")
	public String services() {
		return "indexService";
	}

	@GetMapping("/indexView")
	public String View() {
		return "indexView";
	}

	@GetMapping("/indexTest")
	public String testimonial() {
		return "indexTest";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/logoutUser")
	public String logoutUser(HttpSession session) {
		session.invalidate();
		return "login";
	}

	@GetMapping("/userLogin")
	public String userLogin(Model model, User user, HttpSession session) {

//		 if (session.getAttribute("activeUser") == null) {
//	            String errorMessage = "Please login first!";
//	            model.addAttribute("errorMessage", errorMessage);
//	            return "login";
//	        }
//		 if (session.getAttribute("activeUser") == null) {
//		        return "login"; // Redirect to login if user is not logged in
//		    }
		List<Ride> rideList = rideRepo.findAll();
		model.addAttribute("rideList", rideList);
		model.addAttribute("loggedInUserEmail", user.getEmail());

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
		model.addAttribute("errorMessage", "Invalid username or password !!"); // Set error message
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
		return "index";
	}

	@GetMapping("/product")
	public String product() {
		return "product";
	}

	@GetMapping("/editProfile/{userId}")
	public String editProfile(@PathVariable (required = false)Integer userId, Model model) {

		User user = uRepo.findById(userId).orElse(null);
		if (userId== null) {
			return "dashboard";
		}
		model.addAttribute("userObject", user);
		return "profile";
	}

	@GetMapping("/search")
	public String searchRides(@RequestParam(required = false) String keyword, User user, Model model,
			HttpSession session) {
//		 if (session.getAttribute("activeUser") == null) {
//	            String errorMessage = "Please login first!";
//	            model.addAttribute("errorMessage", errorMessage);
//	            return "login";
//	        }
		List<Ride> rideList;
		if (keyword != null && !keyword.isEmpty()) {
			rideList = rideRepo.findByAboutContainingIgnoreCase(keyword); // Search by name and model
																			// (case-insensitively)
		} else {
			rideList = rideRepo.findAll(); // Retrieve all rides if no keyword provided
		}
		model.addAttribute("loggedInUserEmail", user.getEmail());

		model.addAttribute("rideList", rideList);
		return "dashboard"; // View name to display search results
	}

	@GetMapping("/renttable")
	public String renttable(Model model, HttpSession session) {

//		 if (session.getAttribute("activeUser") == null) {
//	            String errorMessage = "Please login first!";
//	            model.addAttribute("errorMessage", errorMessage);
//	            return "login";
//	        }
		return "renttable";
	}

	@GetMapping("/Services")
	public String Services(Model model, User user, HttpSession session) {
		String loggedInUserEmail = (String) session.getAttribute("activeUser");
		model.addAttribute("loggedInUserEmail", loggedInUserEmail);
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
//		 if (session.getAttribute("activeUser") == null) {
//	            String errorMessage = "Please login first!";
//	            model.addAttribute("errorMessage", errorMessage);
//	            return "login";
//	        }
		return "rent";
	}

	@GetMapping("/orderDetails")
	public String order(Model model, HttpSession session) {

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

	@GetMapping("/cancelBooking/{rentId}")
	public String cancel(@PathVariable int rentId, Model model, User user, HttpSession session) {

		session.setAttribute("activeUser", user.getEmail());
		model.addAttribute("loggedInUserEmail", user.getEmail());

		rentRepo.deleteById(rentId);
		model.addAttribute("rentList", rentRepo.findAll());
		return "orderDetails";

	}

}