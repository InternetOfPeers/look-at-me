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
		profile.setStatus("");
		profile.setAge(30);
		profile.setGender("Male");
		profile.setMainProfileImage(createProfileImage(context, "riccardo_profile_image_1.png"));
		List<ProfileImage> profileImages = new ArrayList<ProfileImage>();
		profileImages.add(createProfileImage(context, "riccardo_profile_image_1.png"));
		profile.setProfileImages(profileImages);

		// "Hi dude!"
		// "nice to meet you again!"
		// "don't think,just do it!"
		// "keep going"
		// "I'm the dumbest AI ever,why you keep chatting with me?"
	}

}
