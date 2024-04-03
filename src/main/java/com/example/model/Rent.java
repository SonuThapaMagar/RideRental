package com.example.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "")
public class Rent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rentId;
	private LocalDate rentStartDate;
	private LocalDate rentEndDate;
	private String rentStatus;
	private LocalTime rentTime;
	private String payementStatus;

	
	
	

	public LocalTime getRentTime() {
		return rentTime;
	}

	public void setRentTime(LocalTime rentTime) {
		this.rentTime = rentTime;
	}

	public int getRentId() {
		return rentId;
	}

	public void setRentId(int rentId) {
		this.rentId = rentId;
	}

	public LocalDate getRentStartDate() {
		return rentStartDate;
	}

	public void setRentStartDate(LocalDate rentStartDate) {
		this.rentStartDate = rentStartDate;
	}

	public LocalDate getRentEndDate() {
		return rentEndDate;
	}

	public void setRentEndDate(LocalDate rentEndDate) {
		this.rentEndDate = rentEndDate;
	}

	public String getRentStatus() {
		return rentStatus;
	}

	public void setRentStatus(String rentStatus) {
		this.rentStatus = rentStatus;
	}

	public String getPayementStatus() {
		return payementStatus;
	}

	public void setPayementStatus(String payementStatus) {
		this.payementStatus = payementStatus;
	}

	public Rent(int rentId, LocalDate rentStartDate, LocalDate rentEndDate, String rentStatus, String payementStatus,
			User user, Ride ride) {
		super();
		this.rentId = rentId;
		this.rentStartDate = rentStartDate;
		this.rentEndDate = rentEndDate;
		this.rentStatus = rentStatus;
		this.payementStatus = payementStatus;
	}
	
	

}
