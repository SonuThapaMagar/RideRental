package com.example.controller;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.Rent;
import com.example.model.Ride;
import com.example.model.User;
import com.example.repository.rentRepository;
import com.example.repository.rideRepository;
import com.example.repository.userRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class RentController {
	@Autowired
	private userRepository uRepo;

	@Autowired
	private rideRepository rideRepo;

	@Autowired
	private rentRepository rentRepo;

	@GetMapping("/rentRide/{rideId}")
	public String rentRide(@PathVariable("rideId") int rideId, Model model, HttpSession session) {

		// Retrieve the logged-in user's ID from the session
Integer loggedInUserId = (Integer) session.getAttribute("loggedInUserId");
// Check if the user is logged in
		if (session.getAttribute("activeUser") == null) {
			String errorMessage = "Please login first!";
			model.addAttribute("errorMessage", errorMessage);
			return "login"; // Redirect to the login page
		}
		 // Retrieve user details from the database using the logged-in user ID
	    Optional<User> optionalUser = uRepo.findById(loggedInUserId);
	    if (!optionalUser.isPresent()) {
	        // Handle case where user is not found
	        return "login"; // Redirect to the login page or handle appropriately
	    }
	    User loggedInUser = optionalUser.get();
	    // Retrieve details of the selected ride
	    Optional<Ride> optionalRide = rideRepo.findById(rideId);
	    Ride ride = optionalRide.orElse(null);
	    if (ride == null) {
	        // Handle case where ride is not found
	        return "dashboard"; // Redirect to the dashboard or handle appropriately
	    }
	    // Pass user and ride details to the rental form
	    model.addAttribute("user", loggedInUser);
	    model.addAttribute("ride", ride);

	    session.setAttribute("selectedRideId", rideId);

		Ride selectedRide = rideRepo.findById(rideId).orElse(null);

		if (selectedRide != null) {
			Rent rent = new Rent(); // Create a new Rent object
			
			model.addAttribute("ride", selectedRide);
			model.addAttribute("selectedRide", selectedRide);
			model.addAttribute("rent", rent); // Add the Rent object to the model
			return "rent"; // Display the rental form
		} else {
			String errorMessage = "Selected ride not found!";
			model.addAttribute("errorMessage", errorMessage);
			return "dashboard"; // Display error page
		}
	}

	@PostMapping("/rentRide")
	public String rentRideDetails(@ModelAttribute Rent rent, Model model, HttpSession session) {

		if (session.getAttribute("activeUser") == null) {
			String errorMessage = "Please login first!";
			model.addAttribute("errorMessage", errorMessage);
			return "login"; // Redirect to the login page
		}

		String fullName = (String) session.getAttribute("activeUser");
		List<User> user = uRepo.findByFullName(fullName);

		if (user == null) {
			String errorMessage = "User not found!";
			model.addAttribute("errorMessage", errorMessage);
			return "dashboard"; // Display error page
		}

		int selectedRideId = (int) session.getAttribute("selectedRideId");

		Ride selectedRide = rideRepo.findById(selectedRideId).orElse(null);

		if (selectedRide != null) {
			// Fetch the price from the selected ride object
			int price = selectedRide.getPrice();
//		        rent.setPrice(price); // Set the price in the rent object

			rent.setRide(selectedRide);
			rentRepo.save(rent);

			selectedRide.setStatus("On Rent");
			rideRepo.save(selectedRide);

			return "orderDetails"; // Display booking confirmation
		} else {
			String errorMessage = "Selected ride not found!";
			model.addAttribute("errorMessage", errorMessage);
			return "dashboard"; // Display error page
		}

	}

	// -------------------Rent Ride Details---------------------

	@GetMapping("/orderDetails")
	public String rideBookingDetails(Model model, HttpSession session) {

		if (session.getAttribute("activeUser") == null) {
			String errorMessage = "Please login first!";
			model.addAttribute("errorMessage", errorMessage);
			return "login";
		}
		List<User> uList = uRepo.findAll();
		model.addAttribute("uList", uList);
		
		List<Rent> rentList = rentRepo.findAll();
		model.addAttribute("rentList", rentList);
		
		return "orderDetails";
	}

	@PostMapping("/orderDetails")
	public String orderDetails(@ModelAttribute Rent rent, HttpSession session, Model model, Ride ride) {

//		String userEmail = (String) session.getAttribute("activeUser");

		Ride rentedRide = rideRepo.findById(rent.getRentId()).orElse(null);

		if (rentedRide != null && rentedRide.getStatus().equals("Available")) {
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
	// -------------------Cancel Booking---------------------

	@GetMapping("/cancelBooking/{rentId}")
	public String cancel(@PathVariable int rentId, Model model, User user, HttpSession session) {

		session.setAttribute("activeUser", user.getEmail());
		model.addAttribute("loggedInUserEmail", user.getEmail());

		rentRepo.deleteById(rentId);
		model.addAttribute("rentList", rentRepo.findAll());
		return "orderDetails";

	}

}
