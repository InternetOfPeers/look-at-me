package com.dreamteam.lookme.bean;

import java.io.Serializable;

import android.graphics.Bitmap;

public class ProfileImage implements Serializable {

	private static final long serialVersionUID = 1L;

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

	public Bitmap getImageBitmap() {
		return null;
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
