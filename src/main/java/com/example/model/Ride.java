package com.example.model;

import org.springframework.web.multipart.MultipartFile;

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
	private String about;
	private int price;
	private int plateNo;
	private String rideImg;

	@Transient
	private MultipartFile newRideImg;

	public MultipartFile getNewRideImg() {
		return newRideImg;
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

	public int getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(int plateNo) {
		this.plateNo = plateNo;
	}

}
