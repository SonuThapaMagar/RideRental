package com.example.controller;

import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.Admin;
import com.example.model.Ride;
import com.example.repository.rideRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
//@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private rideRepository rideRepo;

	// static credential for admin
	private static final String adminEmail = "admin2024@gmail.com";
	// Password hashing
	private static final String adminPassword = "admin@2024";

	@GetMapping("/admin")
	public String admin() {
		return "adminlogin";
	}

	// Admin login via static credential
	@PostMapping("/adminLogin")
	public String adminLogin(@ModelAttribute Admin a, Model model) {

		// Checking the credential
		if (a.getEmail().equals(adminEmail) && a.getPassword().equals(adminPassword)) {
			model.addAttribute("success", "Login successful!"); // Optional
			return "admindash.html";
		}
		model.addAttribute("error", "Invalid credentials");
		return "adminlogin";
	}

	@GetMapping("/add")
	public String add() {
		return "add";
	}


	@PostMapping("/addRide")
	public String addRide(@ModelAttribute Ride ride, @RequestParam	MultipartFile rideImage,Model model) throws IOException{

		try {
			Ride newRide=new Ride();
			newRide.setType(ride.getType());
			newRide.setPlateNo(ride.getPlateNo());
			newRide.setStatus(ride.getStatus());
			newRide.setImageUrl(ride.getImageUrl());
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		rideRepo.save(ride);
		return "add";
	}


}
