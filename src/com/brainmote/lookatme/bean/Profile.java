package com.brainmote.lookatme.bean;

import java.io.Serializable;
import java.util.List;

import com.brainmote.lookatme.enumattribute.Gender;

public class Profile implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String nickname;
	private int age;

	private Gender gender;
	private List<String> tags;

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

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}