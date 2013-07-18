package com.dreamteam.lookme.bean;

public class ProfileImage {
	
	private long id;
	private String profileId;
	private byte[] image;
	private boolean isMainImage;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public boolean isMainImage() {
		return isMainImage;
	}
	public void setMainImage(boolean isMainImage) {
		this.isMainImage = isMainImage;
	}


}
