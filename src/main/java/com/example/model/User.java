package com.example.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "User")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	private String fullName;
	private String email;
	private String license;
	private LocalDate dob;
	private String phone;
	private String password;
	
	@Transient
	private MultipartFile newUserImg;
	
	
	public MultipartFile getNewUserImg() {
		return newUserImg;
	}

	public void setNewUserImg(MultipartFile newUserImg) {
		this.newUserImg = newUserImg;
	}

	public List<Rent> getRentList() {
		return rentList;
	}

	public void setRentList(List<Rent> rentList) {
		this.rentList = rentList;
	}

	@Transient
	private MultipartFile newLicenseFile;

	@OneToOne
	@JoinTable(name = "rentId")
	private Rent rent;

	@OneToOne
	@JoinColumn(name = "rideId")
	private Ride ride;
	
	@OneToOne
	@JoinColumn(name = "reviewId")
	private Review review;
	
	@OneToMany
	(mappedBy = "user")
	private List<Rent> rentList;

	public MultipartFile getNewLicenseFile() {
		return newLicenseFile;
	}

	public void setNewLicenseFile(MultipartFile newLicenseFile) {
		this.newLicenseFile = newLicenseFile;
	}

	public Rent getRent() {
		return rent;
	}

	public void setRent(Rent rent) {
		this.rent = rent;
	}

	public Ride getRide() {
		return ride;
	}

	public void setRide(Ride ride) {
		this.ride = ride;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

}
