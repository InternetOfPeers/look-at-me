package com.brainmote.lookatme.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.brainmote.lookatme.enumattribute.Gender;

public class Profile implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String nickname;
	private int age;

	private Gender gender;

	private String name;
	private String surname;

	private Set<Integer> interestSet = new TreeSet<Integer>();

	private List<Contact> contactList = new ArrayList<Contact>();

	public List<Contact> getContactList() {
		return contactList;
	}

	public void setContactList(List<Contact> contactList) {
		this.contactList = contactList;
	}

	public String getId() {
		return id;
	}

	public void setId(String deviceId) {
		this.id = deviceId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGender() {
		if (gender == null)
			return null;
		return gender.toString();
	}

	public void setGender(String sex) {
		this.gender = Gender.parse(sex);
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Set<Integer> getInterestSet() {
		return interestSet;
	}

	public void setInterestSet(Set<Integer> interestSet) {
		this.interestSet = interestSet;
	}

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

}
