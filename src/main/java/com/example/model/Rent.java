package com.example.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "")
public class Rent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rentId;
	private LocalDateTime rentStartDate;
	private LocalDate rentEndDate;
	private String pickUpLocation;
	private String paymentStatus;
	private String rentPackage;
	private String location;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne
	@JoinColumn(name = "rideId")
	private Ride ride;
	private String fullName;
	private String email;
	private String phone;

	
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getRentId() {
		return rentId;
	}

	public void setRentId(int rentId) {
		this.rentId = rentId;
	}

	public LocalDateTime getRentStartDate() {
		return rentStartDate;
	}

	public void setRentStartDate(LocalDateTime rentStartDate) {
		this.rentStartDate = rentStartDate;
	}

	public LocalDate getRentEndDate() {
		return rentEndDate;
	}

	public void setRentEndDate(LocalDate rentEndDate) {
		this.rentEndDate = rentEndDate;
	}

	public String getPickUpLocation() {
		return pickUpLocation;
	}

	public void setPickUpLocation(String pickUpLocation) {
		this.pickUpLocation = pickUpLocation;
	}

	public String getRentPackage() {
		return rentPackage;
	}

	public void setRentPackage(String rentPackage) {
		this.rentPackage = rentPackage;
	}

	public String getPayementStatus() {
		return paymentStatus;
	}

	public void setPayementStatus(String payementStatus) {
		this.paymentStatus = payementStatus;
	}

	public Rent() {

	}

	public Rent(String location,String fullName, String email, String phone, LocalDateTime rentStartDate, LocalDate rentEndDate,
			String pickUpLocation, String paymentStatus, String rentPackage, User user, Ride ride) {
		super();
		this.location=location;
		this.fullName = fullName;
		this.email = email;
		this.phone = phone;
		this.rentStartDate = rentStartDate;
		this.rentEndDate = rentEndDate;
		this.pickUpLocation = pickUpLocation;
		this.paymentStatus = paymentStatus;
		this.rentPackage = rentPackage;
		this.user = user;
		this.ride = ride;

	}

}
