package com.dreamteam.lookme.bean;

public class FullProfile extends Profile {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String surname;
	private byte[][] profileImages;
	private String status;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public byte[][] getProfileImages() {
		return profileImages;
	}
	public void setProfileImages(byte[][] profileImages) {
		this.profileImages = profileImages;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
