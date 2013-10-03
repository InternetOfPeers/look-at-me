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

//	@Override
//	public boolean equals(Object profileImage) {
//		if (!(profileImage instanceof ProfileImage)) {
//			if (this == profileImage)
//				return true;
//			if (profileImage == null) {
//				return false;
//			}
//			ProfileImage tmpProfileImage = (ProfileImage) profileImage;
//			if (this.getImage().length != tmpProfileImage.getImage().length) {
//				return false;
//			}
//			for (int i = 0; i < this.getImage().length; i++) {
//				if (this.getImage()[i] != tmpProfileImage.getImage()[i]) {
//					return false;
//				}
//			}
//			return true;
//		}
//		return false;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProfileImage other = (ProfileImage) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
