package com.dreamteam.util;

import static com.dreamteam.util.ImageUtil.bitmapToByteArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.ProfileImage;

public class FakeUser {

	static final String FAKE_NUMBER = "99999999";
	BasicProfile basicProfile;
	String profileId = FAKE_NUMBER;
	String nickname = "Lucy";
	String gender = "Female";
	int age = 26;
	List<String> tags = null;
	ProfileImage profileImage;
	long profileImageId = 99999999;
	String profileImageFile = "fake_user_profile_image.jpg";

	public FakeUser(Context context) {
		basicProfile = new BasicProfile();
		basicProfile.setId(profileId);
		basicProfile.setNickname(nickname);
		basicProfile.setGender(gender);
		basicProfile.setAge(age);
		basicProfile.setTags(tags);
		profileImage = new ProfileImage();
		profileImage.setId(profileImageId);
		profileImage.setProfileId(profileId);
		InputStream is = null;
		try {
			is = context.getAssets().open(profileImageFile);
			profileImage.setImage(bitmapToByteArray(BitmapFactory.decodeStream(is)));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		basicProfile.setMainProfileImage(profileImage);
	}

	public BasicProfile getBasicProfile() {
		return basicProfile;
	}

	public String getConversationId() {
		return FAKE_NUMBER;
	}

	public String getNodeId() {
		return FAKE_NUMBER;
	}

}
