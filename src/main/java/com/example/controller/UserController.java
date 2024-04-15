package com.example.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Random;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.example.model.Rent;
import com.example.model.Ride;
import com.example.model.User;
import com.example.repository.rentRepository;
import com.example.repository.rideRepository;
import com.example.repository.userRepository;

import jakarta.mail.Session;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller

public class UserController {
	
	//------------------Repositories ----------------------
	@Autowired
	private userRepository uRepo;

	@Autowired
	private rideRepository rideRepo;

	@Autowired
	private rentRepository rentRepo;

	//------------------Landing Page ----------------------
	@GetMapping("/")
	public String landingPage() {

		return "index";
	}
	//------------------Service Page ----------------------

	@GetMapping("/indexServices")
	public String services() {
		return "indexService";
	}
	//------------------Bike and Price Page ----------------------

	@GetMapping("/indexView")
	public String View() {
		return "indexView";
	}
	//------------------Testimonials Page----------------------

	@GetMapping("/indexTest")
	public String testimonial() {
		return "indexTest";
	}

//----------------------User Registration---------------------
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

	// ----------------------User Login---------------------
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	// ----------------------After Login Goto Dashboard---------------------
	
	@GetMapping("/dashboard")
	public String userLogin(Model model, User user, HttpSession session) {

		if (session.getAttribute("activeUser") == null) {
			String errorMessage = "Please login first!";
			model.addAttribute("errorMessage", errorMessage);
			return "login";
		}

		List<Ride> rideList = rideRepo.findAll();
		model.addAttribute("rideList", rideList);
		model.addAttribute("loggedInUserEmail", user.getEmail());

		return "dashboard";
	}
	
	@PostMapping("/dashboard")
	public String userLogin(@ModelAttribute User user, Model model, HttpSession session) {
		// Hash the password entered by the user
		String hashedPassword = DigestUtils.shaHex(user.getPassword());
		List<User> users = uRepo.findByEmailAndPassword(user.getEmail(), hashedPassword);

		if (users.size() == 1) {
			User loggedInUser = users.get(0);
			session.setAttribute("loggedInUserId", loggedInUser.getUserId());

			session.setAttribute("activeUser", user.getEmail());

			List<User> uList = uRepo.findAll();
			model.addAttribute("uList", uList);

			List<Ride> rideList = rideRepo.findAll();
			model.addAttribute("rideList", rideList);

			model.addAttribute("loggedInUserEmail", user.getEmail());

			return "dashboard";

		} else if (users.isEmpty()) {
			model.addAttribute("errorMessage", "Invalid username or password !!");
		} else {
			model.addAttribute("errorMessage", "Multiple users found with the same email and password combination.");
		}
		return "login";

	}

	//-------------------Logout---------------------
	@GetMapping("/logoutUser")
	public String logoutUser(HttpSession session) {
		session.invalidate();
		return "login";
	}
	
	//-------------------Forgot Password---------------------

	@GetMapping("/forgotPassword")
	public String loadForgotPassword() {

		return "newForgot";
	}

	@PostMapping("/submitForgotPassword")
	public String submitForgotPassword(@RequestParam("email") String email, Model model) {
		// Find the user in the database
		User user = uRepo.findByEmail(email);
		if (user != null) {
			// If user is found, proceed to reset password page
//			model.addAttribute("userId", user.getUserId()); // You may need to pass the user's ID to the reset password
			return "redirect:/resetPassword?userId=" + user.getUserId();
			// page
//			return "newPassword";
		} else {
			// If user is not found, display an error message
			model.addAttribute("errorMessage", "No account found for the provided email address.");
			return "newForgot";
		}
	}
	//-------------------Reset New Password---------------------

	@GetMapping("/resetPassword")
	public String resetPasswordForm(@RequestParam("userId") String userId, Model model) {
		// Pass the userId to the reset password form
		Integer userIdInt = Integer.parseInt(userId);
		model.addAttribute("userId", userId);
		return "newPassword";
	}

