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

import com.example.model.Ride;
import com.example.model.User;
import com.example.repository.rideRepository;
import com.example.repository.userRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class UserController {
	@Autowired
	private userRepository uRepo;

	@Autowired
	private rideRepository rideRepo;
	
	@GetMapping("/")
	public String landingPage() {
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/userLogin")
	public String userLogin(@ModelAttribute User user, Model model, HttpSession session) {
		// Hash the password entered by the user
		String hashedPassword = DigestUtils.shaHex(user.getPassword());

		if (uRepo.existsByEmailAndPassword(user.getEmail(), hashedPassword)) {

			session.setAttribute("activeUser", user.getFullName());
			session.setMaxInactiveInterval(30);
			List<User> uList = uRepo.findAll();
			model.addAttribute("uList", uList);

			List<Ride> rideList=rideRepo.findAll();
			model.addAttribute("rideList", rideList);
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
		List<Ride> rideList=rideRepo.findAll();
		model.addAttribute("rideList", rideList);
		return "dashboard";
	}
	
	
	@GetMapping("/product")
	public String product() {
		return "product";
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
	public String rentRide() {
		
		return "rent";
	}
}
