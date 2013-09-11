package com.brainmote.lookatme.fake;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.brainmote.lookatme.bean.ProfileImage;

public class FakeUserRiccardo extends FakeUserGenericImpl {

	public FakeUserRiccardo(Context context) {
		super(context);
		profile.setNickname("AlfaOmega");
		profile.setName("Riccardo");
		profile.setSurname("Alfrilli");
		profile.setStatus("Due rette parallele non si incontrano mai, e se si incontrano non si salutano.");
		profile.setAge(35);
		profile.setMainProfileImage(createProfileImage(context, "giuseppe_main_profile_image.jpg"));
		List<ProfileImage> profileImages = new ArrayList<ProfileImage>();
		profileImages.add(createProfileImage(context, "fake_user_profile_image_1.jpg"));
		profileImages.add(createProfileImage(context, "fake_user_profile_image_2.jpg"));
		profileImages.add(createProfileImage(context, "fake_user_profile_image_3.jpg"));
		profile.setProfileImages(profileImages);
	}

}
