package com.dreamteam.lookme.bean;

import java.io.Serializable;
import java.util.List;

public class BasicProfile implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String MALE = "M";
	public static final String FEMALE = "F";

	private transient long id;
	private transient String deviceId;
	
	private String nickname;
	private byte[] mainProfileImage;
	private String sex;
	private List<String> tags;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public byte[] getMainProfileImage() {
		return mainProfileImage;
	}
	public void setMainProfileImage(byte[] mainProfileImage) {
		this.mainProfileImage = mainProfileImage;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

}
