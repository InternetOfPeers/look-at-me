package com.brainmote.lookatme.bean;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ProfileImage implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String profileId;
	private byte[] image;
	private Bitmap bitmap;
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
		if (bitmap != null)
			return bitmap;
		return BitmapFactory.decodeByteArray(getImage(), 0, getImage().length);
	}

	public void setImage(byte[] image) {
		this.image = image;
		bitmap = null;
	}

	public boolean isMainImage() {
		return isMainImage;
	}

	public void setMainImage(boolean isMainImage) {
		this.isMainImage = isMainImage;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null) {
			return false;
		}
		ProfileImage profileImage = (ProfileImage) o;
		if (this.getImage().length != profileImage.getImage().length) {
			return false;
		}
		for (int i = 0; i < this.getImage().length; i++) {
			if (this.getImage()[i] != profileImage.getImage()[i]) {
				return false;
			}
		}
		return true;
	}

}
