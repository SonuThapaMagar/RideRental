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
@Table(name = "Rent")
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
	
		public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setRide(Ride ride) {
		this.ride = ride;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Ride getRide() {
	    return ride;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	
	public Rent() {

	}

	public Rent(String location,String fullName, String email, String phone, LocalDateTime rentStartDate, LocalDate rentEndDate,
			String pickUpLocation, String paymentStatus, String rentPackage, User user, Ride ride) {
		super();
		this.location=location;
		this.rentStartDate = rentStartDate;
		this.rentEndDate = rentEndDate;
		this.pickUpLocation = pickUpLocation;
		this.paymentStatus = paymentStatus;
		this.rentPackage = rentPackage;
		this.user = user;
		this.ride = ride;

	}

	

}