	@PostMapping("/changedPassword")
	public String resetPassword(@RequestParam("userId") Integer userId, @RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword, Model model) {

		// Check if the new password matches the confirm password
		if (!newPassword.equals(confirmPassword)) {
			// If passwords don't match, return an error message
			model.addAttribute("errorMessage", "New password and confirm password do not match.");
			return "newPassword"; // Return to the password reset page with an error message
		}

		Optional<User> userOptional = uRepo.findById(userId);
		if (userOptional.isPresent()) {
			// User found, update password
			User user = userOptional.get();
			String hashedPassword = DigestUtils.shaHex(newPassword); // Hash the new password
			user.setPassword(hashedPassword);
			uRepo.save(user);
			return "login"; // Display a success message
		} else {
			// Handle the case where the user is not found
			model.addAttribute("errorMessage", "User not found.");
			return "newForgot";
		}
	}

	// -----------------Change New Password--------------------
	@GetMapping("/loadChangePassword")
	public String loadChangePassword(HttpSession session, Model model) {

		Integer loggedInUserId = (Integer) session.getAttribute("loggedInUserId");
		if (loggedInUserId == null) {
			return "login";
		}
		model.addAttribute("userId", loggedInUserId);
		return "changeNewPass";
	}

	@PostMapping("/changePassword")
	public String changePassword( @RequestParam("userId") Integer userId,
			@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword, Model model) {

		Optional<User> optional = uRepo.findById(userId);
		if (optional.isPresent()) {
			User user = optional.get();

			String hashedOldPassword = DigestUtils.shaHex(oldPassword);

			if (!user.getPassword().equals(hashedOldPassword)) {

				model.addAttribute("errorMessage", "Incorrect old password !!");
				return "changeNewPass";

			}

			if (!newPassword.equals(confirmPassword)) {
				model.addAttribute("errorMessage", "New password and confirm password do not match !!");
				return "changeNewPass";
			}

			String hashedNewPassword = DigestUtils.shaHex(newPassword);
			user.setPassword(hashedNewPassword);
			uRepo.save(user);
			return "login";
		} else {
			model.addAttribute("errorMessage", "User not found !!");
			return "changeNewPass";

		}
	}
	//---------------------------------------

	@GetMapping("/index")
	public String index(Model model) {
		List<Ride> rideList = rideRepo.findAll();
		model.addAttribute("rideList", rideList);
		return "index";
	}
	//-------------------Product Page---------------------

	@GetMapping("/product")
	public String product() {
		return "product";
	}
	//-------------------Edit Profile---------------------

	// Edit User
	@GetMapping("/editProfile/{userId}")
	public String editProfile(@PathVariable int userId, HttpSession session, Model model) {

		if (session.getAttribute("activeUser") == null) {
			session.setAttribute("error", "Please login first!");
			return "login"; 		}

		User user = uRepo.getById(userId);

		model.addAttribute("userObject", user);
		return "profile";
	}

	@PostMapping("/editProfile")
	public String editProfile(@ModelAttribute User user, Model model, HttpSession session) {

		try {
			// Retrieve the logged-in user ID
			Integer loggedInUserId = (Integer) session.getAttribute("loggedInUserId");
			if (loggedInUserId == null) {
				return "login";
			}

			// Retrieve the user from the database based on the logged-in user ID
			User existingUser = uRepo.findById(loggedInUserId).orElse(null);
			if (existingUser == null) {
				// Handle case where user is not found
				return "redirect:/dashboard"; // For example, redirect to the dashboard
			}

			// Update the user's profile data
			existingUser.setFullName(user.getFullName());
			existingUser.setEmail(user.getEmail());
			existingUser.setDob(user.getDob());
			existingUser.setPhone(user.getPhone());

			// Handle file upload for profile image
			MultipartFile newUserImg = user.getNewLicenseFile();
			if (newUserImg != null && !newUserImg.isEmpty()) {
				// Save the profile image file to a location on the server

				File saveDir = new ClassPathResource("static/assets").getFile();

				Path imagePath = Paths
						.get(saveDir.getAbsolutePath() + File.separator + newUserImg.getOriginalFilename());

				try (InputStream inputStream = newUserImg.getInputStream()) {

					Files.copy(newUserImg.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

				}
				// Update the user's profile image path in the database
				String imagePathString = "/" + newUserImg.getOriginalFilename();
				existingUser.setLicense(imagePathString);

				uRepo.save(existingUser);
				model.addAttribute("uList", uRepo.findAll());
				return "login";

			}
		}

		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Failed to save ride details");
			return "profile";
		}
		return "profile";
	}
	//-------------------Search---------------------

