package com.brainmote.lookatme.fake;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.brainmote.lookatme.bean.ProfileImage;

public class FakeUserGiuseppe extends FakeUserGenericImpl {

	public FakeUserGiuseppe(Context context) {
		super(context);
		profile.setNickname("Lapalissiano");
		profile.setName("Giuseppe");
		profile.setSurname("Bertone");
		profile.setStatus("私はジュセッペです");
		profile.setAge(35);
		profile.setGender("Male");
		profile.setMainProfileImage(createProfileImage(context, "giuseppe_profile_image_1.jpg"));
		List<ProfileImage> profileImages = new ArrayList<ProfileImage>();
		profileImages.add(createProfileImage(context, "giuseppe_profile_image_1.jpg"));
		profile.setProfileImages(profileImages);

		// Testi chat

		// Interessi

	}

}
