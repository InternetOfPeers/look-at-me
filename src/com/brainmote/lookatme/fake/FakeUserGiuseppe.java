package com.brainmote.lookatme.fake;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.brainmote.lookatme.bean.Contact;
import com.brainmote.lookatme.bean.ProfileImage;
import com.brainmote.lookatme.enumattribute.ContactType;

public class FakeUserGiuseppe extends FakeUserGenericImpl {

	public FakeUserGiuseppe(Context context) {
		super(context);
		profile.setNickname("Neurone");
		profile.setName("Giuseppe");
		profile.setSurname("Bertone");
		profile.setStatus("私はジュセッペです");
		profile.setAge(35);
		profile.setGender("Male");
		profile.setMainProfileImage(createProfileImage(context, "giuseppe_profile_image_1.jpg"));
		List<ProfileImage> profileImages = new ArrayList<ProfileImage>();
		profileImages.add(createProfileImage(context, "giuseppe_profile_image_1.jpg"));
		profileImages.add(createProfileImage(context, "giuseppe_profile_image_2.jpg"));
		profileImages.add(createProfileImage(context, "giuseppe_profile_image_3.jpg"));
		profile.setProfileImages(profileImages);
		answers = new ArrayList<String>();
		answers.add("Figata");
		answers.add("ちょっと待って下さい");
		answers.add("Vengono fuori dalle pareti! Vengono fuori dalle fottute pareti!");
		answers.add("Look@me! Is there anybody out there?");
		answers.add("Sorry, I can't. Annalisa is my love.");
		List<Contact> contactList = new ArrayList<Contact>();
		contactList.add(new Contact().setProfileId(profileId).setContactType(ContactType.EMAIL).setReference("giuseppe.bertone@brainmote.com"));
		contactList.add(new Contact().setProfileId(profileId).setContactType(ContactType.LINKEDIN).setReference("giuseppebertone"));
		contactList.add(new Contact().setProfileId(profileId).setContactType(ContactType.FACEBOOK).setReference("giuseppe.bertone"));
		profile.setContactList(contactList);
	}
}