	@GetMapping("/search")
	public String searchRides(@RequestParam(required = false) String keyword, User user, Model model,
			HttpSession session) {
		// if (session.getAttribute("activeUser") == null) {
		// String errorMessage = "Please login first!";
		// model.addAttribute("errorMessage", errorMessage);
		// return "login";
		// }
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

		// if (session.getAttribute("activeUser") == null) {
		// String errorMessage = "Please login first!";
		// model.addAttribute("errorMessage", errorMessage);
		// return "login";
		// }
		return "renttable";
	}
	//-------------------Service Page---------------------

	@GetMapping("/Services")
	public String Services(Model model, User user, HttpSession session) {
		String loggedInUserEmail = (String) session.getAttribute("activeUser");
		model.addAttribute("loggedInUserEmail", loggedInUserEmail);
		return "Services";
	}
	//-------------------Testimonials---------------------

	@GetMapping("/test")
	public String test() {
		return "test";
	}
	//-------------------Bikes & Price---------------------

	@GetMapping("/view")
	public String view() {
		return "view";
	}
	//-------------------Rent Ride---------------------

	@GetMapping("/rentRide/{rideId}")
	public String rentRide(Model model, HttpSession session) {
		 if (session.getAttribute("activeUser") == null) {
		 String errorMessage = "Please login first!";
		 model.addAttribute("errorMessage", errorMessage);
		 return "login";
		 }
		return "rent";
	}

	@PostMapping("/rentRide/{rideId}")
	public String rentRide(@PathVariable int rideId, @ModelAttribute Rent rent, HttpSession session, Model model) {
		Ride rentedRide = rideRepo.findById(rideId).orElse(null);
		if (rentedRide != null && rentedRide.getStatus().equals("available")) {
			model.addAttribute("rentedRide", rentedRide);
			return "rent";
		} else {
			model.addAttribute("errorMessage", "Selected ride is not available for rent.");
			return "dashboard";
		}

	}
	//-------------------Rent Ride Details---------------------

	@GetMapping("/orderDetails")
	public String order(Model model, HttpSession session) {

		List<Rent> rentList = rentRepo.findAll();
		model.addAttribute("rentList", rentList);
		return "orderDetails";
	}

	@PostMapping("/orderDetails")
	public String orderDetails(@ModelAttribute Rent rent, HttpSession session, Model model, Ride ride) {

		String userEmail = (String) session.getAttribute("activeUser");

		Ride rentedRide = rideRepo.findById(rent.getRentId()).orElse(null);

		if (rentedRide != null && rentedRide.getStatus().equals("available")) {
			// Update the status of the ride to "on rent"
			rentedRide.setStatus("On Rent");
			rideRepo.save(rentedRide);

			// Save the rent details
			Rent savedRent = rentRepo.save(rent);

			// Retrieve the updated list of rents and rides
			List<Rent> rentList = rentRepo.findAll();
			List<Ride> rideList = rideRepo.findAll();

			// Update the model with the updated lists and redirect to the order details
			model.addAttribute("rentList", rentList);
			model.addAttribute("rideList", rideList);
			return "orderDetails";
		} else {
			model.addAttribute("errorMessage", "Selected ride is not available for rent.");
			return "orderDetails";
		}
	}
	//-------------------Cancel Booking---------------------

	@GetMapping("/cancelBooking/{rentId}")
	public String cancel(@PathVariable int rentId, Model model, User user, HttpSession session) {

		session.setAttribute("activeUser", user.getEmail());
		model.addAttribute("loggedInUserEmail", user.getEmail());

		rentRepo.deleteById(rentId);
		model.addAttribute("rentList", rentRepo.findAll());
		return "orderDetails";

	}
	

}