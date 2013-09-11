package com.brainmote.lookatme.fake;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.brainmote.lookatme.bean.ProfileImage;

public class FakeUserGiuseppe extends FakeUserImpl {

	public FakeUserGiuseppe(Context context) {
		super(context);
		profile.setNickname("Neurone");
		profile.setName("Giuseppe");
		profile.setSurname("Bertone");
		profile.setAge(35);
		profile.setMainProfileImage(createProfileImage(context, "fake_user_main_profile_image.jpg"));
		List<ProfileImage> profileImages = new ArrayList<ProfileImage>();
		profileImages.add(createProfileImage(context, "fake_user_profile_image_1.jpg"));
		profileImages.add(createProfileImage(context, "fake_user_profile_image_2.jpg"));
		profileImages.add(createProfileImage(context, "fake_user_profile_image_3.jpg"));
		profile.setProfileImages(profileImages);
	}

}
