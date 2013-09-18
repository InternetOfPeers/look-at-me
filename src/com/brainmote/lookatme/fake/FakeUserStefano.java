package com.brainmote.lookatme.fake;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.brainmote.lookatme.bean.ProfileImage;

public class FakeUserStefano extends FakeUserGenericImpl {

	public FakeUserStefano(Context context) {
		super(context);
		profile.setNickname("Ste");
		profile.setName("Stefano");
		profile.setSurname("Pirone");
		profile.setStatus("");
		profile.setAge(35);
		profile.setGender("Male");
		profile.setMainProfileImage(createProfileImage(context, "fake_user_profile_image_1.jpg"));
		List<ProfileImage> profileImages = new ArrayList<ProfileImage>();
		profileImages.add(createProfileImage(context, "fake_user_profile_image_1.jpg"));
		profileImages.add(createProfileImage(context, "fake_user_profile_image_2.jpg"));
		profileImages.add(createProfileImage(context, "fake_user_profile_image_3.jpg"));
		profile.setProfileImages(profileImages);
	}

}
