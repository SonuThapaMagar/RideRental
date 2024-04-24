package com.example.model;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "Ride")
public class Ride {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rideId;
	private String type;
	private String status;
	
	@Column(length = 1000)
	private String about;
	private int price;
	private String plateNo;
	private String rideImg;

	// Price for different rental packages
	private int pricePerHour;
	private int pricePer3Hours;
	private int pricePerHalfDay;
	private int pricePerFullDay;

	@Transient
	private MultipartFile newRideImg;

	public MultipartFile getNewRideImg() {
		return newRideImg;
	}

	public int getPricePerHour() {
		return pricePerHour;
	}

	public void setPricePerHour(int pricePerHour) {
		this.pricePerHour = pricePerHour;
	}

	public int getPricePer3Hours() {
		return pricePer3Hours;
	}

	public void setPricePer3Hours(int pricePer3Hours) {
		this.pricePer3Hours = pricePer3Hours;
	}

	public int getPricePerHalfDay() {
		return pricePerHalfDay;
	}

	public void setPricePerHalfDay(int pricePerHalfDay) {
		this.pricePerHalfDay = pricePerHalfDay;
	}

	public int getPricePerFullDay() {
		return pricePerFullDay;
	}

	public void setPricePerFullDay(int pricePerFullDay) {
		this.pricePerFullDay = pricePerFullDay;
	}

	public void setNewRideImg(MultipartFile newRideImg) {
		this.newRideImg = newRideImg;
	}

	public String getRideImg() {
		return rideImg;
	}

	public void setRideImg(String rideImg) {
		this.rideImg = rideImg;
	}

	public int getRideId() {
		return rideId;
	}

	public void setRideId(int rideId) {
		this.rideId = rideId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	

}
