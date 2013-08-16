package com.dreamteam.lookme.bean;

import java.util.ArrayList;
import java.util.List;

public class FullProfile extends BasicProfile {

	private static final long serialVersionUID = 1L;

	private String name;
	private String surname;
	private List<ProfileImage> profileImages = new ArrayList<ProfileImage>();
	private String status;
	private String birthdateYear;
	private String birthdateMonth;
	private String birthdateDay;
	private String birthCountry;
	private String birthCity;
	private String primaryLanguage;
	private String livingCountry;
	private String livingCity;
	private String job;
	private String myDescription;
	private String motto;

	private List<Interest> interestList = new ArrayList<Interest>();

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

	public List<ProfileImage> getProfileImages() {
		return profileImages;
	}

	public void setProfileImages(List<ProfileImage> profileImages) {
		this.profileImages = profileImages;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBirthdateYear() {
		return birthdateYear;
	}

	public void setBirthdateYear(String birthdateYear) {
		this.birthdateYear = birthdateYear;
	}

	public String getBirthdateMonth() {
		return birthdateMonth;
	}

	public void setBirthdateMonth(String birthdateMonth) {
		this.birthdateMonth = birthdateMonth;
	}

	public String getBirthdateDay() {
		return birthdateDay;
	}

	public void setBirthdateDay(String birthdateDay) {
		this.birthdateDay = birthdateDay;
	}

	public String getBirthCountry() {
		return birthCountry;
	}

	public void setBirthCountry(String birthCountry) {
		this.birthCountry = birthCountry;
	}

	public String getBirthCity() {
		return birthCity;
	}

	public void setBirthCity(String birthCity) {
		this.birthCity = birthCity;
	}

	public String getPrimaryLanguage() {
		return primaryLanguage;
	}

	public void setPrimaryLanguage(String primaryLanguage) {
		this.primaryLanguage = primaryLanguage;
	}

	public String getLivingCountry() {
		return livingCountry;
	}

	public void setLivingCountry(String livingCountry) {
		this.livingCountry = livingCountry;
	}

	public String getLivingCity() {
		return livingCity;
	}

	public void setLivingCity(String livingCity) {
		this.livingCity = livingCity;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getMyDescription() {
		return myDescription;
	}

	public void setMyDescription(String myDescription) {
		this.myDescription = myDescription;
	}

	public String getMotto() {
		return motto;
	}

	public void setMotto(String motto) {
		this.motto = motto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Interest> getInterestList() {
		return interestList;
	}

	public void setInterestList(List<Interest> interestList) {
		this.interestList = interestList;
	}

	// public void addInterest(Interest interest) {
	// this.interestList.add(interest);
	// }
	//
	// public void removeInterest(Interest interest) {
	// this.interestList.remove(interest);
	//
	// }

}
