package com.brainmote.lookatme.fake;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.brainmote.lookatme.bean.ProfileImage;

public class FakeUserCarlo extends FakeUserGenericImpl {

	public FakeUserCarlo(Context context) {
		super(context);
		profile.setNickname("Carlo");
		profile.setName("Carlo");
		profile.setSurname("Tassi");
		profile.setStatus("make love, not engineering");
		profile.setAge(29);
		profile.setGender("Male");
		profile.setMainProfileImage(createProfileImage(context, "carlo_profile_image_1.jpg"));
		List<ProfileImage> profileImages = new ArrayList<ProfileImage>();
		profileImages.add(createProfileImage(context, "carlo_profile_image_1.jpg"));
		profileImages.add(createProfileImage(context, "carlo_profile_image_2.jpg"));
		profile.setProfileImages(profileImages);
		answers = new ArrayList<String>();
		answers.add("Hi! Nice to meet you!");
		answers.add("See you later");
		answers.add("Don't forget rating this app!!");
		answers.add("Look@Me it's a new way to know cool people!");
		answers.add("What are you waiting for? Turn on WiFi and return to search nerby!");
	}

}
