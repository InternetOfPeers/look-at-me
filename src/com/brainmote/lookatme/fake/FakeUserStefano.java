package com.brainmote.lookatme.fake;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.brainmote.lookatme.bean.Contact;
import com.brainmote.lookatme.bean.ProfileImage;
import com.brainmote.lookatme.enumattribute.ContactType;

public class FakeUserStefano extends FakeUserGenericImpl {

	public FakeUserStefano(Context context) {
		super(context);
		profile.setNickname("Svalvolo");
		profile.setName("Stefano");
		profile.setSurname("Pirone");
		profile.setStatus("If anything can go wrong, it will");
		profile.setAge(30);
		profile.setGender("Male");
		profile.setMainProfileImage(createProfileImage(context, "stefano_profile_image_1.jpg"));
		List<ProfileImage> profileImages = new ArrayList<ProfileImage>();
		profileImages.add(createProfileImage(context, "stefano_profile_image_1.jpg"));
		profileImages.add(createProfileImage(context, "stefano_profile_image_2.jpg"));
		profile.setProfileImages(profileImages);
		answers = new ArrayList<String>();
		answers.add("Hi dude, nice to meet you.");
		answers.add("Hi, I'm Stefano and I'm one of the developer of this app... you can use it to chat and meet people around you... it's fun!");
		answers.add("My favourite hobbies are technology stuff and music... I play music... I'm a bass player... and what are your favourite interests?");
		answers.add("Sorry dude it's time to go... maybe we can meet another time on look@me... see you");
		List<Contact> contactList = new ArrayList<Contact>();
		contactList.add(new Contact().setProfileId(profileId).setContactType(ContactType.EMAIL).setReference("valvonauta83@gmail.com"));
		profile.setContactList(contactList);
	}

}
