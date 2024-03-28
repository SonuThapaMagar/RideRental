package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity

public class Rent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rentId;
	private int rentStartDate;
	private int rentEndDate;
	private String rentStatus;
	private String payementStatus;
	
	public int getRentId() {
		return rentId;
	}
	public void setRentId(int rentId) {
		this.rentId = rentId;
	}
	public int getRentStartDate() {
		return rentStartDate;
	}
	public void setRentStartDate(int rentStartDate) {
		this.rentStartDate = rentStartDate;
	}
	public int getRentEndDate() {
		return rentEndDate;
	}
	public void setRentEndDate(int rentEndDate) {
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
	
	
	
	
}
