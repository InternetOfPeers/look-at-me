package com.brainmote.lookatme.fake;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.brainmote.lookatme.bean.ProfileImage;

public class FakeUserStefano extends FakeUserGenericImpl {

	public FakeUserStefano(Context context) {
		super(context);
		profile.setNickname("Svalvolo");
		profile.setName("Stefano");
		profile.setSurname("Pirone");
		profile.setStatus("If something can go wrong it will");
		profile.setAge(30);
		profile.setGender("Male");
		profile.setMainProfileImage(createProfileImage(context, "stefano_profile_image_1.jpg"));
		List<ProfileImage> profileImages = new ArrayList<ProfileImage>();
		profileImages.add(createProfileImage(context, "stefano_profile_image_1.jpg"));
		profileImages.add(createProfileImage(context, "stefano_profile_image_2.jpg"));
		profile.setProfileImages(profileImages);
		answers = new ArrayList<String>();
		answers.add("Hi dude, nice to meet you.");
		answers.add("Hi, I'm Stefano and I'm one of the developer of these app..you can use to chatvand meet people around you...it's fun!");
		answers.add("My favourite hobbies are technology stuff and music...I play music..I'm a bass player..and what are your favourite intetest?");
		answers.add("Sorry dude it's time to go...maybe we can meet another time on look@me....see you");
	}

}
